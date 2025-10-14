import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:myappflutter/core/config/app_routes.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import 'package:myappflutter/data/api/http_client.dart';
import '../widgets/base_page.dart';

class AppVersionPage extends StatefulWidget {
  const AppVersionPage({super.key});

  @override
  State<AppVersionPage> createState() => _AppVersionPageState();
}

class _AppVersionPageState extends State<AppVersionPage> {
  final HttpClient _httpClient = HttpClient();
  List<dynamic> _versions = [];
  bool _isLoading = false;
  bool _hasMore = true;
  int _currentPage = 0;
  final int _pageSize = 5;

  @override
  void initState() {
    super.initState();
    _fetchVersions();
  }

  Future<void> _fetchVersions({bool refresh = false}) async {
    if (_isLoading) return;

    setState(() {
      _isLoading = true;
      if (refresh) {
        _currentPage = 0;
        _versions.clear();
        _hasMore = true;
      }
    });

    try {
      final response = await _httpClient.get('/app/versions');

      if (response['code'] == 200 && response['data'] != null) {
        final data = response['data'];
        final List<dynamic> newVersions = data['versions'] ?? [];

        setState(() {
          _versions.addAll(newVersions);
          _currentPage++;
          _hasMore = newVersions.length == _pageSize;
        });
      } else {
        Get.snackbar('error'.tr, 'fetch_version_failed'.tr + ': ${response['message'] ?? 'unknown_error'.tr}');
      }
    } catch (e) {
      LogUtil.error('获取版本列表异常: $e');
      Get.snackbar('error'.tr, 'fetch_version_failed'.tr + ', ' + 'retry_later'.tr);
    } finally {
      setState(() {
        _isLoading = false;
      });
    }
  }

  Widget _buildVersionCard(Map<String, dynamic> version) {
    return Card(
      margin: const EdgeInsets.symmetric(vertical: 8, horizontal: 16),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  '${'version'.tr} ${version['version']}',
                  style: const TextStyle(
                    fontSize: 18,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                if (version['forceUpdate'] == true)
                  Container(
                    padding: const EdgeInsets.symmetric(
                      horizontal: 8,
                      vertical: 4,
                    ),
                    decoration: BoxDecoration(
                      color: Colors.red,
                      borderRadius: BorderRadius.circular(12),
                    ),
                    child: Text(
                      'force_update'.tr,
                      style: const TextStyle(
                        color: Colors.white,
                        fontSize: 12,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),
              ],
            ),
            const SizedBox(height: 8),
            Text('${'release_date'.tr}: ${version['releaseDate']}'),
            const SizedBox(height: 8),
            Text('${'update_content'.tr}: ${version['releaseNotes']}'),
            const SizedBox(height: 12),
            ElevatedButton(
              onPressed: () {
                // 跳转到更新页面
                Get.toNamed(
                  AppRoutes.UPDATE,
                  arguments: {'version': version['version']},
                );
              },
              child: Text('update_app'.tr),
            ),
          ],
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return BasePage(
      title: 'app_version'.tr,
      body: Column(
        children: [
          // 刷新按钮
          Padding(
            padding: const EdgeInsets.all(16),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.end,
              children: [
                ElevatedButton.icon(
                  onPressed: _isLoading
                      ? null
                      : () => _fetchVersions(refresh: true),
                  icon: const Icon(Icons.refresh),
                  label: Text('refresh'.tr),
                ),
              ],
            ),
          ),

          // 版本列表
          Expanded(
            child: _versions.isEmpty && !_isLoading
                ? Center(child: Text('no_version_info'.tr))
                : ListView.builder(
                    itemCount: _versions.length + (_hasMore ? 1 : 0),
                    itemBuilder: (context, index) {
                      if (index == _versions.length) {
                        return _hasMore
                            ? Padding(
                                padding: const EdgeInsets.all(16),
                                child: Center(
                                  child: CircularProgressIndicator(),
                                ),
                              )
                            : Container();
                      }
                      return _buildVersionCard(_versions[index]);
                    },
                  ),
          ),
        ],
      ),
    );
  }
}
