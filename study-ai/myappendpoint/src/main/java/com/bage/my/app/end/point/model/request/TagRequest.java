package com.bage.my.app.end.point.model.request;

import lombok.Data;
import java.util.List;

@Data
public class TagRequest {
    private List<String> tags;
}