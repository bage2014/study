import '../common/common_result.dart';

class LoginRequestResult {

  CommonResult common = CommonResult();

  int? id;
  String? userName;
  String? iconUrl;
  String? mail;

  @override
  String toString() {
    return 'LoginRequestResult{common: $common, id: $id, userName: $userName, iconUrl: $iconUrl, mail: $mail}';
  }
}
