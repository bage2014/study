import 'AppFeedback.dart';
import 'Pagination.dart';

class FeedbackQueryResult {
  int? code;
  int? originCode;
  String? msg;
  String? originMsg;
  List<AppFeedback>? data;
  Pagination? pagination;

  FeedbackQueryResult(
      {this.code,
        this.originCode,
        this.msg,
        this.originMsg,
        this.data,
        this.pagination});

  FeedbackQueryResult.fromJson(Map<String, dynamic> json) {
    code = json['code'];
    originCode = json['originCode'];
    msg = json['msg'];
    originMsg = json['originMsg'];
    if (json['data'] != null) {
      data = [];
      json['data'].forEach((v) {
        data?.add(new AppFeedback.fromJson(v));
      });
    }
    pagination = json['pagination'] != null
        ? new Pagination.fromJson(json['pagination'])
        : null;
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['code'] = this.code;
    data['originCode'] = this.originCode;
    data['msg'] = this.msg;
    data['originMsg'] = this.originMsg;
    if (this.data != null) {
      data['data'] = this.data?.map((v) => v.toJson()).toList();
    }
    if (this.pagination != null) {
      data['pagination'] = this.pagination?.toJson();
    }
    return data;
  }
}


