package com.bage.demo.service.impl;

import com.bage.demo.entity.Message;
import com.bage.demo.repository.MessageRepository;
import com.bage.demo.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 消息管理服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    @Transactional
    public Message createMessage(Message message) {
        log.info("创建消息: {}", message);
        message.setId(null); // 确保是新增
        message.setCreateTime(LocalDateTime.now());
        return messageRepository.save(message);
    }

    @Override
    public Optional<Message> getMessageById(Long id) {
        log.debug("根据ID查询消息: {}", id);
        return messageRepository.findById(id);
    }

    @Override
    public Page<Message> listMessages(String sender, String receiver, String messageType, Pageable pageable) {
        log.debug("分页查询消息列表, sender={}, receiver={}, messageType={}, pageable={}",
                sender, receiver, messageType, pageable);

        Specification<Message> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(sender)) {
                predicates.add(criteriaBuilder.equal(root.get("sender"), sender));
            }
            if (StringUtils.hasText(receiver)) {
                predicates.add(criteriaBuilder.equal(root.get("receiver"), receiver));
            }
            if (StringUtils.hasText(messageType)) {
                predicates.add(criteriaBuilder.equal(root.get("messageType"), messageType));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return messageRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional
    public Message updateMessageContent(Long id, String content) {
        log.info("更新消息内容, id={}", id);
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("消息不存在，id: " + id));
        message.setContent(content);
        return messageRepository.save(message);
    }

    @Override
    @Transactional
    public Message updateMessageStatus(Long id, String status) {
        log.info("更新消息状态, id={}, status={}", id, status);
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("消息不存在，id: " + id));
        message.setStatus(status);
        return messageRepository.save(message);
    }

    @Override
    @Transactional
    public void deleteMessage(Long id) {
        log.info("删除消息, id={}", id);
        if (!messageRepository.existsById(id)) {
            throw new RuntimeException("消息不存在，id: " + id);
        }
        messageRepository.deleteById(id);
    }

    @Override
    @Transactional
    public int deleteMessages(List<Long> ids) {
        log.info("批量删除消息, ids={}", ids);
        List<Message> messages = messageRepository.findAllById(ids);
        if (messages.isEmpty()) {
            return 0;
        }
        messageRepository.deleteAll(messages);
        return messages.size();
    }
}