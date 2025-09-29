import 'package:myappflutter/core/utils/log_util.dart';
import 'package:myappflutter/data/api/http_client.dart';
import '../models/iptv_category_model.dart';

class IptvService {
  final HttpClient _httpClient = HttpClient();

  Future<IptvCategoryResponse> getCategories({
    required List<String> tags,
  }) async {
    try {
      final response = await _httpClient.post(
        '/iptv/query/group',
        body: {'tags': tags},
      );

      if (response['code'] == 200) {
        final data = response['data'];
        // 适配新格式：data['groups']是一个数组
        final groupsData = data['groups'] as List;

        // 转换数据格式
        final categories = <String, List<IptvChannel>>{};
        int totalChannels = 0;

        // 遍历groups数组
        for (var group in groupsData) {
          final groupName = group['groupName'] as String;
          final groupChannelsCount = group['totalChannels'] as int;

          // 初始化该分组的频道列表（实际项目中可能需要额外请求获取频道详情）
          categories[groupName] = [];
          totalChannels += groupChannelsCount;
        }

        return IptvCategoryResponse(
          categories: categories,
          totalCategories: groupsData.length,
          totalChannels: totalChannels,
        );
      } else {
        throw Exception('Failed to load categories: ${response['message']}');
      }
    } catch (e) {
      LogUtil.error('Error loading categories: $e');
      throw Exception('Failed to load categories: $e');
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
    try {
      // 使用POST请求并传递tags参数
      final response = await _httpClient.post(
        '/iptv/query/tags',
        body: {
          'tags': [category],
        },
      );

      LogUtil.info('getChannelsByCategory: $response');

      // 确保response是Map类型
      if (response is Map<String, dynamic>) {
        if (response['code'] == 200) {
          // 适配新格式：channels在data内部
          final data = response['data'] as Map<String, dynamic>?;

          if (data != null &&
              data.containsKey('channels') &&
              data['channels'] is List) {
            return (data['channels'] as List)
                .map((item) => IptvChannel.fromJson(item))
                .toList();
          } else {
            throw Exception(
              'Invalid data structure: channels not found or not a list',
            );
          }
        } else {
          throw Exception(
            'API Error: ${response['message'] ?? 'Unknown error'}',
          );
        }
      } else {
        throw Exception('Unexpected response format: ${response.runtimeType}');
      }
    } catch (e) {
      LogUtil.error('Error getting channels by category $category: $e');
      throw Exception('Failed to get channels by category: $e');
    }
  }
}
