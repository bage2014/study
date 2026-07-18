package com.bage.demo.dto;

import com.bage.demo.dto.CreateMessageRequest.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueryParams {

    private String senderId;

    private String receiverId;

    private MessageType messageType;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 20;

    @Builder.Default
    private Sort.Direction sortDirection = Sort.Direction.DESC;

    @Builder.Default
    private String sortBy = "createdAt";

    public org.springframework.data.domain.Pageable toPageable() {
        Sort sort = Sort.by(sortDirection, sortBy);
        return org.springframework.data.domain.PageRequest.of(page, size, sort);
    }
}