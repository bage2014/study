import 'package:tutorials/request/model/common/common_param.dart';
import 'package:tutorials/request/model/common/page_query_param.dart';

class CommonPageQueryRequestParam {
  CommonParam common = CommonParam();
  PageQueryParam page = PageQueryParam();

  String? keyword;

  @override
  String toString() {
    return 'CommonPageQueryRequestParam{common: $common, page: $page, keyword: $keyword';
  }
}
