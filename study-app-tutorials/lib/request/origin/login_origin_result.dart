class LoginOriginResult {
  int? code;
  int? originCode;
  String? msg;
  String? originMsg;
  Data? data;

  LoginOriginResult(
      {this.code, this.originCode, this.msg, this.originMsg, this.data});

  LoginOriginResult.fromJson(Map<String, dynamic> json) {
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
  String? accessToken;
  String? tokenType;
  String? refreshToken;
  int? expiresIn;
  String? scope;
  String? sub;
  String? jti;

  Data(
      {this.accessToken,
        this.tokenType,
        this.refreshToken,
        this.expiresIn,
        this.scope,
        this.sub,
        this.jti});

  Data.fromJson(Map<String, dynamic> json) {
    accessToken = json['access_token'];
    tokenType = json['token_type'];
    refreshToken = json['refresh_token'];
    expiresIn = json['expires_in'];
    scope = json['scope'];
    sub = json['sub'];
    jti = json['jti'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['access_token'] = this.accessToken;
    data['token_type'] = this.tokenType;
    data['refresh_token'] = this.refreshToken;
    data['expires_in'] = this.expiresIn;
    data['scope'] = this.scope;
    data['sub'] = this.sub;
    data['jti'] = this.jti;
    return data;
  }
}
