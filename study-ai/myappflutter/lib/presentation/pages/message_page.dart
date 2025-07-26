import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import '../../data/api/http_client.dart';
import '../../data/models/message_model.dart';

class MessagePage extends StatefulWidget {
  const MessagePage({super.key});

  @override
  State<MessagePage> createState() => _MessagePageState();
}

class _MessagePageState extends State<MessagePage> {
  final HttpClient _httpClient = HttpClient();
  final List<Message> _messages = [];
  int _currentPage = 1;
  final int _pageSize = 10;
  bool _isLoading = false;
  bool _hasMore = true;
  DateTime? _startDate;
  DateTime? _endDate;
  final ScrollController _scrollController = ScrollController();

  @override
  void initState() {
    super.initState();
    _fetchMessages();
    _scrollController.addListener(_scrollListener);
  }

  Future<void> _fetchMessages({bool refresh = false}) async {
    if (_isLoading || !_hasMore) return;

    setState(() => _isLoading = true);

    try {
      final response = await _httpClient.post(
        '/message/query',
        body: {
          'page': refresh ? 1 : _currentPage,
          'size': _pageSize,
          if (_startDate != null) 'startTime': _startDate!.toIso8601String(),
          if (_endDate != null) 'endTime': _endDate!.toIso8601String(),
        },
      );
      final pageResponse = MessageResponse.fromJson(response);
      setState(() {
        if (refresh) {
          _messages.clear();
          _currentPage = 0;
          _hasMore = true;
        }
        _messages.addAll(pageResponse.data ?? []);
        _currentPage++;
        _hasMore = (pageResponse.total ?? 0) > _currentPage * _pageSize;
        LogUtil.info('length: ${_messages.length}');
      });
    } catch (e) {
      LogUtil.error(e.toString(), error: e);
      Get.snackbar('错误', '获取消息失败: ${e.toString()}');
    } finally {
      setState(() => _isLoading = false);
    }
  }

  void _scrollListener() {
    if (_scrollController.position.pixels ==
        _scrollController.position.maxScrollExtent) {
      _fetchMessages();
    }
  }

  Future<void> _refreshMessages() async {
    await _fetchMessages(refresh: true);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('消息中心')),
      body: RefreshIndicator(
        onRefresh: () => _fetchMessages(refresh: true),
        child: ListView.builder(
          padding: const EdgeInsets.all(12.0),
          itemCount: _messages.length + (_hasMore ? 1 : 0),
          itemBuilder: (context, index) {
            if (index == _messages.length) {
              return _buildLoadMoreIndicator();
            }

            final message = _messages[index];
            return Card(
              elevation: 3.0,
              margin: const EdgeInsets.symmetric(vertical: 8.0),
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(12.0),
              ),
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Text(
                          '发件人: ${message.senderId}',
                          style: const TextStyle(
                            fontWeight: FontWeight.bold,
                            color: Colors.blue,
                          ),
                        ),
                        Text(
                          message.createTime ?? '',
                          style: const TextStyle(
                            color: Colors.grey,
                            fontSize: 12.0,
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 8.0),
                    Text(
                      message.content ?? '',
                      style: const TextStyle(fontSize: 16.0),
                    ),
                  ],
                ),
              ),
            );
          },
        ),
      ),
    );
  }

  Widget _buildLoadMoreIndicator() {
    return _isLoading
        ? const Center(child: CircularProgressIndicator())
        : const SizedBox();
  }
}
