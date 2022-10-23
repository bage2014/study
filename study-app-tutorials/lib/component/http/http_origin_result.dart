class HttpOriginResult {
  int? code;
  int? originCode;
  String? msg;
  Null? originMsg;
  Data? data;

  HttpOriginResult(
      {this.code, this.originCode, this.msg, this.originMsg, this.data});

  HttpOriginResult.fromJson(Map<String, dynamic> json) {
    code = json['code'];
    originCode = json['originCode'];
    msg = json['msg'];
    originMsg = json['originMsg'];
    data = json['data'] != null ? new Data.fromJson(json['data']) : null;
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['code'] = this.code;
    data['originCode'] = this.originCode;
    data['msg'] = this.msg;
    data['originMsg'] = this.originMsg;
    if (this.data != null) {
      data['data'] = this.data!.toJson();
    }
    return data;
  }
}

class Data {
  String? error;
  String? errorDescription;

  Data({this.error, this.errorDescription});

  Data.fromJson(Map<String, dynamic> json) {
    error = json['error'];
    errorDescription = json['error_description'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['error'] = this.error;
    data['error_description'] = this.errorDescription;
    return data;
  }
}
