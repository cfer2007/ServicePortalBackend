package com.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.enums.Status;
import com.service.model.Reservation;
import com.service.repository.ReservationRepository;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

	@Autowired
	private ReservationRepository repo;
	
	@PostMapping("/add")
	public ResponseEntity<Reservation> setReservation(@RequestBody Reservation reservation) {
		reservation.setStatus(Status.INGRESADA);
		Reservation res = repo.save(reservation);
		return ResponseEntity.ok(res);
	}
}