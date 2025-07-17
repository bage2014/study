package com.bage.my.app.end.point.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FamilyMemberTree {
    private Long id;
    private String name;
    private String avatar;
    private Integer generation;
    private String relationship;
    private List<FamilyMemberTree> children;
    
    public FamilyMemberTree(Long id, String name, String avatar, Integer generation, String relationship) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.generation = generation;
        this.relationship = relationship;
    }
    
    // 省略getter/setter
}