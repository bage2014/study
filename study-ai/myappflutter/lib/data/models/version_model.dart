class Version {
  final String version;
  final String releaseDate;
  final String releaseNotes;
  final bool forceUpdate;
  final String fileId;
  final int id;

  Version({
    required this.version,
    required this.releaseDate,
    required this.releaseNotes,
    required this.forceUpdate,
    required this.fileId,
    required this.id,
  });

  factory Version.fromJson(Map<String, dynamic> json) {
    return Version(
      version: json['version'] ?? '',
      releaseDate: json['releaseDate'] ?? '',
      releaseNotes: json['releaseNotes'] ?? '',
      forceUpdate: json['forceUpdate'] ?? false,
      fileId: json['fileId'],
      id: json['id'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'version': version,
      'releaseDate': releaseDate,
      'releaseNotes': releaseNotes,
      'forceUpdate': forceUpdate,
      'fileId': fileId,
      'id': id,
    };
  }

  @override
  String toString() {
    return 'Version{version: $version, releaseDate: $releaseDate, releaseNotes: $releaseNotes, forceUpdate: $forceUpdate, fileId: $fileId, id: $id}';
  }

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is Version &&
          runtimeType == other.runtimeType &&
          version == other.version &&
          releaseDate == other.releaseDate &&
          releaseNotes == other.releaseNotes &&
          forceUpdate == other.forceUpdate &&
          fileId == other.fileId &&
          id == other.id;

  @override
  int get hashCode =>
      version.hashCode ^
      releaseDate.hashCode ^
      releaseNotes.hashCode ^
      forceUpdate.hashCode ^
      fileId.hashCode ^
      id.hashCode;
}