import 'package:myappflutter/data/models/iptv_category_model.dart';

class IptvCategoryResponse {
  final Map<String, List<IptvChannel>> categories;
  final int totalCategories;
  final int totalChannels;

  IptvCategoryResponse({
    required this.categories,
    required this.totalCategories,
    required this.totalChannels,
  });

  factory IptvCategoryResponse.fromJson(Map<String, dynamic> json) {
    final categories = <String, List<IptvChannel>>{};
    
    // 解析新的后端格式
    final categoriesList = json['categories'] as List<dynamic>;
    
    for (var category in categoriesList) {
      final categoryMap = category as Map<String, dynamic>;
      final categoryType = categoryMap['type'] as String;
      final channelsList = categoryMap['channels'] as List<dynamic>;
      
      categories[categoryType] = List<IptvChannel>.from(
        channelsList.map((x) => IptvChannel.fromJson(x)),
      );
    }
    
    return IptvCategoryResponse(
      categories: categories,
      totalCategories: json['totalCategories'] ?? 0,
      totalChannels: json['totalChannels'] ?? 0,
    );
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