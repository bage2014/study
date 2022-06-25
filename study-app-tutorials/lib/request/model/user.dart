
import 'package:tutorials/request/model/login/login_request_result.dart';

class User {
  int? id;
  String? userName;
  String? iconUrl;
  String? mail;

  static User from(LoginRequestResult result) {
    User user = User();
    user.id = result.id;
    user.userName = result.userName;
    user.iconUrl = result.iconUrl;
    user.mail = result.mail;
    return user;
  }

}
