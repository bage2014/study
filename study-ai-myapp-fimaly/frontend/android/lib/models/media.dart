class Media {
  final int id;
  final int familyId;
  final String type;
  final String url;
  final String? description;
  final int uploadedBy;
  final DateTime uploadedAt;
  final DateTime? updatedAt;

  Media({
    required this.id,
    required this.familyId,
    required this.type,
    required this.url,
    this.description,
    required this.uploadedBy,
    required this.uploadedAt,
    this.updatedAt,
  });

  factory Media.fromJson(Map<String, dynamic> json) {
    return Media(
      id: json['id'],
      familyId: json['familyId'],
      type: json['type'],
      url: json['url'],
      description: json['description'],
      uploadedBy: json['uploadedBy'],
      uploadedAt: DateTime.parse(json['uploadedAt']),
      updatedAt: json['updatedAt'] != null ? DateTime.parse(json['updatedAt']) : null,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'familyId': familyId,
      'type': type,
      'url': url,
      'description': description,
      'uploadedBy': uploadedBy,
      'uploadedAt': uploadedAt.toIso8601String(),
      'updatedAt': updatedAt?.toIso8601String(),
    };
  }
}