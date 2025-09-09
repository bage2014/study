import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:myappflutter/core/config/app_routes.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import '../../data/models/tv_model.dart';
import '../../data/services/tv_service.dart';
import '../widgets/base_page.dart'; // 引入BasePage组件

class TvListPage extends StatefulWidget {
  const TvListPage({Key? key}) : super(key: key);

  @override
  State<TvListPage> createState() => _TvListPageState();
}

class _TvListPageState extends State<TvListPage> {
  final TvService _tvService = TvService();
  final TextEditingController _searchController = TextEditingController();
  TvResponse? _tvResponse;
  bool _isLoading = true; // 初始化时设为true，表示正在加载
  bool _hasError = false;
  String _errorMessage = '';
  int _currentPage = 0;
  int _pageSize = 10;
  String _currentKeyword = '';

  @override
  void initState() {
    super.initState();
    // 初始加载数据
    _searchTvChannels('');
  }

  Future<void> _searchTvChannels(String keyword) async {
    LogUtil.info('TvListPage _searchTvChannels = $keyword');
    setState(() {
      _isLoading = true;
      _hasError = false;
      _currentPage = 0;
      _currentKeyword = keyword;
    });

    try {
      final response = await _tvService.searchTvChannels(
        keyword: keyword,
        page: 0,
        size: _pageSize,
      );
      setState(() {
        _tvResponse = response;
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

  Future<void> _loadMoreChannels() async {
    // 检查是否还有更多数据可加载（当前页面+1是否小于总页数）
    if (_isLoading ||
        _tvResponse == null ||
        _tvResponse?.data == null ||
        (_currentPage + 1) >= (_tvResponse?.data?.totalPages ?? 1)) {
      return;
    }

    setState(() {
      _isLoading = true;
    });

    try {
      final response = await _tvService.searchTvChannels(
        keyword: _currentKeyword,
        page: _currentPage + 1,
        size: _pageSize,
      );
      LogUtil.info('TvListPage _loadMoreChannels = $response');
      setState(() {
        // 更新当前页码为新响应的页码
        _currentPage = response.data?.currentPage ?? (_currentPage + 1);
        // 添加新数据到现有列表
        if (_tvResponse!.data?.channels != null &&
            response.data?.channels != null) {
          _tvResponse!.data!.channels!.addAll(response.data!.channels!);
        }
        // 更新分页信息
        if (response.data?.totalElements != null) {
          _tvResponse!.data!.totalElements = response.data!.totalElements;
        }
        if (response.data?.totalPages != null) {
          _tvResponse!.data!.totalPages = response.data!.totalPages;
        }
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

  void _onChannelTap(TvChannel channel) {
    // 跳转到TV播放页面，传递整个channel对象
    if (channel.channelUrls != null && channel.channelUrls!.isNotEmpty) {
      Get.toNamed(AppRoutes.TV_PLAYER, arguments: channel);
    }
  }

  @override
  Widget build(BuildContext context) {
    // 使用BasePage统一组件
    return BasePage(
      title: 'tv_list', // 使用翻译键
      body: _isLoading && _tvResponse == null
          ? const Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  CircularProgressIndicator(),
                  SizedBox(height: 16),
                  Text('loading_tv_channels'), // 加载提示文本
                ],
              ),
            )
          : Column(
              children: [
                // 搜索框
                Padding(
                  padding: const EdgeInsets.fromLTRB(8.0, 8.0, 8.0, 0),
                  child: TextField(
                    controller: _searchController,
                    decoration: InputDecoration(
                      labelText: 'search_channels'.tr, // 多语言
                      suffixIcon: IconButton(
                        icon: const Icon(Icons.search),
                        onPressed: () =>
                            _searchTvChannels(_searchController.text),
                      ),
                      border: const OutlineInputBorder(),
                    ),
                    onSubmitted: _searchTvChannels,
                  ),
                ),

                // 错误信息显示
                if (_hasError)
                  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Text(
                      _errorMessage,
                      style: TextStyle(color: Colors.red),
                    ),
                  ),

                // 频道列表
                if (!_isLoading &&
                    _tvResponse != null &&
                    _tvResponse!.data != null)
                  Expanded(
                    child:
                        _tvResponse!.data!.channels == null ||
                            _tvResponse!.data!.channels!.isEmpty
                        ? const Center(
                            child: Text('no_tv_channels_found'),
                          ) // 多语言
                        : ListView.builder(
                            itemCount: _tvResponse!.data!.channels!.length,
                            itemBuilder: (context, index) {
                              final channel =
                                  _tvResponse!.data!.channels![index];
                              return Card(
                                margin: const EdgeInsets.symmetric(
                                  horizontal: 8,
                                  vertical: 4,
                                ),
                                child: ListTile(
                                  leading: channel.logo != null
                                      ? Image.network(
                                          channel.logo!,
                                          width: 50,
                                          height: 50,
                                          fit: BoxFit.cover,
                                          errorBuilder:
                                              (context, error, stackTrace) {
                                                return Icon(Icons.tv, size: 50);
                                              },
                                        )
                                      : const Icon(Icons.tv, size: 50),
                                  title: Text(
                                    channel.title ?? 'unknown_channel'.tr,
                                  ), // 多语言
                                  subtitle: Text(
                                    '${channel.channelUrls?.length ?? 0} ${'signal_sources'.tr}', // 多语言
                                  ),
                                  onTap: () => _onChannelTap(channel),
                                ),
                              );
                            },
                          ),
                  ),

                // 分页加载指示器
                if (_isLoading && _tvResponse != null)
                  const Padding(
                    padding: EdgeInsets.all(8.0),
                    child: CircularProgressIndicator(),
                  ),

                // 分页信息
                if (!_isLoading &&
                    _tvResponse != null &&
                    _tvResponse!.data != null)
                  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Text(
                      '${'page'.tr} ${_currentPage + 1}/${_tvResponse?.data?.totalPages ?? 1}, ${'total'.tr} ${_tvResponse?.data?.totalElements ?? 0} ${'channels'.tr}', // 多语言
                      style: TextStyle(fontSize: 12, color: Colors.grey),
                    ),
                  ),

                // 加载更多按钮
                if (!_isLoading &&
                    _tvResponse != null &&
                    _tvResponse!.data != null &&
                    (_currentPage + 1) < (_tvResponse?.data?.totalPages ?? 1))
                  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: ElevatedButton(
                      onPressed: _loadMoreChannels,
                      child: Text('load_more'.tr), // 多语言
                    ),
                  ),
              ],
            ),
    );
  }
}
