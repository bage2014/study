import 'package:tutorials/request/model/common_param.dart';

class RegisterRequestParam {
  String? userName;
  String? password;
  String? securityCode;
  CommonParam common = CommonParam();

  RegisterRequestParam();

  RegisterRequestParam.fromJson(Map<String, dynamic> json) {
    userName = json['userName'];
    password = json['password'];
    securityCode = json['securityCode'];
    common = CommonParam.fromJson(json['common']);
  }

  Map<String, dynamic> toJson() {
    final _data = <String, dynamic>{};
    _data['userName'] = userName;
    _data['password'] = password;
    _data['securityCode'] = securityCode;
    _data['common'] = common.toJson();
    return _data;
  }

}
