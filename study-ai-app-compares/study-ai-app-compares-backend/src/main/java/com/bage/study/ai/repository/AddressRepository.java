package com.bage.study.ai.repository;

import com.bage.study.ai.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByIsDefaultTrue();

    List<Address> findByReceiverNameContaining(String receiverName);

    Optional<Address> findById(Long id);

    @Modifying
    @Query("UPDATE Address a SET a.isDefault = false WHERE a.isDefault = true")
    void clearAllDefault();
}