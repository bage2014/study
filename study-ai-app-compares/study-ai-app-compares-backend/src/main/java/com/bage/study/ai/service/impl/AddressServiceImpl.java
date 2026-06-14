package com.bage.study.ai.service.impl;

import com.bage.study.ai.dto.request.AddressRequest;
import com.bage.study.ai.dto.response.AddressResponse;
import com.bage.study.ai.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

@Slf4j
@Service
public class AddressServiceImpl implements AddressService {

    private final Map<String, AddressResponse> addressStorage = new ConcurrentHashMap<>();

    public AddressServiceImpl() {
        initDefaultAddresses();
    }

    private void initDefaultAddresses() {
        AddressResponse defaultAddr = AddressResponse.builder()
                .id("1")
                .receiverName("张三")
                .phoneNumber("13800138001")
                .provinceCityDistrict("北京市朝阳区")
                .detailAddress("望京SOHO T1 1201室")
                .isDefault(true)
                .build();
        addressStorage.put("1", defaultAddr);

        AddressResponse addr2 = AddressResponse.builder()
                .id("2")
                .receiverName("李四")
                .phoneNumber("13900139002")
                .provinceCityDistrict("上海市浦东新区")
                .detailAddress("陆家嘴金融中心88层")
                .isDefault(false)
                .build();
        addressStorage.put("2", addr2);
    }

    @Override
    public List<AddressResponse> listAddresses() {
        return new ArrayList<>(addressStorage.values());
    }

    @Override
    public AddressResponse getAddressById(String id) {
        AddressResponse address = addressStorage.get(id);
        if (address == null) {
            throw new RuntimeException("地址不存在: " + id);
        }
        return address;
    }

    @Override
    public AddressResponse createAddress(AddressRequest request) {
        String id = UUID.randomUUID().toString();
        
        if (Boolean.TRUE.equals(request.getIsDefault())) {
            addressStorage.values().forEach(addr -> addr.setIsDefault(false));
        }

        AddressResponse response = AddressResponse.builder()
                .id(id)
                .receiverName(request.getReceiverName())
                .phoneNumber(request.getPhoneNumber())
                .provinceCityDistrict(request.getProvinceCityDistrict())
                .detailAddress(request.getDetailAddress())
                .isDefault(request.getIsDefault() != null ? request.getIsDefault() : false)
                .build();

        addressStorage.put(id, response);
        log.info("创建地址成功: {}", id);
        return response;
    }

    @Override
    public AddressResponse updateAddress(String id, AddressRequest request) {
        AddressResponse existing = getAddressById(id);

        if (Boolean.TRUE.equals(request.getIsDefault())) {
            addressStorage.values().forEach(addr -> addr.setIsDefault(false));
        }

        AddressResponse updated = AddressResponse.builder()
                .id(id)
                .receiverName(request.getReceiverName())
                .phoneNumber(request.getPhoneNumber())
                .provinceCityDistrict(request.getProvinceCityDistrict())
                .detailAddress(request.getDetailAddress())
                .isDefault(request.getIsDefault() != null ? request.getIsDefault() : existing.getIsDefault())
                .build();

        addressStorage.put(id, updated);
        log.info("更新地址成功: {}", id);
        return updated;
    }

    @Override
    public void deleteAddress(String id) {
        if (!addressStorage.containsKey(id)) {
            throw new RuntimeException("地址不存在: " + id);
        }
        addressStorage.remove(id);
        log.info("删除地址成功: {}", id);
    }
}