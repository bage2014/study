
import 'package:tutorials/request/model/common/common_result.dart';

class UserDetailResult {

  CommonResult common = CommonResult();

  int? id;
  String? userName;
  String? iconUrl;
  String? mail;

  @override
  String toString() {
    return 'UserDetailResult{common: $common, id: $id, userName: $userName, iconUrl: $iconUrl, mail: $mail}';
  }
}
