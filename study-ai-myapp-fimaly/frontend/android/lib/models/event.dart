class Event {
  final int id;
  final int familyId;
  final String name;
  final String? description;
  final DateTime? eventDate;
  final String? relatedMembers;
  final String? photo;
  final DateTime createdAt;
  final DateTime? updatedAt;

  Event({
    required this.id,
    required this.familyId,
    required this.name,
    this.description,
    this.eventDate,
    this.relatedMembers,
    this.photo,
    required this.createdAt,
    this.updatedAt,
  });

  factory Event.fromJson(Map<String, dynamic> json) {
    return Event(
      id: json['id'],
      familyId: json['familyId'],
      name: json['name'],
      description: json['description'],
      eventDate: json['eventDate'] != null ? DateTime.parse(json['eventDate']) : null,
      relatedMembers: json['relatedMembers'],
      photo: json['photo'],
      createdAt: DateTime.parse(json['createdAt']),
      updatedAt: json['updatedAt'] != null ? DateTime.parse(json['updatedAt']) : null,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'familyId': familyId,
      'name': name,
      'description': description,
      'eventDate': eventDate?.toIso8601String(),
      'relatedMembers': relatedMembers,
      'photo': photo,
      'createdAt': createdAt.toIso8601String(),
      'updatedAt': updatedAt?.toIso8601String(),
    };
  }
}