import 'package:tutorials/component/http/http_result.dart';
import 'package:tutorials/request/model/login/login_request_result.dart';

class LoginResultMapping {

  static LoginRequestResult mapping(HttpResult result) {

    return LoginRequestResult();
  }

}
