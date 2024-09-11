
class TvItem {
  int? id;
  String? name;
  String? logo;
  bool? isFavorite;
  int? favoriteId;
  int? userId;
  List<String>? urls;

  TvItem(
      {this.id = 0,
        this.name = "",
        this.logo = "",
        this.isFavorite = false,
        this.favoriteId = 0,
        this.userId,
        this.urls});

  TvItem.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    name = json['name'];
    logo = json['logo'];
    isFavorite = json['isFavorite'];
    favoriteId = json['favoriteId'];
    userId = json['userId'];
    urls = json['urls'].cast<String>();
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['id'] = this.id;
    data['name'] = this.name;
    data['logo'] = this.logo;
    data['isFavorite'] = this.isFavorite;
    data['favoriteId'] = this.favoriteId;
    data['userId'] = this.userId;
    data['urls'] = this.urls;
    return data;
  }
}