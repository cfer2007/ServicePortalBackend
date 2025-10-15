package com.service.service;

import com.service.repository.ProfessionalRepository;
import com.service.model.Professional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProfessionalService {

    @Autowired
    private ProfessionalRepository professionalRepository;

    public List<Professional> searchProfessionals(Long categoryId, Long professionId, Long skillId, String keyword) {
        return professionalRepository.searchProfessionals(categoryId, professionId, skillId, keyword);
    }
}

