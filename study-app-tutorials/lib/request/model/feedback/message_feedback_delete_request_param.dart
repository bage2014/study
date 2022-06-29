import 'package:tutorials/request/model/common/common_param.dart';
import 'package:tutorials/request/model/common/page_query_param.dart';
import 'package:tutorials/request/model/feedback/message_feedback.dart';

class MessageFeedbackDeleteRequestParam {
  CommonParam common = CommonParam();

  int? feedbackId;

  @override
  String toString() {
    return 'MessageFeedbackQueryRequestParam{common: $common, feedbackId: $feedbackId}';
  }
}
