import 'package:tutorials/request/model/common/common_param.dart';

class UserDetailParam {
  String? userName;
  String? password;
  String? securityCode;

  CommonParam common = CommonParam();

  @override
  String toString() {
    return 'UserDetailParam{userName: $userName, password: $password, securityCode: $securityCode, common: $common}';
  }
}
