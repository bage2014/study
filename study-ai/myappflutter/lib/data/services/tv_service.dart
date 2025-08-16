import 'dart:async';
import 'package:myappflutter/core/utils/log_util.dart';
import 'package:myappflutter/data/api/http_client.dart';
import '../models/tv_model.dart';

class TvService {
  final HttpClient _httpClient = HttpClient();

  Future<TvResponse> searchTvChannels({
    required String keyword,
    required int page,
    required int size,
  }) async {
    try {
      // 将queryParameters从Map<String, dynamic>改为Map<String, String>
      Map<String, String> queryParameters = {
        'keyword': keyword,
        'page': page.toString(),
        'size': size.toString(),
      };
      final response = await _httpClient.get(
        '/tv/search',
        queryParameters: queryParameters,
      );
      LogUtil.info('TvService searchTvChannels = $response');
      // 直接使用response作为参数，而不是response.data
      return TvResponse.fromJson(response);
    } catch (e) {
      LogUtil.error('TvService searchTvChannels = $e');
      throw Exception('Failed to search TV channels: $e');
    }
  }
}
