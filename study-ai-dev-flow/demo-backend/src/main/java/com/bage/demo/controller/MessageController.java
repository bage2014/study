package com.bage.demo.controller;

import com.bage.demo.dto.MessageCreateRequest;
import com.bage.demo.dto.MessageUpdateRequest;
import com.bage.demo.dto.MessageStatusUpdateRequest;
import com.bage.demo.entity.Message;
import com.bage.demo.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 消息管理控制器，提供消息的增删改查REST API
 */
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "消息管理", description = "消息的增删改查操作")
public class MessageController {

    private final MessageService messageService;

    /**
     * 创建新消息
     *
     * @param request 消息创建请求体
     * @return 创建后的消息对象
     */
    @PostMapping
    @Operation(summary = "创建消息", description = "创建一条新消息，返回创建后的消息对象")
    public ResponseEntity<Message> createMessage(@Valid @RequestBody MessageCreateRequest request) {
        Message message = messageService.createMessage(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    /**
     * 根据ID查询消息
     *
     * @param id 消息ID
     * @return 消息对象，不存在则返回404
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询消息", description = "根据消息ID获取单条消息")
    public ResponseEntity<Message> getMessageById(@PathVariable Long id) {
        return messageService.getMessageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 分页查询消息列表，支持按发送者、接收者、消息类型筛选
     *
     * @param page        页码（从0开始）
     * @param size        每页条数
     * @param sender      发送者（可选）
     * @param receiver    接收者（可选）
     * @param messageType 消息类型（可选）
     * @return 分页结果
     */
    @GetMapping
    @Operation(summary = "分页查询消息列表", description = "支持按发送者、接收者、消息类型筛选")
    public ResponseEntity<Page<Message>> getMessages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sender,
            @RequestParam(required = false) String receiver,
            @RequestParam(required = false) String messageType) {
        Page<Message> messages = messageService.getMessages(page, size, sender, receiver, messageType);
        return ResponseEntity.ok(messages);
    }

    /**
     * 更新消息内容
     *
     * @param id      消息ID
     * @param request 消息更新请求体
     * @return 更新后的消息对象
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新消息内容", description = "根据ID更新消息内容，返回更新后的消息对象")
    public ResponseEntity<Message> updateMessage(@PathVariable Long id, @Valid @RequestBody MessageUpdateRequest request) {
        Message message = messageService.updateMessage(id, request);
        return ResponseEntity.ok(message);
    }

    /**
     * 更新消息状态（如已读/未读）
     *
     * @param id      消息ID
     * @param request 状态更新请求体
     * @return 更新后的消息对象
     */
    @PatchMapping("/{id}/status")
    @Operation(summary = "更新消息状态", description = "根据ID更新消息状态（如已读、未读），返回更新后的消息对象")
    public ResponseEntity<Message> updateMessageStatus(@PathVariable Long id, @Valid @RequestBody MessageStatusUpdateRequest request) {
        Message message = messageService.updateMessageStatus(id, request);
        return ResponseEntity.ok(message);
    }

    /**
     * 根据ID删除消息
     *
     * @param id 消息ID
     * @return 成功提示
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除消息", description = "根据ID删除单条消息")
    public ResponseEntity<Map<String, Object>> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.ok(Map.of("message", "消息删除成功", "id", id));
    }

    /**
     * 批量删除消息
     *
     * @param ids 消息ID列表
     * @return 被删除的数量
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除消息", description = "批量删除多条消息，返回被删除的数量")
    public ResponseEntity<Map<String, Object>> deleteMessages(@RequestBody List<Long> ids) {
        int deletedCount = messageService.deleteMessages(ids);
        return ResponseEntity.ok(Map.of("message", "批量删除成功", "deletedCount", deletedCount));
    }
}