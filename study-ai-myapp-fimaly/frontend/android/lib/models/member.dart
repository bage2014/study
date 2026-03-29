class Member {
  final int id;
  final int familyId;
  final String name;
  final String gender;
  final DateTime? birthDate;
  final DateTime? deathDate;
  final String? photo;
  final String? details;
  final DateTime createdAt;
  final DateTime? updatedAt;

  Member({
    required this.id,
    required this.familyId,
    required this.name,
    required this.gender,
    this.birthDate,
    this.deathDate,
    this.photo,
    this.details,
    required this.createdAt,
    this.updatedAt,
  });

  factory Member.fromJson(Map<String, dynamic> json) {
    return Member(
      id: json['id'],
      familyId: json['familyId'],
      name: json['name'],
      gender: json['gender'],
      birthDate: json['birthDate'] != null ? DateTime.parse(json['birthDate']) : null,
      deathDate: json['deathDate'] != null ? DateTime.parse(json['deathDate']) : null,
      photo: json['photo'],
      details: json['details'],
      createdAt: DateTime.parse(json['createdAt']),
      updatedAt: json['updatedAt'] != null ? DateTime.parse(json['updatedAt']) : null,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'familyId': familyId,
      'name': name,
      'gender': gender,
      'birthDate': birthDate?.toIso8601String(),
      'deathDate': deathDate?.toIso8601String(),
      'photo': photo,
      'details': details,
      'createdAt': createdAt.toIso8601String(),
      'updatedAt': updatedAt?.toIso8601String(),
    };
  }
}