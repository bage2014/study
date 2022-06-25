import 'package:tutorials/request/model/common/common_param.dart';

class LoginRequestParam {
  String? userName;
  String? password;
  String? securityCode;

  CommonParam common = CommonParam();

  @override
  String toString() {
    return 'LoginRequestParam{userName: $userName, password: $password, securityCode: $securityCode, common: $common}';
  }
}
