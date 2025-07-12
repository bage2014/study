class FamilyMember {
  final String? id;
  final String? name;
  final String? avatar;
  final int? generation;
  final String? relationship;
  final List<FamilyMember>? spouses;
  final List<FamilyMember>? parents;
  final List<FamilyMember>? children;

  FamilyMember({
    this.id,
    this.name,
    this.avatar,
    this.generation,
    this.relationship,
    this.spouses,
    this.parents,
    this.children,
  });

  factory FamilyMember.fromJson(Map<String, dynamic> json) {
    return FamilyMember(
      id: json['id'],
      name: json['name'],
      avatar: json['avatar'],
      generation: json['generation'],
      relationship: json['relationship'],
      spouses: json['spouses'] != null 
          ? List<FamilyMember>.from(json['spouses'].map((x) => FamilyMember.fromJson(x)))
          : null,
      parents: json['parents'] != null
          ? List<FamilyMember>.from(json['parents'].map((x) => FamilyMember.fromJson(x)))
          : null,
      children: json['children'] != null
          ? List<FamilyMember>.from(json['children'].map((x) => FamilyMember.fromJson(x)))
          : null,
    );
  }
}
