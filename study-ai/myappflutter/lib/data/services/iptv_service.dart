import 'package:myappflutter/data/api/http_client.dart';
import '../models/iptv_category_model.dart';

class IptvService {
  final HttpClient _httpClient = HttpClient();

  Future<IptvCategoryResponse> getCategories() async {
    final response = await _httpClient.get('iptv/categories');

    // 处理新的后端响应格式: {"code": 200, "message": "success", "data": [...]}
    if (response is Map<String, dynamic>) {
      if (response['code'] == 200) {
        // 获取频道列表
        final channelsData = response['data'] as List<dynamic>;
        
        // 重构数据以符合 IptvCategoryResponse 结构
        final categoriesMap = <String, List<IptvChannel>>{};
        final List<IptvChannel> channels = [];
        
        for (var item in channelsData) {
          final channelData = item as Map<String, dynamic>;
          
          // 创建频道对象
          final channel = IptvChannel(
            name: channelData['name'] ?? '',
            url: channelData['url'] ?? '',
            group: channelData['category'] ?? '', // 使用 category 作为 group
            language: channelData['language'] ?? '',
            country: channelData['country'] ?? '', // 使用默认值
            logo: channelData['logo'] ?? '', // 使用默认值
            category: channelData['category'] ?? '',
          );
          
          channels.add(channel);
          
          // 按分类组织频道
          final category = channelData['category'] ?? '未知分类';
          if (!categoriesMap.containsKey(category)) {
            categoriesMap[category] = [];
          }
          categoriesMap[category]!.add(channel);
        }
        
        // 返回重构后的响应
        return IptvCategoryResponse(
          categories: categoriesMap,
          totalCategories: categoriesMap.length,
          totalChannels: channels.length,
        );
      } else {
        throw Exception('API Error: ${response['message']}');
      }
    } else {
      // 保持对旧格式的兼容
      return IptvCategoryResponse.fromJson(response);
    }
  }

  Future<List<IptvChannel>> searchChannels(String keyword) async {
    final response = await _httpClient.get(
      'iptv/search',
      queryParameters: {'q': keyword},
    );

    // 假设搜索接口返回的是列表格式
    if (response is List) {
      return (response as List)
          .map((item) => IptvChannel.fromJson(item))
          .toList();
    } else if (response is Map<String, dynamic>) {
      // 如果返回的是包含列表的对象，需要根据实际API结构调整
      // 这里假设响应格式为 {'data': [channel1, channel2, ...]}
      if (response.containsKey('data') && response['data'] is List) {
        return (response['data'] as List)
            .map((item) => IptvChannel.fromJson(item))
            .toList();
      } else {
        return [IptvChannel.fromJson(response)];
      }
    } else {
      throw Exception('Unexpected response format');
    }
  }

  Future<List<IptvChannel>> getChannelsByCategory(String category) async {
    final response = await _httpClient.get('iptv/$category');

    // 处理新的后端响应格式: {"code": 200, "message": "success", "data": [...]}
    if (response is Map<String, dynamic>) {
      if (response['code'] == 200) {
        final data = response['data'] as List<dynamic>;
        return data.map((item) => IptvChannel.fromJson(item)).toList();
      } else {
        throw Exception('API Error: ${response['message']}');
      }
    } else if (response is List) {
      // 保持对旧格式的兼容
      return (response as List)
          .map((item) => IptvChannel.fromJson(item))
          .toList();
    } else {
      throw Exception('Unexpected response format');
    }
  }
}
