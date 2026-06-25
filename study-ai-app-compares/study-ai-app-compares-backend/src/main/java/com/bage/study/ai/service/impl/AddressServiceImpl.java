package com.bage.study.ai.service.impl;

import com.bage.study.ai.dto.request.AddressRequest;
import com.bage.study.ai.dto.response.AddressResponse;
import com.bage.study.ai.entity.Address;
import com.bage.study.ai.exception.BusinessException;
import com.bage.study.ai.repository.AddressRepository;
import com.bage.study.ai.service.AddressService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @PostConstruct
    public void initDefaultAddresses() {
        if (addressRepository.count() == 0) {
            log.info("初始化默认地址数据");
            Address defaultAddr = Address.builder()
                    .receiverName("张三")
                    .phoneNumber("13800138001")
                    .provinceCityDistrict("北京市朝阳区")
                    .detailAddress("望京SOHO T1 1201室")
                    .isDefault(true)
                    .build();
            addressRepository.save(defaultAddr);

            Address addr2 = Address.builder()
                    .receiverName("李四")
                    .phoneNumber("13900139002")
                    .provinceCityDistrict("上海市浦东新区")
                    .detailAddress("陆家嘴金融中心88层")
                    .isDefault(false)
                    .build();
            addressRepository.save(addr2);
        }
    }

    @Override
    public List<AddressResponse> listAddresses() {
        return addressRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AddressResponse getAddressById(String id) {
        Address address = addressRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new BusinessException("ADDRESS_NOT_FOUND", "地址不存在: " + id));
        return toResponse(address);
    }

    @Override
    @Transactional
    public AddressResponse createAddress(AddressRequest request) {
        if (Boolean.TRUE.equals(request.getIsDefault())) {
            addressRepository.clearAllDefault();
        }

        Address address = Address.builder()
                .receiverName(request.getReceiverName())
                .phoneNumber(request.getPhoneNumber())
                .provinceCityDistrict(request.getProvinceCityDistrict())
                .detailAddress(request.getDetailAddress())
                .isDefault(request.getIsDefault() != null ? request.getIsDefault() : false)
                .build();

        Address saved = addressRepository.save(address);
        log.info("创建地址成功: {}", saved.getId());
        return toResponse(saved);
    }

    @Override
    @Transactional
    public AddressResponse updateAddress(String id, AddressRequest request) {
        Address existing = addressRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new BusinessException("ADDRESS_NOT_FOUND", "地址不存在: " + id));

        if (Boolean.TRUE.equals(request.getIsDefault())) {
            addressRepository.clearAllDefault();
        }

        existing.setReceiverName(request.getReceiverName());
        existing.setPhoneNumber(request.getPhoneNumber());
        existing.setProvinceCityDistrict(request.getProvinceCityDistrict());
        existing.setDetailAddress(request.getDetailAddress());
        existing.setIsDefault(request.getIsDefault() != null ? request.getIsDefault() : existing.getIsDefault());

        Address saved = addressRepository.save(existing);
        log.info("更新地址成功: {}", saved.getId());
        return toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteAddress(String id) {
        if (!addressRepository.existsById(Long.parseLong(id))) {
            throw new BusinessException("ADDRESS_NOT_FOUND", "地址不存在: " + id);
        }
        addressRepository.deleteById(Long.parseLong(id));
        log.info("删除地址成功: {}", id);
    }

    private AddressResponse toResponse(Address address) {
        return AddressResponse.builder()
                .id(String.valueOf(address.getId()))
                .receiverName(address.getReceiverName())
                .phoneNumber(address.getPhoneNumber())
                .provinceCityDistrict(address.getProvinceCityDistrict())
                .detailAddress(address.getDetailAddress())
                .isDefault(address.getIsDefault())
                .build();
    }
}