package com.bage.demo.repository;

import com.bage.demo.entity.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MessageRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MessageRepository messageRepository;

    @Test
    void save_ShouldPersistMessage() {
        Message message = new Message();
        message.setContent("Hello");
        message.setSender("Alice");

        Message saved = messageRepository.save(message);

        assertNotNull(saved.getId());
        assertEquals("Hello", saved.getContent());
        assertEquals("Alice", saved.getSender());
    }

    @Test
    void findById_ShouldReturnMessageWhenExists() {
        Message message = new Message();
        message.setContent("Test");
        message.setSender("Bob");
        Message persisted = entityManager.persistFlushFind(message);

        Optional<Message> found = messageRepository.findById(persisted.getId());

        assertTrue(found.isPresent());
        assertEquals("Test", found.get().getContent());
        assertEquals("Bob", found.get().getSender());
    }

    @Test
    void findById_ShouldReturnEmptyWhenNotExists() {
        Optional<Message> found = messageRepository.findById(999L);

        assertFalse(found.isPresent());
    }

    @Test
    void findAll_ShouldReturnAllMessages() {
        Message msg1 = new Message();
        msg1.setContent("First");
        msg1.setSender("Alice");
        entityManager.persist(msg1);

        Message msg2 = new Message();
        msg2.setContent("Second");
        msg2.setSender("Bob");
        entityManager.persist(msg2);

        List<Message> messages = messageRepository.findAll();

        assertEquals(2, messages.size());
    }

    @Test
    void findAll_ShouldReturnEmptyListWhenNoMessages() {
        List<Message> messages = messageRepository.findAll();

        assertTrue(messages.isEmpty());
    }

    @Test
    void deleteById_ShouldRemoveMessage() {
        Message message = new Message();
        message.setContent("To be deleted");
        message.setSender("Charlie");
        Message persisted = entityManager.persistFlushFind(message);

        messageRepository.deleteById(persisted.getId());

        Optional<Message> deleted = messageRepository.findById(persisted.getId());
        assertFalse(deleted.isPresent());
    }

    @Test
    void existsById_ShouldReturnTrueWhenExists() {
        Message message = new Message();
        message.setContent("Exists");
        message.setSender("Dave");
        Message persisted = entityManager.persistFlushFind(message);

        boolean exists = messageRepository.existsById(persisted.getId());

        assertTrue(exists);
    }

    @Test
    void existsById_ShouldReturnFalseWhenNotExists() {
        boolean exists = messageRepository.existsById(999L);

        assertFalse(exists);
    }
}