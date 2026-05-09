package com.familytree.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvatarUploadResponse {

    private boolean success;
    private String message;
    private String avatarUrl;

    public static AvatarUploadResponse success(String avatarUrl) {
        return new AvatarUploadResponse(true, "头像上传成功", avatarUrl);
    }

    public static AvatarUploadResponse error(String message) {
        return new AvatarUploadResponse(false, message, null);
    }
}