package com.bage.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageCreateRequest {

    @NotBlank(message = "Title must not be blank")
    private String title;

    @NotBlank(message = "Content must not be blank")
    private String content;

    private String author;
}
