package com.bage.demo.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessagePageRequest {

    @Min(value = 1, message = "页码最小值为1")
    private Integer page = 1;

    @Min(value = 1, message = "每页条数最小值为1")
    private Integer size = 10;

    private Long id;

    private String sender;

    private String receiver;

    private String subject;

    private Boolean read;

    private String sortBy = "sentAt";

    private String sortDirection = "desc";
}
