package com.bage.demo.service;

import com.bage.demo.dto.MessageDTO;
import com.bage.demo.entity.Message;
import com.bage.demo.repository.MessageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 消息服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    /**
     * 新增消息
     *
     * @param messageDTO 消息DTO
     * @return 新增成功的消息实体
     */
    @Override
    @Transactional
    public Message createMessage(MessageDTO messageDTO) {
        log.info("创建消息: senderId={}, receiverId={}", messageDTO.getSenderId(), messageDTO.getReceiverId());
        Message message = Message.builder()
                .senderId(messageDTO.getSenderId())
                .receiverId(messageDTO.getReceiverId())
                .content(messageDTO.getContent())
                .type(messageDTO.getType())
                .status(0) // 默认未读
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Message savedMessage = messageRepository.save(message);
        log.info("消息创建成功, id={}", savedMessage.getId());
        return savedMessage;
    }

    /**
     * 根据ID删除消息
     *
     * @param id 消息ID
     */
    @Override
    @Transactional
    public void deleteMessage(Long id) {
        log.info("删除消息, id={}", id);
        if (!messageRepository.existsById(id)) {
            log.warn("消息不存在, id={}", id);
            throw new EntityNotFoundException("消息不存在，id: " + id);
        }
        messageRepository.deleteById(id);
        log.info("消息删除成功, id={}", id);
    }

    /**
     * 更新消息
     *
     * @param id         消息ID
     * @param messageDTO 消息DTO
     * @return 更新后的消息实体
     */
    @Override
    @Transactional
    public Message updateMessage(Long id, MessageDTO messageDTO) {
        log.info("更新消息, id={}", id);
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("消息不存在, id={}", id);
                    return new EntityNotFoundException("消息不存在，id: " + id);
                });
        // 更新字段
        if (messageDTO.getContent() != null) {
            message.setContent(messageDTO.getContent());
        }
        if (messageDTO.getType() != null) {
            message.setType(messageDTO.getType());
        }
        if (messageDTO.getStatus() != null) {
            message.setStatus(messageDTO.getStatus());
        }
        message.setUpdatedAt(LocalDateTime.now());
        Message updatedMessage = messageRepository.save(message);
        log.info("消息更新成功, id={}", updatedMessage.getId());
        return updatedMessage;
    }

    /**
     * 根据ID查询消息
     *
     * @param id 消息ID
     * @return 消息实体Optional
     */
    @Override
    public Optional<Message> getMessageById(Long id) {
        log.debug("查询消息, id={}", id);
        return messageRepository.findById(id);
    }

    /**
     * 分页查询消息列表
     *
     * @param senderId   发送者ID（可选）
     * @param receiverId 接收者ID（可选）
     * @param pageable   分页和排序参数
     * @return 消息分页结果
     */
    @Override
    public Page<Message> listMessages(Long senderId, Long receiverId, Pageable pageable) {
        log.info("分页查询消息, senderId={}, receiverId={}, page={}, size={}",
                senderId, receiverId, pageable.getPageNumber(), pageable.getPageSize());
        if (senderId != null && receiverId != null) {
            return messageRepository.findBySenderIdAndReceiverId(senderId, receiverId, pageable);
        } else if (senderId != null) {
            return messageRepository.findBySenderId(senderId, pageable);
        } else if (receiverId != null) {
            return messageRepository.findByReceiverId(receiverId, pageable);
        } else {
            return messageRepository.findAll(pageable);
        }
    }
}