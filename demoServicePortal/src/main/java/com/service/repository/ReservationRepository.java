package com.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.service.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{
	
	@Query(value = "SELECT * FROM reservation WHERE professional_id = ?1 AND STR_TO_DATE(reservation_date, '%d/%m/%Y %H:%i') > NOW();", nativeQuery = true)
	List<Reservation> findPendingReservations(@Param("professionalId")Long id);

}
