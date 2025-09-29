import 'package:flutter/material.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import 'package:myappflutter/data/models/iptv_category_model.dart';
import 'package:myappflutter/data/services/iptv_service.dart';
import 'package:myappflutter/presentation/widgets/base_page.dart';
import 'tv_player_page.dart';
import '../../data/models/tv_model.dart';

class CategoryChannelsPage extends StatefulWidget {
  final String categoryName;

  const CategoryChannelsPage({Key? key, required this.categoryName})
    : super(key: key);

  @override
  State<CategoryChannelsPage> createState() => _CategoryChannelsPageState();
}

class _CategoryChannelsPageState extends State<CategoryChannelsPage> {
  final IptvService _iptvService = IptvService();
  List<IptvChannel> _channels = [];
  bool _isLoading = true;
  bool _hasError = false;
  String _errorMessage = '';

  @override
  void initState() {
    super.initState();
    _loadChannels();
  }

  Future<void> _loadChannels() async {
    setState(() {
      _isLoading = true;
      _hasError = false;
    });

    try {
      final channels = await _iptvService.getChannelsByCategory(
        widget.categoryName,
      );
      LogUtil.info('channels: ${channels.length}');
      if (channels.length >= 20) {
        return;
      }
      setState(() {
        _channels = channels;
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
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : _hasError
          ? Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Text('加载失败: $_errorMessage'),
                  ElevatedButton(
                    onPressed: _loadChannels,
                    child: const Text('重试'),
                  ),
                ],
              ),
            )
          : _channels.isEmpty
          ? const Center(child: Text('没有找到频道'))
          : ListView.builder(
              itemCount: _channels.length,
              itemBuilder: (context, index) {
                return _buildChannelCard(_channels[index]);
              },
            ),
    );
  }
}
