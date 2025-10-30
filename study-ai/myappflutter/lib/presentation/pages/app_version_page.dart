import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:myappflutter/core/config/app_routes.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import 'package:myappflutter/data/api/http_client.dart';
import 'package:myappflutter/data/models/version_model.dart';
import '../widgets/base_page.dart';

class AppVersionPage extends StatefulWidget {
  const AppVersionPage({super.key});

  @override
  State<AppVersionPage> createState() => _AppVersionPageState();
}

class _AppVersionPageState extends State<AppVersionPage> {
  final HttpClient _httpClient = HttpClient();
  List<Version> _versions = [];
  bool _isLoading = false;
  bool _hasMore = true;
  int _currentPage = 0;
  int _totalPages = 1;
  final int _pageSize = 3;

  @override
  void initState() {
    super.initState();
    _fetchVersions();
  }

  Future<void> _fetchVersions({bool refresh = false}) async {
    return _fetchPage(1);
  }

  Future<void> _fetchPage(int page) async {
    if (_isLoading) return;

    setState(() {
      _isLoading = true;
    });

    try {
      final response = await _httpClient.get(
        '/app/versions?page=${page - 1}&size=$_pageSize',
      );

      if (response['code'] == 200 && response['data'] != null) {
        final data = response['data'];
        final List<dynamic> versionData = data['versions'] ?? [];
        final List<Version> newVersions = versionData
            .map<Version>((item) => Version.fromJson(item))
            .toList();

        // 从响应中获取分页信息
        _totalPages = data['totalPages'] ?? 1;
        final int currentPage = (data['currentPage'] ?? 0) + 1; // 后端从0开始，前端从1开始
        final int totalElements = data['totalElements'] ?? 0;

        setState(() {
          _versions = newVersions;
          _currentPage = currentPage;
          _hasMore = currentPage < _totalPages;
        });
      } else {
        Get.snackbar(
          'error'.tr,
          'fetch_version_failed'.tr +
              ': ${response['message'] ?? 'unknown_error'.tr}',
        );
      }
    } catch (e) {
      LogUtil.error('获取版本列表异常: $e');
      Get.snackbar(
        'error'.tr,
        'fetch_version_failed'.tr + ', ' + 'retry_later'.tr,
      );
    } finally {
      setState(() {
        _isLoading = false;
      });
    }
  }

  Widget _buildVersionCard(Version version) {
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
                  '${'version'.tr} ${version.version}',
                  style: const TextStyle(
                    fontSize: 18,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                if (version.forceUpdate == true)
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
            Text('${'release_date'.tr}: ${version.releaseDate}'),
            const SizedBox(height: 8),
            Text('${'update_content'.tr}: ${version.releaseNotes}'),
            const SizedBox(height: 12),
            ElevatedButton(
              onPressed: () {
                // 跳转到更新页面，传递整个version对象
                Get.toNamed(AppRoutes.UPDATE, arguments: {'version': version});
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
                    itemCount: _versions.length,
                    itemBuilder: (context, index) {
                      return _buildVersionCard(_versions[index]);
                    },
                  ),
          ),

          // 分页控件
          if (_versions.isNotEmpty)
            Container(
              padding: const EdgeInsets.all(16),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  // 上一页按钮
                  ElevatedButton.icon(
                    onPressed: _currentPage > 1 && !_isLoading
                        ? () => _fetchPage(_currentPage - 1)
                        : null,
                    icon: const Icon(Icons.arrow_back),
                    label: Text('previous_page'.tr),
                    style: ElevatedButton.styleFrom(
                      backgroundColor: _currentPage > 1
                          ? Colors.blue
                          : Colors.grey,
                    ),
                  ),

                  // 页码显示
                  Column(
                    children: [
                      Container(
                        padding: const EdgeInsets.symmetric(
                          horizontal: 16,
                          vertical: 8,
                        ),
                        decoration: BoxDecoration(
                          color: Colors.blue.withOpacity(0.1),
                          borderRadius: BorderRadius.circular(8),
                        ),
                        child: Text(
                          '$_currentPage / $_totalPages',
                          style: const TextStyle(
                            fontSize: 16,
                            fontWeight: FontWeight.bold,
                            color: Colors.blue,
                          ),
                        ),
                      ),
                    ],
                  ),

                  // 下一页按钮
                  ElevatedButton.icon(
                    onPressed: _hasMore && !_isLoading
                        ? () => _fetchPage(_currentPage + 1)
                        : null,
                    icon: const Icon(Icons.arrow_forward),
                    label: Text('next_page'.tr),
                    style: ElevatedButton.styleFrom(
                      backgroundColor: _hasMore ? Colors.blue : Colors.grey,
                    ),
                  ),
                ],
              ),
            ),
        ],
      ),
    );
  }
}
