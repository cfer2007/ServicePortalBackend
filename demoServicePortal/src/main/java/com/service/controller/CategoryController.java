package com.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.dto.CategoryDTO;
import com.service.exception.ResourceNotFoundException;
import com.service.model.Category;
import com.service.repository.CategoryRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
	private CategoryRepository repo;
	
	@PostMapping("/add")
	public ResponseEntity<Category> setCattegory(@Valid @RequestBody Category category) {
		Category p = repo.save(category);
		return ResponseEntity.ok(p);
	}
	
	@GetMapping("/all")
	@PreAuthorize("hasAuthority('ADMIN')")
	 public ResponseEntity<List<Category>> getAllCategories() {
		List<Category> list = repo.findAll();
		return ResponseEntity.ok(list);
	}
	
	@PutMapping("/edit/{id}")
	public ResponseEntity<Long> editCategory(@PathVariable Long id, @RequestBody CategoryDTO dto) {
		Category existing = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found",1001));
		dto.updateEntity(existing);
		Category updated = repo.save(existing);
		return ResponseEntity.ok(updated.getCategoryId());
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id) {
		repo.deleteById(id);			
		return ResponseEntity.ok("Category deleted");	
	}
}