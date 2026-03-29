class Family {
  final int id;
  final String name;
  final String? description;
  final String? avatar;
  final int creatorId;
  final DateTime createdAt;
  final DateTime? updatedAt;

  Family({
    required this.id,
    required this.name,
    this.description,
    this.avatar,
    required this.creatorId,
    required this.createdAt,
    this.updatedAt,
  });

  factory Family.fromJson(Map<String, dynamic> json) {
    return Family(
      id: json['id'],
      name: json['name'],
      description: json['description'],
      avatar: json['avatar'],
      creatorId: json['creatorId'],
      createdAt: DateTime.parse(json['createdAt']),
      updatedAt: json['updatedAt'] != null ? DateTime.parse(json['updatedAt']) : null,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'name': name,
      'description': description,
      'avatar': avatar,
      'creatorId': creatorId,
      'createdAt': createdAt.toIso8601String(),
      'updatedAt': updatedAt?.toIso8601String(),
    };
  }
}