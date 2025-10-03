import 'package:flutter/material.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import 'package:myappflutter/data/models/iptv_category_model.dart';
import 'package:myappflutter/data/services/iptv_service.dart';
import 'package:myappflutter/presentation/widgets/base_page.dart';

class LiveAllPage extends StatefulWidget {
  const LiveAllPage({Key? key}) : super(key: key);

  @override
  State<LiveAllPage> createState() => _LiveAllPageState();
}

class _LiveAllPageState extends State<LiveAllPage> {
  final IptvService _iptvService = IptvService();
  final TextEditingController _searchController = TextEditingController();
  List<IptvChannel> _allChannels = []; // 存储所有频道
  List<IptvChannel> _favoriteChannels = []; // 存储喜欢的频道
  bool _isLoading = true;
  bool _hasError = false;
  String _errorMessage = '';
  int _currentTab = 0; // 0: 喜欢, 1: 所有

  // 分页相关状态
  int _currentPage = 0;
  int _pageSize = 20;
  bool _hasMorePages = true;
  bool _isLoadingMore = false;
  String searchText = '';

  @override
  void initState() {
    super.initState();
    _loadFavoriteChannels();
  }

  // 加载所有频道
  Future<void> _loadAllChannels({bool isLoadMore = false}) async {
    if (_currentTab != 1) return; // 只有在所有标签下才加载

    if (isLoadMore && (!_hasMorePages || _isLoadingMore)) return;

    setState(() {
      if (isLoadMore) {
        _isLoadingMore = true;
      } else {
        _isLoading = true;
        _hasError = false;
        _allChannels = [];
        _currentPage = 0;
      }
    });

    try {
      final page = isLoadMore ? _currentPage + 1 : 0;

      // 使用空标签获取所有频道
      final channels = await _iptvService.getChannelsByKeyword(
        searchText,
        page,
        _pageSize,
      );

      setState(() {
        if (isLoadMore) {
          _allChannels.addAll(channels);
          _currentPage = page;
        } else {
          _allChannels = channels;
        }
        _hasMorePages = channels.length == _pageSize;
        _isLoading = false;
        _isLoadingMore = false;
      });
    } catch (e) {
      setState(() {
        _isLoading = false;
        _isLoadingMore = false;
        _hasError = true;
        _errorMessage = e.toString();
      });
    }
  }

  // 加载喜欢的频道
  Future<void> _loadFavoriteChannels({bool isLoadMore = false}) async {
    if (_currentTab != 0) return; // 只有在喜欢标签下才加载

    if (isLoadMore && (!_hasMorePages || _isLoadingMore)) return;

    setState(() {
      if (isLoadMore) {
        _isLoadingMore = true;
      } else {
        _isLoading = true;
        _hasError = false;
        _favoriteChannels = [];
        _currentPage = 0;
      }
    });

    try {
      final page = isLoadMore ? _currentPage + 1 : 0;
      final channels = await _iptvService.getFavoriteChannels(page, _pageSize);

      setState(() {
        if (isLoadMore) {
          _favoriteChannels.addAll(channels);
          _currentPage = page;
        } else {
          _favoriteChannels = channels;
        }
        _hasMorePages = channels.length == _pageSize;
        _isLoading = false;
        _isLoadingMore = false;
      });
    } catch (e) {
      setState(() {
        _isLoading = false;
        _isLoadingMore = false;
        _hasError = true;
        _errorMessage = e.toString();
      });
    }
  }

  void _onTabChanged(int index) {
    setState(() {
      _currentTab = index;
      _isLoading = true;
    });

    // 根据选择的标签加载对应数据
    switch (index) {
      case 0:
        _loadFavoriteChannels();
        break;
      case 1:
        _loadAllChannels();
        break;
    }
  }

  void _onChannelTap(IptvChannel channel) {
    // 这里可以添加跳转到播放页面的逻辑
    print('Selected channel: ${channel.name} - ${channel.url}');
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

    // 根据当前标签执行不同的搜索逻辑
    switch (_currentTab) {
      case 0: // 喜欢
        // 重新加载喜欢的频道并应用搜索
        _favoriteChannels = [];
        _loadFavoriteChannels();
        break;
      case 1: // 所有
        // 重新加载所有频道并应用搜索
        _allChannels = [];
        _loadAllChannels();
        break;
    }
  }

  Widget _buildContent() {
    // 根据当前选中的标签构建不同的内容
    Widget content;

    switch (_currentTab) {
      case 0: // 喜欢
        content = _favoriteChannels.isEmpty
            ? Container(
                color: Theme.of(context).scaffoldBackgroundColor, // 与主题背景色一致
                child: const Center(child: Text('暂无喜欢的频道')),
              )
            : NotificationListener<ScrollNotification>(
                onNotification: (scrollInfo) {
                  // 实现上拉加载更多
                  if (!_isLoadingMore &&
                      _hasMorePages &&
                      scrollInfo.metrics.pixels ==
                          scrollInfo.metrics.maxScrollExtent) {
                    _loadFavoriteChannels(isLoadMore: true);
                  }
                  return false;
                },
                child: ListView.builder(
                  itemCount:
                      _favoriteChannels.length + (_isLoadingMore ? 1 : 0),
                  itemBuilder: (context, index) {
                    if (index == _favoriteChannels.length) {
                      return const Center(child: CircularProgressIndicator());
                    }
                    return _buildChannelCard(_favoriteChannels[index]);
                  },
                ),
              );
        break;

      case 1: // 所有
        content = _allChannels.isEmpty
            ? Container(
                color: Theme.of(context).scaffoldBackgroundColor, // 与主题背景色一致
                child: const Center(child: Text('暂无频道数据')),
              )
            : NotificationListener<ScrollNotification>(
                onNotification: (scrollInfo) {
                  // 实现上拉加载更多
                  if (!_isLoadingMore &&
                      _hasMorePages &&
                      scrollInfo.metrics.pixels ==
                          scrollInfo.metrics.maxScrollExtent) {
                    _loadAllChannels(isLoadMore: true);
                  }
                  return false;
                },
                child: ListView.builder(
                  itemCount: _allChannels.length + (_isLoadingMore ? 1 : 0),
                  itemBuilder: (context, index) {
                    if (index == _allChannels.length) {
                      return const Center(child: CircularProgressIndicator());
                    }
                    return _buildChannelCard(_allChannels[index]);
                  },
                ),
              );
        break;

      default:
        content = Container(
          color: Theme.of(context).scaffoldBackgroundColor, // 与主题背景色一致
          child: const Center(child: Text('未知标签')),
        );
    }

    return content;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      // 设置Scaffold背景色与主题一致
      backgroundColor: Theme.of(context).scaffoldBackgroundColor,
      // 使用Scaffold作为根组件，确保bottomNavigationBar正常工作
      body: BasePage(
        title: '频道列表',
        body: _isLoading
            ? Container(
                color: Theme.of(context).scaffoldBackgroundColor, // 与主题背景色一致
                child: const Center(child: CircularProgressIndicator()),
              )
            : _hasError
            ? Container(
                color: Theme.of(context).scaffoldBackgroundColor, // 与主题背景色一致
                child: Center(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      const Icon(Icons.error, size: 64, color: Colors.red),
                      const SizedBox(height: 16),
                      Text('加载失败: $_errorMessage'),
                      const SizedBox(height: 16),
                      ElevatedButton(
                        onPressed: () {
                          if (_currentTab == 0) {
                            _loadFavoriteChannels();
                          } else {
                            _loadAllChannels();
                          }
                        },
                        child: const Text('重试'),
                      ),
                    ],
                  ),
                ),
              )
            : Column(
                children: [
                  // 搜索框
                  Container(
                    color: Theme.of(context).scaffoldBackgroundColor, // 与主题背景色一致
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
                  // 内容区域，使用Expanded确保可以占据剩余空间
                  Expanded(child: _buildContent()),
                ],
              ),
      ),
      // 底部导航栏固定在底部
      bottomNavigationBar: BottomNavigationBar(
        currentIndex: _currentTab,
        onTap: _onTabChanged,
        items: const [
          BottomNavigationBarItem(icon: Icon(Icons.favorite), label: '喜欢'),
          BottomNavigationBarItem(icon: Icon(Icons.list), label: '所有'),
        ],
      ),
    );
  }
}
