class Person {
  final int id;
  final String name;
  final String gender;
  final String birthDate;
  final String? deathDate;
  final String description;

  Person({
    required this.id,
    required this.name,
    required this.gender,
    required this.birthDate,
    this.deathDate,
    required this.description,
  });

  factory Person.fromJson(Map<String, dynamic> json) {
    return Person(
      id: json['id'],
      name: json['name'],
      gender: json['gender'],
      birthDate: json['birthDate'],
      deathDate: json['deathDate'],
      description: json['description'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'name': name,
      'gender': gender,
      'birthDate': birthDate,
      'deathDate': deathDate,
      'description': description,
    };
  }
}