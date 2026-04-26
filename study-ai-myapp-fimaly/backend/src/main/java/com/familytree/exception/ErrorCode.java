package com.familytree.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 通用错误 (G000 - G999)
    INTERNAL_SERVER_ERROR("G001", "服务器内部错误"),
    INVALID_PARAMETER("G002", "参数无效"),
    RESOURCE_NOT_FOUND("G003", "资源不存在"),
    OPERATION_FAILED("G004", "操作失败"),
    UNAUTHORIZED("G005", "未授权"),
    FORBIDDEN("G006", "禁止访问"),

    // 认证相关 (A000 - A999)
    AUTHENTICATION_FAILED("A001", "认证失败"),
    INVALID_CREDENTIALS("A002", "用户名或密码错误"),
    TOKEN_EXPIRED("A003", "令牌已过期"),
    TOKEN_INVALID("A004", "令牌无效"),
    TOKEN_MISSING("A005", "令牌缺失"),

    // 用户相关 (U000 - U999)
    USER_NOT_FOUND("U001", "用户不存在"),
    USER_ALREADY_EXISTS("U002", "用户已存在"),
    EMAIL_ALREADY_EXISTS("U003", "邮箱已被注册"),
    PHONE_ALREADY_EXISTS("U004", "手机号已被注册"),
    USERNAME_ALREADY_EXISTS("U005", "用户名已被使用"),
    PASSWORD_INVALID("U006", "密码格式不正确"),
    PASSWORD_MISMATCH("U007", "两次密码输入不一致"),

    // 家族相关 (F000 - F999)
    FAMILY_NOT_FOUND("F001", "家族不存在"),
    FAMILY_ALREADY_EXISTS("F002", "家族已存在"),
    FAMILY_NAME_REQUIRED("F003", "家族名称不能为空"),
    FAMILY_NAME_TOO_LONG("F004", "家族名称过长"),
    FAMILY_DESCRIPTION_TOO_LONG("F005", "家族描述过长"),
    FAMILY_CREATION_FAILED("F006", "创建家族失败"),
    FAMILY_UPDATE_FAILED("F007", "更新家族失败"),
    FAMILY_DELETE_FAILED("F008", "删除家族失败"),

    // 成员相关 (M000 - M999)
    MEMBER_NOT_FOUND("M001", "成员不存在"),
    MEMBER_ALREADY_EXISTS("M002", "成员已存在"),
    MEMBER_NAME_REQUIRED("M003", "成员姓名不能为空"),
    MEMBER_NAME_TOO_LONG("M004", "成员姓名过长"),
    MEMBER_BIRTH_DATE_INVALID("M005", "出生日期格式不正确"),
    MEMBER_DEATH_DATE_INVALID("M006", "去世日期格式不正确"),
    MEMBER_BIRTH_DATE_AFTER_DEATH("M007", "出生日期不能晚于去世日期"),
    MEMBER_GENDER_INVALID("M008", "性别格式不正确"),
    MEMBER_CREATION_FAILED("M009", "创建成员失败"),
    MEMBER_UPDATE_FAILED("M010", "更新成员失败"),
    MEMBER_DELETE_FAILED("M011", "删除成员失败"),

    // 关系相关 (R000 - R999)
    RELATIONSHIP_NOT_FOUND("R001", "关系不存在"),
    RELATIONSHIP_ALREADY_EXISTS("R002", "关系已存在"),
    RELATIONSHIP_TYPE_INVALID("R003", "关系类型不正确"),
    RELATIONSHIP_SELF_REFERENCE("R004", "不能与自身建立关系"),
    RELATIONSHIP_CREATION_FAILED("R005", "创建关系失败"),
    RELATIONSHIP_UPDATE_FAILED("R006", "更新关系失败"),
    RELATIONSHIP_DELETE_FAILED("R007", "删除关系失败"),

    // 权限相关 (P000 - P999)
    PERMISSION_DENIED("P001", "权限不足"),
    NOT_FAMILY_ADMINISTRATOR("P002", "只有家族管理员可以执行此操作"),
    NOT_FAMILY_MEMBER("P003", "不是家族成员"),
    ALREADY_FAMILY_MEMBER("P004", "已经是家族成员"),
    CANNOT_REMOVE_LAST_ADMINISTRATOR("P005", "不能移除最后一个管理员"),
    CANNOT_LEAVE_FAMILY("P006", "管理员不能离开家族"),

    // 事件相关 (E000 - E999)
    EVENT_NOT_FOUND("E001", "事件不存在"),
    EVENT_TITLE_REQUIRED("E002", "事件标题不能为空"),
    EVENT_TITLE_TOO_LONG("E003", "事件标题过长"),
    EVENT_DATE_INVALID("E004", "事件日期格式不正确"),
    EVENT_DESCRIPTION_TOO_LONG("E005", "事件描述过长"),
    EVENT_CREATION_FAILED("E006", "创建事件失败"),
    EVENT_UPDATE_FAILED("E007", "更新事件失败"),
    EVENT_DELETE_FAILED("E008", "删除事件失败"),

    // 媒体相关 (D000 - D999)
    MEDIA_NOT_FOUND("D001", "媒体不存在"),
    MEDIA_TYPE_INVALID("D002", "媒体类型不正确"),
    MEDIA_UPLOAD_FAILED("D003", "媒体上传失败"),
    MEDIA_DELETE_FAILED("D004", "媒体删除失败"),
    MEDIA_URL_REQUIRED("D005", "媒体URL不能为空"),

    // 位置相关 (L000 - L999)
    LOCATION_NOT_FOUND("L001", "位置不存在"),
    LOCATION_NAME_REQUIRED("L002", "位置名称不能为空"),
    LOCATION_NAME_TOO_LONG("L003", "位置名称过长"),
    LOCATION_CREATION_FAILED("L004", "创建位置失败"),
    LOCATION_UPDATE_FAILED("L005", "更新位置失败"),
    LOCATION_DELETE_FAILED("L006", "删除位置失败"),

    // AI服务相关 (AI00 - AI99)
    AI_SERVICE_UNAVAILABLE("AI01", "AI服务不可用"),
    AI_SERVICE_TIMEOUT("AI02", "AI服务超时"),
    AI_SERVICE_ERROR("AI03", "AI服务错误"),
    AI_MODEL_NOT_FOUND("AI04", "AI模型不存在"),
    AI_PREDICTION_FAILED("AI05", "AI预测失败"),
    AI_STORY_GENERATION_FAILED("AI06", "AI故事生成失败"),
    AI_FACE_RECOGNITION_FAILED("AI07", "AI人脸识别失败"),
    AI_RATE_LIMIT_EXCEEDED("AI08", "AI请求频率超限");

    private final String code;
    private final String message;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getMessage(Object... args) {
        if (args == null || args.length == 0) {
            return message;
        }
        return String.format(message, args);
    }
}