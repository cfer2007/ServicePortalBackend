package com.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.service.enums.ReservationStatus;
import com.service.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>{
	
	@Query(value = "SELECT * FROM appointment WHERE professional_id = ?1 AND STR_TO_DATE(appointment_date, '%d/%m/%Y %H:%i') > NOW() AND status IN (?2)", nativeQuery = true)
	List<Appointment> getAppointmentsByProfessional(
	    @Param("id") Long id,
	    @Param("status") List<ReservationStatus> status
	);

	
	@Query(value = "SELECT * FROM appointment WHERE client_id = ?1 AND STR_TO_DATE(appointment_date, '%d/%m/%Y %H:%i') > NOW();", nativeQuery = true)
	List<Appointment> getAppointmentsByClient(@Param("clientId")Long id);

}
