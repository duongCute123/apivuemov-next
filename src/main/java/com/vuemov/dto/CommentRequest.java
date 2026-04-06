package com.vuemov.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentRequest {
    @NotBlank(message = "Slug is required")
    private String slug;
    
    @NotBlank(message = "Content is required")
    private String content;
    
    private int rating;
}
