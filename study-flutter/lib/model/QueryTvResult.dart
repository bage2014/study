class QueryTvResult {
  int code;
  int originCode;
  String msg;
  Null originMsg;
  List<TvItem> data;

  QueryTvResult({this.code, this.originCode, this.msg, this.originMsg, this.data});

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
    return data;
  }
}

class TvItem {
  int id;
  String name;
  String logo;
  String url;
  Null backupUrls;
  Null tags;

  TvItem({this.id, this.name, this.logo, this.url, this.backupUrls, this.tags});

  TvItem.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    name = json['name'];
    logo = json['logo'];
    url = json['url'];
    backupUrls = json['backupUrls'];
    tags = json['tags'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['id'] = this.id;
    data['name'] = this.name;
    data['logo'] = this.logo;
    data['url'] = this.url;
    data['backupUrls'] = this.backupUrls;
    data['tags'] = this.tags;
    return data;
  }
}