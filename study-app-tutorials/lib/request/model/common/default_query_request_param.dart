import 'package:tutorials/request/model/common/common_param.dart';
import 'package:tutorials/request/model/common/page_query_param.dart';

class DefaultQueryRequestParam extends CommonParam {

  String? keyword;

  DefaultQueryRequestParam(){

  }

  DefaultQueryRequestParam.fromJson(Map<String, dynamic> json){
    keyword = json['keyword'];
  }

  Map<String, dynamic> toJson() {
    final _data = <String, dynamic>{};
    _data['keyword'] = keyword;
    return _data;
  }

  @override
  String toString() {
    return 'DefaultQueryRequestParam{keyword: $keyword}';
  }
}
