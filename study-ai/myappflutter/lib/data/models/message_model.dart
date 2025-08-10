import 'dart:convert';

class MessageResponse {
  final int? code;
  final String? message;
  final List<Message>? data;
  final int? total;
  final int? page;
  final int? size;

  MessageResponse({
    this.code,
    this.message,
    this.data,
    this.total,
    this.page,
    this.size,
  });

  factory MessageResponse.fromJson(Map<String, dynamic> json) {
    return MessageResponse(
      code: json['code'],
      message: json['message'],
      data: json['data'] != null
          ? List<Message>.from(
              json['data'].map((x) => Message.fromJson(x)),
            )
          : null,
      total: json['total'],
      page: json['page'],
      size: json['size'],
    );
  }
}

class Message {
  final String? id;
  final String? senderId;
  final String? senderAvatar;  // 添加头像URL字段
  final String? content;
  final String? createTime;
  final String? readTime;

  Message({
    this.id,
    this.senderId,
    this.senderAvatar,  // 添加到构造函数
    this.content,
    this.createTime,
    this.readTime,
  });

  factory Message.fromJson(Map<String, dynamic> json) {
    return Message(
      id: json['id'],
      senderId: json['senderId'],
      senderAvatar: json['senderAvatar'],  // 从JSON解析头像URL
      content: json['content'],
      createTime: json['createTime'],
      readTime: json['readTime'],
    );
  }
}
