package com.service.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.service.dto.AppointmentDTO;
import com.service.enums.ReservationStatus;
import com.service.exception.ResourceNotFoundException;
import com.service.model.Appointment;
import com.service.repository.AppointmentRepository;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {
	@Autowired
	private AppointmentRepository repo;
	
	@PostMapping("/add")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<Appointment> setAppointment(@RequestBody Appointment appointment) {
		appointment.setStatus(ReservationStatus.PENDING);
		Appointment app = repo.save(appointment);
		return ResponseEntity.ok(app);
	}
	
	@GetMapping("/list/professional/{id}")
	public ResponseEntity<List<Appointment>> getAppointmentsByProfessional(@PathVariable Long id, @RequestParam String status) {
		List<ReservationStatus> statusList = Arrays.stream(status.split(","))
                .map(String::trim)
                .map(ReservationStatus::valueOf)
                .toList();
		
	    List<Appointment> list = repo.getAppointmentsByProfessional(id, statusList);
	    return ResponseEntity.ok(list);
	}

	
	@GetMapping("/list/client/{id}")
	public ResponseEntity<List<Appointment>> getAppointmentsByClient(@PathVariable Long id){		
		List<Appointment> list = repo.getAppointmentsByClient(id);
		return ResponseEntity.ok(list);
	}
	
	@PutMapping("/edit/{id}")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> editAppointment(@PathVariable Long id, @RequestBody AppointmentDTO dto){
		Appointment existing = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Appointment not found", 1001));
		dto.updateEntity(existing);
		Appointment updated = repo.save(existing);
		return ResponseEntity.ok(updated.getAppointmentId());
	}
}