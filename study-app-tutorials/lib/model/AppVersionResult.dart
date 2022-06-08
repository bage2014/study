import 'AppVersion.dart';

class AppVersionResult {
  int? code;
  int? originCode;
  String? msg;
  Null? originMsg;
  AppVersion? data;

  AppVersionResult(
      {this.code, this.originCode, this.msg, this.originMsg, this.data});

  AppVersionResult.fromJson(Map<String, dynamic> json) {
    code = json['code'];
    originCode = json['originCode'];
    msg = json['msg'];
    originMsg = json['originMsg'];
    data = json['data'] != null ? new AppVersion.fromJson(json['data']) : null;
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['code'] = this.code;
    data['originCode'] = this.originCode;
    data['msg'] = this.msg;
    data['originMsg'] = this.originMsg;
    if (this.data != null) {
      data['data'] = this.data?.toJson();
    }
    return data;
  }
}
