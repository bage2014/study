class Relationship {
  final int id;
  final int memberId1;
  final int memberId2;
  final String relationshipType;
  final DateTime? createdAt;
  final DateTime? updatedAt;

  Relationship({
    required this.id,
    required this.memberId1,
    required this.memberId2,
    required this.relationshipType,
    this.createdAt,
    this.updatedAt,
  });

  factory Relationship.fromJson(Map<String, dynamic> json) {
    return Relationship(
      id: json['id'],
      memberId1: json['memberId1'],
      memberId2: json['memberId2'],
      relationshipType: json['relationshipType'],
      createdAt: json['createdAt'] != null ? DateTime.parse(json['createdAt']) : null,
      updatedAt: json['updatedAt'] != null ? DateTime.parse(json['updatedAt']) : null,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'memberId1': memberId1,
      'memberId2': memberId2,
      'relationshipType': relationshipType,
      'createdAt': createdAt?.toIso8601String(),
      'updatedAt': updatedAt?.toIso8601String(),
    };
  }
}