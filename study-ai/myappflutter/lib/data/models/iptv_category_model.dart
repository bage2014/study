class IptvCategoryResponse {
  final Map<String, List<IptvChannel>> categories;

  IptvCategoryResponse({required this.categories});

  factory IptvCategoryResponse.fromJson(Map<String, dynamic> json) {
    final categories = <String, List<IptvChannel>>{};
    
    json.forEach((key, value) {
      if (value is List) {
        categories[key] = List<IptvChannel>.from(
          value.map((x) => IptvChannel.fromJson(x)),
        );
      }
    });
    
    return IptvCategoryResponse(categories: categories);
  }
}

class IptvChannel {
  final String name;
  final String url;
  final String group;
  final String language;
  final String country;
  final String logo;
  final String category;

  IptvChannel({
    required this.name,
    required this.url,
    required this.group,
    required this.language,
    required this.country,
    required this.logo,
    required this.category,
  });

  factory IptvChannel.fromJson(Map<String, dynamic> json) {
    return IptvChannel(
      name: json['name'] ?? '',
      url: json['url'] ?? '',
      group: json['group'] ?? '',
      language: json['language'] ?? '',
      country: json['country'] ?? '',
      logo: json['logo'] ?? '',
      category: json['category'] ?? '',
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'name': name,
      'url': url,
      'group': group,
      'language': language,
      'country': country,
      'logo': logo,
      'category': category,
    };
  }
}