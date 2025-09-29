import 'package:get/get_connect/http/src/utils/utils.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import 'package:myappflutter/data/api/http_client.dart';
import '../models/iptv_category_model.dart';

class IptvService {
  final HttpClient _httpClient = HttpClient();

  Future<IptvCategoryResponse> getCategories({
    required List<String> tags,
  }) async {
    final response = await HttpClient().post(
      '/iptv/query/group',
      body: {'tags': tags},
    );

    if (response['code'] == 200) {
      final data = response['data'];
      final groupedChannels = data['channelsByGroup'] as Map<String, dynamic>;

      LogUtil.info('channelsByGroup: ${groupedChannels.length}');
      
      // 转换数据格式
      final categories = <String, List<IptvChannel>>{};
      groupedChannels.forEach((groupName, channels) {
       
        categories[groupName] = (channels as List)
            .map((channel) => IptvChannel.fromJson(channel))
            .toList();
      });

      return IptvCategoryResponse(
        categories: categories,
        totalCategories: data['totalGroups'],
        totalChannels: categories.values.fold(
          0,
          (sum, list) => sum + list.length,
        ),
      );
    }

    // 保留对旧格式的兼容处理
    return IptvCategoryResponse.fromJson(response);
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
