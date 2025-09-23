package com.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.service.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>{
	
	//@Query(value = "SELECT * FROM appointment WHERE professional_id = ?1 AND STR_TO_DATE(appointment_date, '%d/%m/%Y %H:%i') > NOW() AND status IN ('PENDING','CONFIRMED','BLOCKED') OR status = 'IN_PROGRESS'", nativeQuery = true)
	@Query(value = "SELECT * FROM appointment " +
            "WHERE professional_id = :id " +
            "AND STR_TO_DATE(appointment_date, '%d/%m/%Y %H:%i') > NOW() " +
            "AND status IN (:status)", nativeQuery = true)
List<Appointment> getActiveAppointmentsByProfessional(@Param("id") Long id, @Param("status") List<String> status);

	
	@Query(value = """
		    SELECT * FROM appointment
		    WHERE professional_id = ?1
		    AND status IN (
		      'CANCELLED_BY_CLIENT',
		      'CANCELLED_BY_PROFESSIONAL',
		      'IN_PROGRESS',
		      'COMPLETED',
		      'NO_SHOW'
		    )
		    """, nativeQuery = true)
	List<Appointment> getProfessionalRecords(Long professionalId);


	
	@Query(value = "SELECT * FROM appointment WHERE client_id = ?1 AND STR_TO_DATE(appointment_date, '%d/%m/%Y %H:%i') > NOW();", nativeQuery = true)
	List<Appointment> getAppointmentsByClient(@Param("clientId")Long id);
}
