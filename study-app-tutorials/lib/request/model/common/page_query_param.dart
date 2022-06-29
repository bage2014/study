class PageQueryParam {
  PageQueryParam();
  int targetPage = 1;
  int pageSize = 10;

  
  PageQueryParam.fromJson(Map<String, dynamic> json){
    targetPage = json['targetPage'];
    pageSize = json['pageSize'];
  }

  Map<String, dynamic> toJson() {
    final _data = <String, dynamic>{};
    _data['targetPage'] = targetPage;
    _data['pageSize'] = pageSize;
    return _data;
  }

  @override
  String toString() {
    return 'PageQueryParam{targetPage: $targetPage, pageSize: $pageSize';
  }
}