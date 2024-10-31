class AuthorQueryResult {
  Data? data;
  int? code;
  int? originCode;
  String? msg;
  Null? originMsg;

  AuthorQueryResult(
      {this.data, this.code, this.originCode, this.msg, this.originMsg});

  AuthorQueryResult.fromJson(Map<String, dynamic> json) {
    data = json['data'] != null ? new Data.fromJson(json['data']) : null;
    code = json['code'];
    originCode = json['originCode'];
    msg = json['msg'];
    originMsg = json['originMsg'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    if (this.data != null) {
      data['data'] = this.data!.toJson();
    }
    data['code'] = this.code;
    data['originCode'] = this.originCode;
    data['msg'] = this.msg;
    data['originMsg'] = this.originMsg;
    return data;
  }
}

class Data {
  int? id;
  String? firstName;
  String? lastName;
  String? iconUrl;
  String? homePageUrl;
  String? description;
  String? email;

  Data(
      {this.id,
        this.firstName,
        this.lastName,
        this.iconUrl,
        this.homePageUrl,
        this.description,
        this.email});

  Data.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    firstName = json['firstName'];
    lastName = json['lastName'];
    iconUrl = json['iconUrl'];
    homePageUrl = json['homePageUrl'];
    description = json['description'];
    email = json['email'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['id'] = this.id;
    data['firstName'] = this.firstName;
    data['lastName'] = this.lastName;
    data['iconUrl'] = this.iconUrl;
    data['homePageUrl'] = this.homePageUrl;
    data['description'] = this.description;
    data['email'] = this.email;
    return data;
  }
}
