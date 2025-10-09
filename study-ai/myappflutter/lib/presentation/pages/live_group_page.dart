import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:myappflutter/core/config/app_routes.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import 'package:myappflutter/data/models/iptv_category_model.dart';
import 'package:myappflutter/data/services/iptv_service.dart';
import 'package:myappflutter/presentation/widgets/base_page.dart';
import 'package:myappflutter/presentation/pages/tv_group_channels_page.dart';

class LiveGroupPage extends StatefulWidget {
  const LiveGroupPage({Key? key}) : super(key: key);

  @override
  State<LiveGroupPage> createState() => _LiveGroupPageState();
}

class _LiveGroupPageState extends State<LiveGroupPage> {
  final IptvService _iptvService = IptvService();
  final TextEditingController _searchController = TextEditingController();
  IptvCategoryResponse? _categoryResponse;
  bool _isLoading = true;
  bool _hasError = false;
  String _errorMessage = '';
  String searchText = '';

  @override
  void initState() {
    super.initState();
    _loadCategories();
  }

  Future<void> _loadCategories() async {
    setState(() {
      _isLoading = true;
      _hasError = false;
    });

    try {
      List<String> tags = [];
      if (searchText.isNotEmpty) {
        tags.add(searchText);
      }
      final response = await _iptvService.getCategories(tags: tags);
      setState(() {
        _categoryResponse = response;
        _isLoading = false;
      });
    } catch (e) {
      setState(() {
        _isLoading = false;
        _hasError = true;
        _errorMessage = e.toString();
      });
    }
  }

  void _onChannelTap(IptvChannel channel) {
    // 这里可以添加跳转到播放页面的逻辑
    print('Selected channel: ${channel.name} - ${channel.url}');
  }

  void _onCategoryTap(String categoryName) {
    Get.toNamed(
      AppRoutes.LIVE_ALL,
      arguments: {'categoryName': categoryName},
    );
  }

  Widget _buildCategorySection(
    String categoryName,
    List<IptvChannel> channels,
  ) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        GestureDetector(
          onTap: () => _onCategoryTap(categoryName),
          child: Padding(
            padding: const EdgeInsets.symmetric(vertical: 16, horizontal: 16),
            child: Row(
              children: [
                Text(
                  categoryName,
                  style: const TextStyle(
                    fontSize: 20,
                    fontWeight: FontWeight.bold,
                    color: Colors.blue,
                  ),
                ),
                const SizedBox(width: 8),
                const Icon(
                  Icons.arrow_forward_ios,
                  size: 16,
                  color: Colors.blue,
                ),
              ],
            ),
          ),
        ),
        ...channels.map((channel) => _buildChannelCard(channel)).toList(),
      ],
    );
  }

  Widget _buildChannelCard(IptvChannel channel) {
    return Card(
      margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      color: Theme.of(context).cardTheme.color ?? Theme.of(context).cardColor,
      elevation: Theme.of(context).cardTheme.elevation ?? 2,
      child: ListTile(
        leading: channel.logo.isNotEmpty
            ? Image.network(
                channel.logo,
                width: 50,
                height: 50,
                fit: BoxFit.cover,
                errorBuilder: (context, error, stackTrace) {
                  return const Icon(Icons.live_tv, size: 50);
                },
              )
            : const Icon(Icons.live_tv, size: 50),
        title: Text(channel.name),
        subtitle: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text('分组: ${channel.group}'),
            Text('语言: ${channel.language}'),
            Text('国家: ${channel.country}'),
          ],
        ),
        trailing: const Icon(Icons.play_arrow),
        onTap: () => _onChannelTap(channel),
      ),
    );
  }

  // 搜索方法
  void _performSearch() {
    searchText = _searchController.text.trim();
    LogUtil.info('searchText: $searchText');

    setState(() {
      _isLoading = true;
      _hasError = false;
    });

    _loadCategories();
  }

  @override
  Widget build(BuildContext context) {
    return BasePage(
      title: '频道分组',
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : _hasError
          ? Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const Icon(Icons.error, size: 64, color: Colors.red),
                  const SizedBox(height: 16),
                  Text('加载失败: $_errorMessage'),
                  const SizedBox(height: 16),
                  ElevatedButton(
                    onPressed: _loadCategories,
                    child: const Text('重试'),
                  ),
                ],
              ),
            )
          : Column(
              children: [
                // 搜索框
                Padding(
                  padding: const EdgeInsets.all(16),
                  child: TextField(
                    controller: _searchController,
                    decoration: InputDecoration(
                      labelText: '搜索频道',
                      suffixIcon: IconButton(
                        icon: const Icon(Icons.search),
                        onPressed: _performSearch,
                      ),
                      border: const OutlineInputBorder(),
                    ),
                  ),
                ),
                Expanded(
                  child:
                      _categoryResponse == null ||
                          _categoryResponse!.categories.isEmpty
                      ? const Center(child: Text('暂无频道数据'))
                      : ListView.builder(
                          shrinkWrap: true,
                          physics: const AlwaysScrollableScrollPhysics(),
                          itemCount:
                              _categoryResponse!.categories.entries.length,
                          itemBuilder: (context, index) {
                            final entry = _categoryResponse!.categories.entries
                                .elementAt(index);
                            return _buildCategorySection(
                              entry.key,
                              entry.value,
                            );
                          },
                        ),
                ),
              ],
            ),
    );
  }
}
