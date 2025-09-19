package com.service.dto.review;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReviewDecisionRequest {

    @NotBlank
    public String decision;   // "APPROVED" | "REQUIRES_CHANGES" | "REJECTED"

    public String notes;      // mensaje general opcional

    // requerido si decision == "REQUIRES_CHANGES"
    public List<@Valid Item> items;

    public static class Item {
        @NotNull
        public Long documentId;

        @NotBlank
        public String comment;

        public String docType; // opcional, útil para logs
    }
}