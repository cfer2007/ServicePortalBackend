package com.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@GetMapping("/get/pending/{id}")
	public ResponseEntity<List<Reservation>> getPendingReservation(@PathVariable Long id){		
		List<Reservation> list = repo.findPendingReservations(id);
		return ResponseEntity.ok(list);
	}
}