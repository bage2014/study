class QueryTvResult {
  int code;
  int originCode;
  String msg;
  Null originMsg;
  List<TvItem> data;
  Pagination pagination;

  QueryTvResult(
      {this.code,
        this.originCode,
        this.msg,
        this.originMsg,
        this.data,
        this.pagination});

  QueryTvResult.fromJson(Map<String, dynamic> json) {
    code = json['code'];
    originCode = json['originCode'];
    msg = json['msg'];
    originMsg = json['originMsg'];
    if (json['data'] != null) {
      data = new List<TvItem>();
      json['data'].forEach((v) {
        data.add(new TvItem.fromJson(v));
      });
    }
    pagination = json['pagination'] != null
        ? new Pagination.fromJson(json['pagination'])
        : null;
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['code'] = this.code;
    data['originCode'] = this.originCode;
    data['msg'] = this.msg;
    data['originMsg'] = this.originMsg;
    if (this.data != null) {
      data['data'] = this.data.map((v) => v.toJson()).toList();
    }
    if (this.pagination != null) {
      data['pagination'] = this.pagination.toJson();
    }
    return data;
  }
}

class TvItem {
  int id;
  String name;
  String logo;
  bool isFavorite;
  int userId;
  List<String> urls;

  TvItem(
      {this.id, this.name, this.logo, this.isFavorite, this.userId, this.urls});

  TvItem.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    name = json['name'];
    logo = json['logo'];
    isFavorite = json['isFavorite'];
    userId = json['userId'];
    urls = json['urls'].cast<String>();
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['id'] = this.id;
    data['name'] = this.name;
    data['logo'] = this.logo;
    data['isFavorite'] = this.isFavorite;
    data['userId'] = this.userId;
    data['urls'] = this.urls;
    return data;
  }
}

class Pagination {
  int targetPage;
  int pageSize;
  int total;

  Pagination({this.targetPage, this.pageSize, this.total});

  Pagination.fromJson(Map<String, dynamic> json) {
    targetPage = json['targetPage'];
    pageSize = json['pageSize'];
    total = json['total'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['targetPage'] = this.targetPage;
    data['pageSize'] = this.pageSize;
    data['total'] = this.total;
    return data;
  }
}