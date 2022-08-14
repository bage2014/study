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
  int? id;
  String? userName;
  String? phone;
  String? mail;
  String? password;
  String? sex;
  Null? iconId;
  Null? iconUrl;
  String? birthday;
  String? createTime;
  String? updateTime;
  Null? deleteState;

  Data(
      {this.id,
        this.userName,
        this.phone,
        this.mail,
        this.password,
        this.sex,
        this.iconId,
        this.iconUrl,
        this.birthday,
        this.createTime,
        this.updateTime,
        this.deleteState});

  Data.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    userName = json['userName'];
    phone = json['phone'];
    mail = json['mail'];
    password = json['password'];
    sex = json['sex'];
    iconId = json['iconId'];
    iconUrl = json['iconUrl'];
    birthday = json['birthday'];
    createTime = json['createTime'];
    updateTime = json['updateTime'];
    deleteState = json['deleteState'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['id'] = this.id;
    data['userName'] = this.userName;
    data['phone'] = this.phone;
    data['mail'] = this.mail;
    data['password'] = this.password;
    data['sex'] = this.sex;
    data['iconId'] = this.iconId;
    data['iconUrl'] = this.iconUrl;
    data['birthday'] = this.birthday;
    data['createTime'] = this.createTime;
    data['updateTime'] = this.updateTime;
    data['deleteState'] = this.deleteState;
    return data;
  }
}
