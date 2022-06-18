

import 'dart:collection';

import 'package:tutorials/component/http/HttpRequests.dart';
import 'package:tutorials/component/log/Logs.dart';
import 'package:tutorials/constant/http_constant.dart';

class LoginRequest {

  static void Login() async {
    Map<String, String> param = new HashMap();
    param.putIfAbsent("param", () => userName);
    HttpRequests.post(
        HttpConstant.url_login, param, null)
        .then((result) {
      Logs.info('_insertFeedback responseBody=' + (result?.responseBody ?? ""));
      setState(() {
        FeedbackUpdateEvent event = FeedbackUpdateEvent();
        event.data = "hhh";
        EventBus.publish(event);
      });
    }).catchError((error) {
      print(error.toString());
    });
  }

}
