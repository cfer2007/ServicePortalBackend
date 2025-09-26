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
import com.service.enums.AppointmentStatus;
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
		appointment.setStatus(AppointmentStatus.PENDING);
		Appointment app = repo.save(appointment);
		return ResponseEntity.ok(app);
	}
	
	@PostMapping("/add/blocked")
	@PreAuthorize("hasAuthority('PROFESSIONAL')")
	public ResponseEntity<Appointment> setBlockAppointment(@RequestBody Appointment appointment) {
		Appointment app = repo.save(appointment);
		return ResponseEntity.ok(app);
	}
	
	@GetMapping("/list/professional/{id}")
	public ResponseEntity<List<Appointment>> getActiveAppointmentsByProfessional(@PathVariable Long id, @RequestParam String status) {
		List<String> statusList = Arrays.stream(status.split(",")).map(String::trim).toList();		
	    List<Appointment> list = repo.getActiveAppointmentsByProfessional(id, statusList);
	    return ResponseEntity.ok(list);
	}
	
	@GetMapping("/list/professional/records/{id}")
	public ResponseEntity<List<Appointment>> getProfessionalRecords(@PathVariable Long id, @RequestParam String status) {	
		List<String> statusList = Arrays.stream(status.split(",")).map(String::trim).toList();
		List<Appointment> list = repo.getProfessionalRecords(id, statusList);
	    return ResponseEntity.ok(list);
	}
	
	@GetMapping("/list/client/{id}")
	public ResponseEntity<List<Appointment>> getAppointmentsByClient(@PathVariable Long id, @RequestParam String status){		
		List<String> statusList = Arrays.stream(status.split(",")).map(String::trim).toList();
		List<Appointment> list = repo.getActiveAppointmentsByClient(id,statusList);
		return ResponseEntity.ok(list);
	}
	
	@GetMapping("/list/client/records/{id}")
	public ResponseEntity<List<Appointment>> getClientRecords(@PathVariable Long id, @RequestParam String status) {	
		List<String> statusList = Arrays.stream(status.split(",")).map(String::trim).toList();
		List<Appointment> list = repo.getclientRecords(id, statusList);
	    return ResponseEntity.ok(list);
	}
	
	@PutMapping("/edit/{id}")
	@PreAuthorize("hasAnyAuthority('USER','PROFESSIONAL')")
	public ResponseEntity<?> editAppointment(@PathVariable Long id, @RequestBody AppointmentDTO dto){
		Appointment existing = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Appointment not found", 1001));
		dto.updateEntity(existing);
		Appointment updated = repo.save(existing);
		return ResponseEntity.ok(updated.getAppointmentId());
	}
}