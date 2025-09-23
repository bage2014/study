

import 'package:myappflutter/data/api/http_client.dart';
import '../models/iptv_category_model.dart';

class IptvService {
  final HttpClient _httpClient = HttpClient();

  Future<IptvCategoryResponse> getCategories() async {
    final response = await _httpClient.get(
      'iptv/categories',
    );
    return IptvCategoryResponse.fromJson(response);
  }

  Future<List<IptvChannel>> searchChannels(String keyword) async {
    final response = await _httpClient.get(
      'iptv/search',
      queryParameters: {'q': keyword},
    );
    
    // 假设搜索接口返回的是列表格式
    if (response is List) {
      return (response as List).map((item) => IptvChannel.fromJson(item)).toList();
    } else if (response is Map<String, dynamic>) {
      // 如果返回的是包含列表的对象，需要根据实际API结构调整
      // 这里假设响应格式为 {'data': [channel1, channel2, ...]}
      if (response.containsKey('data') && response['data'] is List) {
        return (response['data'] as List).map((item) => IptvChannel.fromJson(item)).toList();
      } else {
        return [IptvChannel.fromJson(response)];
      }
    } else {
      throw Exception('Unexpected response format');
    }
  }
}
