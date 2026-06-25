package com.bage.study.ai.service;

import com.bage.study.ai.dto.request.AddressRequest;
import com.bage.study.ai.dto.response.AddressResponse;
import com.bage.study.ai.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AddressServiceTest {

    @Autowired
    private AddressService addressService;

    @Test
    @DisplayName("测试获取地址列表")
    void testListAddresses() {
        List<AddressResponse> addresses = addressService.listAddresses();
        assertNotNull(addresses);
        assertTrue(addresses.size() >= 2);
    }

    @Test
    @DisplayName("测试获取单个地址")
    void testGetAddressById() {
        List<AddressResponse> addresses = addressService.listAddresses();
        String id = addresses.get(0).getId();
        
        AddressResponse address = addressService.getAddressById(id);
        assertNotNull(address);
        assertEquals(id, address.getId());
    }

    @Test
    @DisplayName("测试获取不存在的地址")
    void testGetAddressByIdNotFound() {
        assertThrows(BusinessException.class, () -> {
            addressService.getAddressById("99999");
        });
    }

    @Test
    @DisplayName("测试创建地址")
    void testCreateAddress() {
        AddressRequest request = AddressRequest.builder()
                .receiverName("王五")
                .phoneNumber("13700137003")
                .provinceCityDistrict("广东省深圳市")
                .detailAddress("科技园南区18栋")
                .isDefault(false)
                .build();

        AddressResponse response = addressService.createAddress(request);
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("王五", response.getReceiverName());
    }

    @Test
    @DisplayName("测试创建默认地址")
    void testCreateDefaultAddress() {
        AddressRequest request = AddressRequest.builder()
                .receiverName("赵六")
                .phoneNumber("13600136004")
                .provinceCityDistrict("浙江省杭州市")
                .detailAddress("西湖区文三路123号")
                .isDefault(true)
                .build();

        AddressResponse response = addressService.createAddress(request);
        assertTrue(response.getIsDefault());

        List<AddressResponse> addresses = addressService.listAddresses();
        long defaultCount = addresses.stream().filter(AddressResponse::getIsDefault).count();
        assertEquals(1, defaultCount);
    }

    @Test
    @DisplayName("测试更新地址")
    void testUpdateAddress() {
        List<AddressResponse> addresses = addressService.listAddresses();
        String id = addresses.get(0).getId();

        AddressRequest request = AddressRequest.builder()
                .receiverName("张三更新")
                .phoneNumber("13800138001")
                .provinceCityDistrict("北京市海淀区")
                .detailAddress("中关村大街1号")
                .isDefault(false)
                .build();

        AddressResponse response = addressService.updateAddress(id, request);
        assertEquals("张三更新", response.getReceiverName());
        assertEquals("北京市海淀区", response.getProvinceCityDistrict());
    }

    @Test
    @DisplayName("测试删除地址")
    void testDeleteAddress() {
        AddressRequest request = AddressRequest.builder()
                .receiverName("测试用户")
                .phoneNumber("13500135000")
                .provinceCityDistrict("测试省测试市")
                .detailAddress("测试地址")
                .isDefault(false)
                .build();

        AddressResponse created = addressService.createAddress(request);
        String id = created.getId();

        addressService.deleteAddress(id);
        
        assertThrows(BusinessException.class, () -> {
            addressService.getAddressById(id);
        });
    }
}