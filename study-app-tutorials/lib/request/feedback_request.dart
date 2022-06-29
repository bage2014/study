import 'dart:collection';

import 'package:tutorials/component/log/Logs.dart';
import 'package:tutorials/request/model/feedback/message_feedback.dart';
import 'package:tutorials/request/model/feedback/message_feedback_query_request_param.dart';
import 'package:tutorials/request/model/feedback/message_feedback_query_request_result.dart';

class MessageFeedbackRequests {
  static Future<MessageFeedbackQueryRequestResult> query(
      MessageFeedbackQueryRequestParam requestParam) async {
    Logs.info('request param : ${requestParam?.toString()}');

    // String url = HttpConstant.url_settings_version_check
    //     .replaceAll("{version}", AppUtils.getCurrentVersion().toString());
    // HttpResult httpResult = await HttpRequests.get(url, null, null);
    // String? temp = httpResult?.responseBody;
    // String responseBody = temp == null ? "" : temp;
    // Logs.info("responseBody = ${responseBody}");
    Map<String, String> param = HashMap();
    // param.putIfAbsent("param", () => userName);
    // return HttpRequests.post(HttpConstant.url_login, param, null);
    return Future.delayed(const Duration(seconds: 1), () => mock());
  }

  static MessageFeedbackQueryRequestResult mock() {
    MessageFeedback feedback = MessageFeedback();
    feedback.id = 1;
    feedback.fromUserId = 1235;
    feedback.fromUserName = '张三';
    feedback.fromUserIconUrl = 'https://avatars.githubusercontent.com/u/18094768?v=4';
    feedback.toUserId = 1234;
    feedback.toUserName = '李四';
    feedback.msgContent = '这是留言内容，吧啦吧啦吧啦吧';
    feedback.readStatus = 'read';
    feedback.sendTime = '2022-06-06 10:24';
    feedback.readTime = '2022-06-06 10:24';
    feedback.msgType = 'default';
    feedback.createTime = '2022-06-06 10:24';

    MessageFeedbackQueryRequestResult result = MessageFeedbackQueryRequestResult();
    result.common.code = 200;
    result.common.message = "ok";
    result.feedbacks = [
      feedback,
    ];
    Logs.info('request result : ${result?.toString()}');
    return result;
  }
}
