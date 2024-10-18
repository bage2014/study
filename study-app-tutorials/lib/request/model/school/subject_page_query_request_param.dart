import 'package:tutorials/request/model/common/common_param.dart';
import 'package:tutorials/request/model/common/default_page_query_request_param.dart';
import 'package:tutorials/request/model/common/page_query_param.dart';

class SubjectPageQueryRequestParam extends DefaultPageQueryRequestParam {

  int? schoolId;

  SubjectPageQueryRequestParam(){

  }

  SubjectPageQueryRequestParam.fromJson(Map<String, dynamic> json){
    schoolId = json['schoolId'];
  }

  Map<String, dynamic> toJson() {
    final _data = <String, dynamic>{};
    _data['schoolId'] = schoolId;
    return _data;
  }

  @override
  String toString() {
    return 'PageQueryParam{schoolId: $schoolId}';
  }
}
