package com.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.service.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>{
	
	@Query(value = "SELECT * FROM appointment WHERE professional_id = :id AND STR_TO_DATE(appointment_date, '%d/%m/%Y %H:%i') > NOW() AND status IN (:status)", nativeQuery = true)
	List<Appointment> getActiveAppointmentsByProfessional(@Param("id") Long id, @Param("status") List<String> status);
	
	@Query(value = " SELECT * FROM appointment WHERE professional_id = :id AND status IN (:status) ", nativeQuery = true)
	List<Appointment> getProfessionalRecords(@Param("id")Long id, @Param("status") List<String>  status);

	
	@Query(value = "SELECT * FROM appointment WHERE client_id = :id AND STR_TO_DATE(appointment_date, '%d/%m/%Y %H:%i') > NOW() AND status IN (:status)", nativeQuery = true)
	List<Appointment> getActiveAppointmentsByClient(@Param("id") Long id, @Param("status") List<String> status);
	
	@Query(value = " SELECT * FROM appointment WHERE client_id = :id AND status IN (:status) ", nativeQuery = true)
	List<Appointment> getclientRecords(@Param("id")Long id, @Param("status") List<String>  status);
}
