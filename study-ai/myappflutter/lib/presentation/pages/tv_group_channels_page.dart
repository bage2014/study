import 'package:flutter/material.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import 'package:myappflutter/data/models/iptv_category_model.dart';
import 'package:myappflutter/data/services/iptv_service.dart';
import 'package:myappflutter/presentation/widgets/base_page.dart';
import 'tv_player_page.dart';
import '../../data/models/tv_model.dart';

class TVGroupChannelPage extends StatefulWidget {
  final String categoryName;

  const TVGroupChannelPage({Key? key, required this.categoryName})
    : super(key: key);

  @override
  State<TVGroupChannelPage> createState() => _TVGroupChannelPageState();
}

class _TVGroupChannelPageState extends State<TVGroupChannelPage> {
  final IptvService _iptvService = IptvService();
  List<IptvChannel> _channels = [];
  bool _isLoading = true;
  bool _hasError = false;
  String _errorMessage = '';

  // 添加分页相关状态
  int _currentPage = 0;
  int _pageSize = 20;
  bool _hasMorePages = true;
  bool _isLoadingMore = false;

  // 添加搜索相关状态
  final TextEditingController _searchController = TextEditingController();
  String _searchKeyword = '';
  bool _isSearching = false;

  @override
  void initState() {
    super.initState();
    _loadChannels();
  }

  Future<void> _loadChannels({bool isLoadMore = false}) async {
    // 如果是加载更多且没有更多页或已经在加载中，则返回
    if (isLoadMore && (!_hasMorePages || _isLoadingMore)) {
      return;
    }

    setState(() {
      if (isLoadMore) {
        _isLoadingMore = true;
      } else {
        _isLoading = true;
        _hasError = false;
        _isSearching = false; // 重置搜索状态
      }
    });

    try {
      final page = isLoadMore ? _currentPage + 1 : 0;
      final channels = await _iptvService.getChannelsByCategory(
        [widget.categoryName], // 使用搜索词作为标签
        page, // 传递分页参数
        _pageSize,
      );

      LogUtil.info('channels: ${channels.length}');

      setState(() {
        if (isLoadMore) {
          // 加载更多时追加数据
          _channels.addAll(channels);
          _currentPage = page;
          // 判断是否还有更多页（如果返回的数据少于pageSize，则认为没有更多数据）
          _hasMorePages = channels.length == _pageSize;
        } else {
          // 刷新时替换数据
          _channels = channels;
          _currentPage = 0;
          _hasMorePages = channels.length == _pageSize;
        }
        _isLoading = false;
        _isLoadingMore = false;
      });
    } catch (e) {
      setState(() {
        _isLoading = false;
        _isLoadingMore = false;
        if (!isLoadMore) {
          _hasError = true;
          _errorMessage = e.toString();
        } else {
          // 加载更多失败时显示错误提示
          ScaffoldMessenger.of(
            context,
          ).showSnackBar(SnackBar(content: Text('加载更多失败: $e')));
        }
      });
    }
  }

  // 实现搜索方法
  Future<void> _searchChannels(String keyword) async {
    if (keyword.isEmpty) {
      // 如果搜索词为空，重新加载原始分类的频道
      _loadChannels();
      return;
    }

    setState(() {
      _isLoading = true;
      _hasError = false;
      _isSearching = true;
      _searchKeyword = keyword;
    });

    try {
      final channels = await _iptvService.getChannelsByKeyword(
        widget.categoryName, // 使用分类名称作为标签
        keyword, // 使用搜索词作为标签
        0, // 搜索总是从第0页开始
        _pageSize,
      );

      LogUtil.info('search results: ${channels.length}');

      setState(() {
        _channels = channels;
        _currentPage = 0;
        _hasMorePages = channels.length == _pageSize;
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

  // 清除搜索
  void _clearSearch() {
    _searchController.clear();
    _loadChannels();
  }

  // 用于触发加载更多的滚动控制器
  final ScrollController _scrollController = ScrollController();

  @override
  void dispose() {
    _scrollController.dispose();
    _searchController.dispose();
    super.dispose();
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    // 添加滚动监听，实现滚动到底部自动加载更多
    _scrollController.addListener(() {
      if (_scrollController.position.pixels ==
          _scrollController.position.maxScrollExtent) {
        _loadChannels(isLoadMore: true);
      }
    });
  }

  void _onChannelTap(IptvChannel channel) {
    // 打印选中的频道信息
    print('Selected channel: ${channel.name} - ${channel.url}');

    // 将 IptvChannel 转换为 TvChannel
    TvChannel tvChannel = TvChannel(
      title: channel.name, // 使用 IptvChannel 的 name 作为 TvChannel 的 title
      logo: channel.logo, // 直接使用 logo
      channelUrls: [
        ChannelUrl(
          title: channel.name, // 频道名称作为URL标题
          url: channel.url, // 使用 IptvChannel 的 url
        ),
      ],
    );

    // 跳转到 TvPlayerPage 并传递转换后的 TvChannel 对象
    Navigator.push(
      context,
      MaterialPageRoute(builder: (context) => TvPlayerPage(channel: tvChannel)),
    );
  }

  Widget _buildChannelCard(IptvChannel channel) {
    return Card(
      margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      child: ListTile(
        leading: channel.logo.isNotEmpty
            ? Image.network(
                channel.logo,
                width: 50,
                height: 50,
                fit: BoxFit.cover,
                errorBuilder: (context, error, stackTrace) {
                  return const Icon(Icons.tv, size: 50);
                },
              )
            : const Icon(Icons.tv, size: 50),
        title: Text(channel.name),
        subtitle: Text(channel.group),
        onTap: () => _onChannelTap(channel),
        trailing: const Icon(Icons.arrow_forward_ios),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return BasePage(
      title: widget.categoryName,
      body: Column(
        children: [
          // 添加搜索框
          Padding(
            padding: const EdgeInsets.all(16.0),
            child: TextField(
              controller: _searchController,
              decoration: InputDecoration(
                labelText: '搜索频道',
                hintText: '输入标签进行搜索',
                prefixIcon: const Icon(Icons.search),
                suffixIcon: _searchKeyword.isNotEmpty
                    ? IconButton(
                        icon: const Icon(Icons.clear),
                        onPressed: _clearSearch,
                      )
                    : null,
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(8.0),
                ),
              ),
              onSubmitted: _searchChannels,
            ),
          ),
          // 搜索状态指示器
          if (_isSearching)
            Padding(
              padding: EdgeInsets.symmetric(vertical: 8.0),
              child: Text('搜索结果：基于标签 $_searchKeyword'),
            ),
          // 主要内容区域
          Expanded(
            child: _isLoading
                ? const Center(child: CircularProgressIndicator())
                : _hasError
                ? Center(
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Text('加载失败: $_errorMessage'),
                        ElevatedButton(
                          onPressed: _searchKeyword.isEmpty
                              ? _loadChannels
                              : () => _searchChannels(_searchKeyword),
                          child: const Text('重试'),
                        ),
                      ],
                    ),
                  )
                : _channels.isEmpty
                ? const Center(child: Text('没有找到频道'))
                : Stack(
                    children: [
                      ListView.builder(
                        controller: _scrollController, // 添加滚动控制器
                        itemCount: _channels.length,
                        itemBuilder: (context, index) {
                          return _buildChannelCard(_channels[index]);
                        },
                      ),
                      // 加载更多时显示指示器
                      if (_isLoadingMore)
                        const Positioned(
                          bottom: 16,
                          left: 0,
                          right: 0,
                          child: Center(child: CircularProgressIndicator()),
                        ),
                      // 手动加载更多按钮
                      if (!_isLoadingMore && _hasMorePages)
                        Positioned(
                          bottom: 16,
                          left: 0,
                          right: 0,
                          child: Center(
                            child: ElevatedButton(
                              onPressed: () => _loadChannels(isLoadMore: true),
                              child: const Text('加载更多'),
                            ),
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
