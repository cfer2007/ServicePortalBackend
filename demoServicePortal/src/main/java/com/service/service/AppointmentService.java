package com.service.service;

import org.springframework.stereotype.Service;

import com.service.auth.enums.Role;
import com.service.auth.model.User;
import com.service.auth.model.UserRole;
import com.service.dto.AppointmentRequest;
import com.service.enums.AppointmentStatus;
import com.service.model.Appointment;
import com.service.model.Client;
import com.service.model.Professional;
import com.service.model.ProfessionalAvailability;
import com.service.notification.enums.NotificationType;
import com.service.notification.service.NotificationService;
import com.service.repository.AppointmentRepository;
import com.service.repository.ClientRepository;
import com.service.repository.ProfessionalAvailabilityRepository;
import com.service.repository.ProfessionalRepository;

import jakarta.transaction.Transactional;

@Service
public class AppointmentService {

    private final AppointmentRepository repo;
    private final ClientRepository clientRepo;
    private final ProfessionalRepository professionalRepo;
    private final ProfessionalAvailabilityRepository availabilityRepo;
    private final NotificationService notificationService;

    public AppointmentService(
            AppointmentRepository repo,
            ClientRepository clientRepo,
            ProfessionalRepository professionalRepo,
            ProfessionalAvailabilityRepository availabilityRepo,
            NotificationService notificationService) {
        this.repo = repo;
        this.clientRepo = clientRepo;
        this.professionalRepo = professionalRepo;
        this.availabilityRepo = availabilityRepo;
        this.notificationService = notificationService;
    }

    @Transactional
    public Appointment createAppointment(AppointmentRequest req) {

        Client client = clientRepo.findById(req.getClientId())
                .orElseThrow(() -> new RuntimeException("Client no encontrado"));

        Professional professional = professionalRepo.findById(req.getProfessionalId())
                .orElseThrow(() -> new RuntimeException("Professional no encontrado"));

        // ✅ Validar cita duplicada
        boolean exists = repo.existsByProfessionalAndDate(
                req.getProfessionalId(),
                req.getAppointmentDate()
        );

        if (exists) {
            throw new RuntimeException("Ya existe una cita en ese horario");
        }

        ProfessionalAvailability availability = null;
        if (req.getProfessionalAvailabilityId() != null) {
            availability = availabilityRepo.findById(req.getProfessionalAvailabilityId())
                    .orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada"));
        }

        Appointment appointment = new Appointment();
        appointment.setClient(client);
        appointment.setProfessional(professional);
        appointment.setProfessionalAvailability(availability);
        appointment.setAppointmentDate(req.getAppointmentDate());
        appointment.setStatus(AppointmentStatus.PENDING);

        Appointment saved = repo.save(appointment);

        // ✅ profesional.getUser() ya viene cargado desde BD
        Long professionalRoleId = professional.getUser()
                .getRoles()
                .stream()
                .filter(r -> r.getRole() == Role.PROFESSIONAL)
                .findFirst()
                .map(UserRole::getUserRoleId)
                .orElse(null);

        notificationService.sendNotification(
                professionalRoleId,
                "Nueva cita solicitada",
                "Tienes una nueva cita programada el " + saved.getAppointmentDate(),
                NotificationType.APPOINTMENT
        );

        return saved;
    }
    
    @Transactional
    public void updateStatus(Long appointmentId, AppointmentStatus newStatus, Long actorUserRoleId) {

        Appointment appt = repo.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment no encontrada"));

        AppointmentStatus oldStatus = appt.getStatus();
        if (oldStatus == newStatus) return; // no hacer nada si no cambió

        appt.setStatus(newStatus);
        repo.save(appt);

        // ✅ Si es un bloqueo (no tiene cliente), no se notifica
        if (appt.getClient() == null || appt.getProfessional() == null) {
            return;
        }

        Long proRoleId = extractRoleId(appt.getProfessional().getUser(), Role.PROFESSIONAL);
        Long clientRoleId = extractRoleId(appt.getClient().getUser(), Role.USER);

        // ✅ lógica de notificaciones por transición
        switch (newStatus) {

            case CONFIRMED -> {
                // Confirmada -> Notificar cliente (a menos que él mismo lo marcó)
                if (!actorUserRoleId.equals(clientRoleId)) {
                    sendSafe(
                        clientRoleId,
                        "Cita confirmada",
                        "Tu cita fue confirmada para el " + appt.getAppointmentDate(),
                        NotificationType.APPOINTMENT
                    );
                }
            }

            case CANCELLED_BY_PROFESSIONAL -> {
                sendSafe(
                    clientRoleId,
                    "Cita cancelada",
                    "El profesional canceló tu cita del " + appt.getAppointmentDate(),
                    NotificationType.APPOINTMENT
                );
            }

            case CANCELLED_BY_CLIENT -> {
                sendSafe(
                    proRoleId,
                    "Cita cancelada por el cliente",
                    "El cliente canceló la cita del " + appt.getAppointmentDate(),
                    NotificationType.APPOINTMENT
                );
            }

            case COMPLETED -> {
                // ✅ Pedir reseña al cliente
                sendSafe(
                    clientRoleId,
                    "¿Cómo te fue en tu cita?",
                    "Tu cita del " + appt.getAppointmentDate() + " fue marcada como completada. ¡Deja tu reseña!",
                    NotificationType.APPOINTMENT
                );

                // ✅ Avisar al profesional
                sendSafe(
                    proRoleId,
                    "Cita completada",
                    "La cita del " + appt.getAppointmentDate() + " ya fue marcada como completada.",
                    NotificationType.APPOINTMENT
                );
            }

            case NO_SHOW -> {
                // Profesional marcó no-show -> avisar cliente
                if (actorUserRoleId.equals(proRoleId)) {
                    sendSafe(
                        clientRoleId,
                        "Ausencia registrada",
                        "El profesional reportó que no asististe a la cita del " + appt.getAppointmentDate(),
                        NotificationType.APPOINTMENT
                    );
                } else {
                    // Cliente marcó no-show (raro, pero posible) -> avisar profesional
                    sendSafe(
                        proRoleId,
                        "Ausencia registrada",
                        "El cliente reportó ausencia en la cita del " + appt.getAppointmentDate(),
                        NotificationType.APPOINTMENT
                    );
                }
            }

            default -> {
                // Otros estados en el futuro
            }
        }
    }

    private Long extractRoleId(User user, Role role) {
        return user.getRoles()
            .stream()
            .filter(r -> r.getRole() == role)
            .findFirst()
            .map(UserRole::getUserRoleId)
            .orElse(null);
    }

    private void sendSafe(Long roleId, String title, String body, NotificationType type) {
        if (roleId != null) {
            notificationService.sendNotification(roleId, title, body, type);
        }
    }

}


