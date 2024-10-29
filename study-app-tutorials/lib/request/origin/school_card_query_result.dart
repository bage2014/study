class SchoolCardQueryResult {
  int? code;
  int? originCode;
  String? msg;
  String? originMsg;
  List<Data>? data;
  Pagination? pagination;

  SchoolCardQueryResult(
      {this.code,
        this.originCode,
        this.msg,
        this.originMsg,
        this.data,
        this.pagination});

  SchoolCardQueryResult.fromJson(Map<String, dynamic> json) {
    code = json['code'];
    originCode = json['originCode'];
    msg = json['msg'];
    originMsg = json['originMsg'];
    if (json['data'] != null) {
      data = <Data>[];
      json['data'].forEach((v) {
        data!.add(new Data.fromJson(v));
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
      data['data'] = this.data!.map((v) => v.toJson()).toList();
    }
    if (this.pagination != null) {
      data['pagination'] = this.pagination!.toJson();
    }
    return data;
  }
}

class Data {
  int? id;
  int? userId;
  int? schoolId;
  String? schoolName;
  int? subjectId;
  String? subjectName;
  String? timeStart;
  String? timeEnd;
  String? imageUrl;

  Data(
      {this.id,
        this.userId,
        this.schoolId,
        this.schoolName,
        this.subjectId,
        this.subjectName,
        this.timeStart,
        this.timeEnd,
        this.imageUrl});

  Data.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    userId = json['userId'];
    schoolId = json['schoolId'];
    schoolName = json['schoolName'];
    subjectId = json['subjectId'];
    subjectName = json['subjectName'];
    timeStart = json['timeStart'];
    timeEnd = json['timeEnd'];
    imageUrl = json['imageUrl'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['id'] = this.id;
    data['userId'] = this.userId;
    data['schoolId'] = this.schoolId;
    data['schoolName'] = this.schoolName;
    data['subjectId'] = this.subjectId;
    data['subjectName'] = this.subjectName;
    data['timeStart'] = this.timeStart;
    data['timeEnd'] = this.timeEnd;
    data['imageUrl'] = this.imageUrl;
    return data;
  }
}

class Pagination {
  int? targetPage;
  int? pageSize;
  int? total;

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
