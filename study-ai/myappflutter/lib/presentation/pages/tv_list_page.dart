import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import '../../data/models/tv_model.dart';
import '../../data/services/tv_service.dart';

class TvListPage extends StatefulWidget {
  const TvListPage({Key? key}) : super(key: key);

  @override
  State<TvListPage> createState() => _TvListPageState();
}

class _TvListPageState extends State<TvListPage> {
  final TvService _tvService = TvService();
  final TextEditingController _searchController = TextEditingController();
  TvResponse? _tvResponse;
  bool _isLoading = false;
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
      LogUtil.info('TvListPage = $response');
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
    if (_isLoading || _tvResponse == null || _tvResponse!.last == true) {
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
      setState(() {
        _currentPage = response.number ?? 0;
        if (_tvResponse!.content != null && response.content != null) {
          _tvResponse!.content!.addAll(response.content!);
        }
        // 恢复分页信息更新逻辑
        // _tvResponse!.last = response.last;
        // _tvResponse!.totalElements = response.totalElements;
        // _tvResponse!.totalPages = response.totalPages;
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
    // 跳转到TV播放页面，使用第一个可用的频道URL
    if (channel.channelUrls != null && channel.channelUrls!.isNotEmpty) {
      Get.toNamed('/tv_player', arguments: channel.channelUrls![0].url);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('TV 列表')),
      body: Column(
        children: [
          // 搜索框
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: TextField(
              controller: _searchController,
              decoration: InputDecoration(
                labelText: '搜索频道',
                suffixIcon: IconButton(
                  icon: const Icon(Icons.search),
                  onPressed: () => _searchTvChannels(_searchController.text),
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
              child: Text(_errorMessage, style: TextStyle(color: Colors.red)),
            ),

          // 加载指示器
          if (_isLoading && _tvResponse == null)
            const Expanded(child: Center(child: CircularProgressIndicator())),

          // 频道列表
          if (_tvResponse != null)
            Expanded(
              child:
                  _tvResponse!.content == null || _tvResponse!.content!.isEmpty
                  ? const Center(child: Text('没有找到频道'))
                  : ListView.builder(
                      itemCount: _tvResponse!.content!.length,
                      itemBuilder: (context, index) {
                        final channel = _tvResponse!.content![index];
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
                                    errorBuilder: (context, error, stackTrace) {
                                      return Icon(Icons.tv, size: 50);
                                    },
                                  )
                                : const Icon(Icons.tv, size: 50),
                            title: Text(channel.title ?? '未知频道'),
                            subtitle: Text(
                              '${channel.channelUrls?.length ?? 0} 个信号源',
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

          // 分页信息 - 添加空值检查
          if (_tvResponse != null && !_isLoading)
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Text(
                '第 ${_currentPage + 1}/${_tvResponse?.totalPages ?? 1} 页，共 ${_tvResponse?.totalElements ?? 0} 个频道',
                style: TextStyle(fontSize: 12, color: Colors.grey),
              ),
            ),

          // 加载更多按钮 - 添加空值检查
          if (_tvResponse != null &&
              !(_tvResponse?.last ?? true) &&
              !_isLoading)
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: ElevatedButton(
                onPressed: _loadMoreChannels,
                child: const Text('加载更多'),
              ),
            ),
        ],
      ),
    );
  }
}
