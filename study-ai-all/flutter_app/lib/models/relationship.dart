class Relationship {
  final int id;
  final int person1Id;
  final int person2Id;
  final String type;
  final String description;

  Relationship({
    required this.id,
    required this.person1Id,
    required this.person2Id,
    required this.type,
    required this.description,
  });

  factory Relationship.fromJson(Map<String, dynamic> json) {
    return Relationship(
      id: json['id'],
      person1Id: json['person1Id'],
      person2Id: json['person2Id'],
      type: json['type'],
      description: json['description'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'person1Id': person1Id,
      'person2Id': person2Id,
      'type': type,
      'description': description,
    };
  }
}