
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