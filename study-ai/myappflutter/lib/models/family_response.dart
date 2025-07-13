class FamilyResponse {
  final int code;
  final String message;
  final FamilyData data;

  FamilyResponse({
    required this.code,
    required this.message,
    required this.data,
  });

  factory FamilyResponse.fromJson(Map<String, dynamic> json) {
    return FamilyResponse(
      code: json['code'],
      message: json['message'],
      data: FamilyData.fromJson(json['data']),
    );
  }
}

class FamilyData {
  final int id;
  final String name;
  final String avatar;
  final int generation;
  final String relationship;
  final List<FamilyData>? children;

  FamilyData({
    required this.id,
    required this.name,
    required this.avatar,
    required this.generation,
    required this.relationship,
    this.children,
  });

  factory FamilyData.fromJson(Map<String, dynamic> json) {
    return FamilyData(
      id: json['id'],
      name: json['name'],
      avatar: json['avatar'],
      generation: json['generation'],
      relationship: json['relationship'],
      children: json['children'] != null
          ? (json['children'] as List)
                .map((e) => FamilyData.fromJson(e))
                .toList()
          : null,
    );
  }
}
