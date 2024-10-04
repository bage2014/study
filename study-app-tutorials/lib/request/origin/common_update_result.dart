class CommonUpdateResult {
  int? code;
  int? originCode;
  String? msg;
  String? originMsg;

  CommonUpdateResult(
      {this.code,
        this.originCode,
        this.msg,
        this.originMsg,});

  CommonUpdateResult.fromJson(Map<String, dynamic> json) {
    code = json['code'];
    originCode = json['originCode'];
    msg = json['msg'];
    originMsg = json['originMsg'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['code'] = this.code;
    data['originCode'] = this.originCode;
    data['msg'] = this.msg;
    data['originMsg'] = this.originMsg;
    return data;
  }
}
