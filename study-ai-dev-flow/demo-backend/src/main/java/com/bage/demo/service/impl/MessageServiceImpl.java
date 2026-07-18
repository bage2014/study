package com.bage.demo.service.impl;

import com.bage.demo.dto.MessageDTO;
import com.bage.demo.dto.MessageQueryDTO;
import com.bage.demo.dto.PageResult;
import com.bage.demo.entity.Message;
import com.bage.demo.exception.BusinessException;
import com.bage.demo.repository.MessageRepository;
import com.bage.demo.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 消息管理服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message createMessage(MessageDTO messageDTO) {
        log.info("新增消息: {}", messageDTO);
        Message message = Message.builder()
                .senderId(messageDTO.getSenderId())
                .receiverId(messageDTO.getReceiverId())
                .content(messageDTO.getContent())
                .type(messageDTO.getType())
                .status(0) // 默认未读
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        Message saved = messageRepository.save(message);
        log.info("消息新增成功, ID: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMessage(Long id) {
        log.info("删除消息, ID: {}", id);
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new BusinessException("消息不存在, ID: " + id));
        messageRepository.delete(message);
        log.info("消息删除成功, ID: {}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message updateMessage(Long id, MessageDTO messageDTO) {
        log.info("更新消息, ID: {}, 数据: {}", id, messageDTO);
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new BusinessException("消息不存在, ID: " + id));
        message.setSenderId(messageDTO.getSenderId());
        message.setReceiverId(messageDTO.getReceiverId());
        message.setContent(messageDTO.getContent());
        message.setType(messageDTO.getType());
        message.setStatus(messageDTO.getStatus());
        message.setUpdateTime(LocalDateTime.now());
        Message updated = messageRepository.save(message);
        log.info("消息更新成功, ID: {}", updated.getId());
        return updated;
    }

    @Override
    public Message getMessageById(Long id) {
        log.info("查询消息, ID: {}", id);
        return messageRepository.findById(id)
                .orElseThrow(() -> new BusinessException("消息不存在, ID: " + id));
    }

    @Override
    public PageResult<Message> listMessages(MessageQueryDTO queryDTO) {
        log.info("分页查询消息, 条件: {}", queryDTO);
        // 构建排序
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        if (StringUtils.hasText(queryDTO.getSortField())) {
            Sort.Direction direction = "asc".equalsIgnoreCase(queryDTO.getSortDirection())
                    ? Sort.Direction.ASC : Sort.Direction.DESC;
            sort = Sort.by(direction, queryDTO.getSortField());
        }
        // 构建分页
        Pageable pageable = PageRequest.of(queryDTO.getPage() - 1, queryDTO.getSize(), sort);
        // 构建查询条件
        Specification<Message> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (queryDTO.getId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("id"), queryDTO.getId()));
            }
            if (StringUtils.hasText(queryDTO.getSenderId())) {
                predicates.add(criteriaBuilder.like(root.get("senderId"), "%" + queryDTO.getSenderId() + "%"));
            }
            if (StringUtils.hasText(queryDTO.getReceiverId())) {
                predicates.add(criteriaBuilder.like(root.get("receiverId"), "%" + queryDTO.getReceiverId() + "%"));
            }
            if (queryDTO.getType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), queryDTO.getType()));
            }
            if (queryDTO.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), queryDTO.getStatus()));
            }
            if (queryDTO.getStartTime() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createTime"), queryDTO.getStartTime()));
            }
            if (queryDTO.getEndTime() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createTime"), queryDTO.getEndTime()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        // 执行查询
        Page<Message> page = messageRepository.findAll(specification, pageable);
        // 返回分页结果
        return PageResult.<Message>builder()
                .records(page.getContent())
                .total(page.getTotalElements())
                .page(queryDTO.getPage())
                .size(queryDTO.getSize())
                .build();
    }
}