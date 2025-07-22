import 'dart:convert';

class MessageResponse {
  final int? code;
  final String? message;
  final MessageData? data;

  MessageResponse({
    this.code,
    this.message,
    this.data,
  });

  factory MessageResponse.fromJson(Map<String, dynamic> json) {
    return MessageResponse(
      code: json['code'],
      message: json['message'],
      data: json['data'] != null ? MessageData.fromJson(json['data']) : null,
    );
  }
}

class MessageData {
  final List<Message>? content;
  final Pageable? pageable;
  final int? totalElements;
  final int? totalPages;
  final bool? last;
  final int? size;
  final int? number;
  final Sort? sort;
  final bool? first;
  final int? numberOfElements;
  final bool? empty;

  MessageData({
    this.content,
    this.pageable,
    this.totalElements,
    this.totalPages,
    this.last,
    this.size,
    this.number,
    this.sort,
    this.first,
    this.numberOfElements,
    this.empty,
  });

  factory MessageData.fromJson(Map<String, dynamic> json) {
    return MessageData(
      content: json['content'] != null
          ? List<Message>.from(
              json['content'].map((x) => Message.fromJson(x)),
            )
          : null,
      pageable: json['pageable'] != null
          ? Pageable.fromJson(json['pageable'])
          : null,
      totalElements: json['totalElements'],
      totalPages: json['totalPages'],
      last: json['last'],
      size: json['size'],
      number: json['number'],
      sort: json['sort'] != null ? Sort.fromJson(json['sort']) : null,
      first: json['first'],
      numberOfElements: json['numberOfElements'],
      empty: json['empty'],
    );
  }
}

class Pageable {
  final int? pageNumber;
  final int? pageSize;
  final Sort? sort;
  final int? offset;
  final bool? paged;
  final bool? unpaged;

  Pageable({
    this.pageNumber,
    this.pageSize,
    this.sort,
    this.offset,
    this.paged,
    this.unpaged,
  });

  factory Pageable.fromJson(Map<String, dynamic> json) {
    return Pageable(
      pageNumber: json['pageNumber'],
      pageSize: json['pageSize'],
      sort: json['sort'] != null ? Sort.fromJson(json['sort']) : null,
      offset: json['offset'],
      paged: json['paged'],
      unpaged: json['unpaged'],
    );
  }
}

class Sort {
  final bool? empty;
  final bool? sorted;
  final bool? unsorted;

  Sort({this.empty, this.sorted, this.unsorted});

  factory Sort.fromJson(Map<String, dynamic> json) {
    return Sort(
      empty: json['empty'],
      sorted: json['sorted'],
      unsorted: json['unsorted'],
    );
  }
}

class Message {
  final int? id;
  final int? senderId;
  final int? receiverId;
  final String? content;
  final String? email;
  final bool? isEmail;
  final bool? isRead;
  final bool? isDeleted;
  final String? createTime;
  final String? readTime;

  Message({
    this.id,
    this.senderId,
    this.receiverId,
    this.content,
    this.email,
    this.isEmail,
    this.isRead,
    this.isDeleted,
    this.createTime,
    this.readTime,
  });

  factory Message.fromJson(Map<String, dynamic> json) {
    return Message(
      id: json['id'],
      senderId: json['senderId'],
      receiverId: json['receiverId'],
      content: json['content'],
      email: json['email'],
      isEmail: json['isEmail'],
      isRead: json['isRead'],
      isDeleted: json['isDeleted'],
      createTime: json['createTime'],
      readTime: json['readTime'],
    );
  }
}
