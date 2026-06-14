package com.bage.study.ai.service;

import com.bage.study.ai.dto.request.AddressRequest;
import com.bage.study.ai.dto.response.AddressResponse;

import java.util.List;

public interface AddressService {

    List<AddressResponse> listAddresses();

    AddressResponse getAddressById(String id);

    AddressResponse createAddress(AddressRequest request);

    AddressResponse updateAddress(String id, AddressRequest request);

    void deleteAddress(String id);
}