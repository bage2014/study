import 'package:tutorials/request/model/common/common_param.dart';
import 'package:tutorials/request/model/common/page_query_param.dart';
import 'package:tutorials/request/model/feedback/message_feedback.dart';

class MessageFeedbackInsertRequestParam {
  CommonParam common = CommonParam();

  MessageFeedback? feedback;

  @override
  String toString() {
    return 'MessageFeedbackQueryRequestParam{common: $common, feedback: $feedback}';
  }
}
