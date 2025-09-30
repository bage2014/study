package com.bage.my.app.end.point.model.request;

import lombok.Data;
import java.util.List;

import com.bage.my.app.end.point.dto.PageQueryRequest;

@Data
public class TagRequest extends PageQueryRequest {
    private List<String> tags;
}