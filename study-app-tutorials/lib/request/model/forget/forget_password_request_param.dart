import 'package:tutorials/request/model/common/common_param.dart';

class ForgetPasswordRequestParam {
  String? userName;
  String? securityCode;
  CommonParam common = CommonParam();

  ForgetPasswordRequestParam();

  ForgetPasswordRequestParam.fromJson(Map<String, dynamic> json) {
    userName = json['userName'];
    securityCode = json['securityCode'];
    common = CommonParam.fromJson(json['common']);
  }

  Map<String, dynamic> toJson() {
    final _data = <String, dynamic>{};
    _data['userName'] = userName;
    _data['securityCode'] = securityCode;
    _data['common'] = common.toJson();
    return _data;
  }

  @override
  String toString() {
    return 'ForgetPasswordRequestParam{userName: $userName, securityCode: $securityCode, common: $common}';
  }
}
