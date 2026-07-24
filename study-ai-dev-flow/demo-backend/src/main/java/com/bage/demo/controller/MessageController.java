package com.bage.demo.controller;

import com.bage.demo.dto.MessageCreateRequest;
import com.bage.demo.dto.MessageResponse;
import com.bage.demo.dto.MessageUpdateRequest;
import com.bage.demo.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    /**
     * 创建消息
     *
     * @param request 消息创建请求体
     * @return 创建成功的消息响应
     */
    @PostMapping
    public ResponseEntity<MessageResponse> createMessage(@Valid @RequestBody MessageCreateRequest request) {
        MessageResponse response = messageService.createMessage(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 根据ID查询单条消息
     *
     * @param id 消息ID
     * @return 消息响应
     */
    @GetMapping("/{id}")
    public ResponseEntity<MessageResponse> getMessageById(@PathVariable Long id) {
        MessageResponse response = messageService.getMessageById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * 条件查询消息列表（分页，按时间倒序）
     *
     * @param sender    发送者（可选）
     * @param receiver  接收者（可选）
     * @param startTime 开始时间（可选）
     * @param endTime   结束时间（可选）
     * @param pageable  分页参数
     * @return 消息分页结果
     */
    @GetMapping
    public ResponseEntity<Page<MessageResponse>> listMessages(
            @RequestParam(required = false) String sender,
            @RequestParam(required = false) String receiver,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @PageableDefault(sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
        Page<MessageResponse> responses = messageService.listMessages(sender, receiver, startTime, endTime, pageable);
        return ResponseEntity.ok(responses);
    }

    /**
     * 更新消息
     *
     * @param id      消息ID
     * @param request 消息更新请求体
     * @return 更新后的消息响应
     */
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateMessage(@PathVariable Long id, @Valid @RequestBody MessageUpdateRequest request) {
        MessageResponse response = messageService.updateMessage(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 删除单条消息
     *
     * @param id 消息ID
     * @return 无内容响应
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 批量删除消息
     *
     * @param ids 消息ID列表
     * @return 无内容响应
     */
    @DeleteMapping("/batch")
    public ResponseEntity<Void> deleteMessages(@RequestBody List<Long> ids) {
        messageService.deleteMessages(ids);
        return ResponseEntity.noContent().build();
    }
}