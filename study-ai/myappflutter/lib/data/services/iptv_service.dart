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

  Future<List<IptvChannel>> getChannelsByCategory(
    List<String> tags,
    int page,
    int size,
  ) async {
    try {
      // 使用POST请求并传递tags参数
      final response = await _httpClient.post(
        '/iptv/query/tags',
        body: {
          'tags': tags,
          'page': page, // 传递分页参数
          'size': size,
        },
      );

      LogUtil.info('getChannelsByCategory: $response');

      // 确保response是Map类型
      if (response is Map<String, dynamic>) {
        if (response['code'] == 200) {
          // 适配新格式：data是一个对象，内部包含channels数组
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
      LogUtil.error('Error getting channels by category $tags: $e');
      throw Exception('Failed to get channels by category: $e');
    }
  }

  Future<List<IptvChannel>> getChannelsByKeyword(
    String categoryName,
    String keyword,
    int page,
    int size,
  ) async {
    try {
      // 使用POST请求并传递tags参数
      final response = await _httpClient.post(
        '/iptv/channels',
        body: {
          'keyword': keyword, // 使用搜索词作为标签
          'category': categoryName, // 使用分类名称作为标签
          'page': page, // 传递分页参数
          'size': size,
        },
      );

      LogUtil.info('getChannelsByKeyword: $response');

      // 确保response是Map类型
      if (response is Map<String, dynamic>) {
        if (response['code'] == 200) {
          // 适配新格式：data直接是频道数组
          final data = response['data'];

          if (data is List) {
            return data
                .map((item) => IptvChannel.fromJson(item))
                .toList();
          } else {
            throw Exception(
              'Invalid data structure: data is not a list',
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
      LogUtil.error('Error getting channels by keyword $keyword: $e');
      throw Exception('Failed to get channels by keyword: $e');
    }
  }

  // 添加获取喜欢频道的方法
  Future<List<IptvChannel>> getFavoriteChannels(int page, int size) async {
    try {
      final response = await _httpClient.post(
        '/iptv/query/tags',
        body: {
          'tags': ['favorite'], // 使用favorite标签标识喜欢的频道
          'page': page,
          'size': size,
        },
      );
  
      LogUtil.info('getFavoriteChannels: $response');
  
      if (response is Map<String, dynamic>) {
        if (response['code'] == 200) {
          // 适配新格式：data是一个对象，包含channels数组
          final data = response['data'];
  
          if (data is Map<String, dynamic> && data.containsKey('channels') && data['channels'] is List) {
            // 修复这里的类型转换问题
            return (data['channels'] as List)
                .map((item) => IptvChannel.fromJson(item))
                .toList();
          } else {
            throw Exception(
              'Invalid data structure: channels array not found',
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
      LogUtil.error('Error getting favorite channels: $e');
      throw Exception('Failed to get favorite channels: $e');
    }
  }
}
