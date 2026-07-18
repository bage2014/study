package com.bage.demo.service;

import com.bage.demo.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 消息管理服务接口
 */
public interface MessageService {

    /**
     * 创建消息
     *
     * @param message 消息实体
     * @return 创建后的消息
     */
    Message createMessage(Message message);

    /**
     * 根据ID查询消息
     *
     * @param id 消息ID
     * @return 消息Optional
     */
    Optional<Message> getMessageById(Long id);

    /**
     * 分页查询消息列表，支持按发送者、接收者、消息类型筛选
     *
     * @param sender      发送者（可选）
     * @param receiver    接收者（可选）
     * @param messageType 消息类型（可选）
     * @param pageable    分页参数
     * @return 分页消息列表
     */
    Page<Message> listMessages(String sender, String receiver, String messageType, Pageable pageable);

    /**
     * 更新消息内容
     *
     * @param id      消息ID
     * @param content 新内容
     * @return 更新后的消息
     */
    Message updateMessageContent(Long id, String content);

    /**
     * 更新消息状态
     *
     * @param id     消息ID
     * @param status 新状态
     * @return 更新后的消息
     */
    Message updateMessageStatus(Long id, String status);

    /**
     * 根据ID删除消息
     *
     * @param id 消息ID
     */
    void deleteMessage(Long id);

    /**
     * 批量删除消息
     *
     * @param ids 消息ID列表
     * @return 删除的消息数量
     */
    int deleteMessages(List<Long> ids);
}