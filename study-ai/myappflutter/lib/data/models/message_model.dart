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
  final int? id;
  final int? senderId;
  final int? receiverId;
  final String? content;
  final bool? isRead;
  final String? createTime;
  final String? readTime;

  Message({
    this.id,
    this.senderId,
    this.receiverId,
    this.content,
    this.isRead,
    this.createTime,
    this.readTime,
  });

  factory Message.fromJson(Map<String, dynamic> json) {
    return Message(
      id: json['id'],
      senderId: json['senderId'],
      receiverId: json['receiverId'],
      content: json['content'],
      isRead: json['isRead'],
      createTime: json['createTime'],
      readTime: json['readTime'],
    );
  }
}
