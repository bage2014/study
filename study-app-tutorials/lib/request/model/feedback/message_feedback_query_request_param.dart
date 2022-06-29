import 'package:tutorials/request/model/common/common_param.dart';
import 'package:tutorials/request/model/common/page_query_param.dart';

class MessageFeedbackQueryRequestParam {
  CommonParam common = CommonParam();
  PageQueryParam page = PageQueryParam();

  String? keyword;
  int? fromUserId;

  @override
  String toString() {
    return 'FeedbackQueryRequestParam{common: $common, page: $page, keyword: $keyword, fromUserId: $fromUserId}';
  }
}
