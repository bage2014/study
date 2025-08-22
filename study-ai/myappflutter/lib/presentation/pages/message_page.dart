import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import '../../data/api/http_client.dart';
import '../../data/models/message_model.dart';
import '../../presentation/widgets/base_page.dart';

class MessagePage extends StatefulWidget {
  const MessagePage({super.key});

  @override
  State<MessagePage> createState() => _MessagePageState();
}

class _MessagePageState extends State<MessagePage> {
  final HttpClient _httpClient = HttpClient();
  final List<Message> _messages = [];
  int _currentPage = 1;
  final int _pageSize = 5;
  bool _isLoading = false;
  bool _hasMore = true;
  DateTime? _startDate;
  DateTime? _endDate;
  final ScrollController _scrollController = ScrollController();
  int _retryCount = 0;
  final int _maxRetries = 1;
  // 上拉加载阈值
  final double _loadMoreThreshold = 100.0;
  // 添加时间间隔限制 (毫秒)
  final int _minRefreshInterval = 1000; // 1秒
  // 上次请求时间
  DateTime? _lastRequestTime;

  @override
  void initState() {
    super.initState();
    _fetchMessages(refresh: true);
    _scrollController.addListener(_scrollListener);
  }

  Future<void> _fetchMessages({bool refresh = false}) async {
    // 检查时间间隔
    if (_lastRequestTime != null &&
        DateTime.now().difference(_lastRequestTime!).inMilliseconds <
            _minRefreshInterval) {
      return;
    }

    if (_isLoading) return;

    setState(() => _isLoading = true);
    // 记录请求时间
    _lastRequestTime = DateTime.now();

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
      // 从response中提取data字段，再获取messages数组
      // 直接解析messages数组
      final messages = List<Message>.from(
        response['data']['messages'].map((x) => Message.fromJson(x)),
      );
      // 如果需要分页信息，可以创建一个新的对象存储
      final pageResponse = MessageResponse(
        data: messages,
        // 可以添加其他分页相关字段
      );
      setState(() {
        if (refresh) {
          _messages.clear();
          _currentPage = 1;
        }
        // 避免添加重复数据
        final newMessages = pageResponse.data ?? [];
        if (newMessages.isNotEmpty) {
          _messages.addAll(newMessages);
        }
        _hasMore = (pageResponse.total ?? 0) > _messages.length;
        if (!refresh && _hasMore) {
          _currentPage++;
        }
        // 重置重试计数
        _retryCount = 0;
        LogUtil.info('消息总数: ${_messages.length}');
      });
    } catch (e) {
      LogUtil.error(e.toString(), error: e);
      // 重试机制
      if (_retryCount < _maxRetries) {
        _retryCount++;
        LogUtil.info('获取消息失败，正在重试($_retryCount/$_maxRetries)...');
        // 延迟重试
        Future.delayed(const Duration(milliseconds: 1000), () {
          _fetchMessages(refresh: refresh);
        });
      } else {
        Get.snackbar('error', '${'fetch_message_failed'}: ${e.toString()}');
        // 重置重试计数
        _retryCount = 0;
      }
    } finally {
      setState(() => _isLoading = false);
    }
  }

  // 添加发送消息的方法
  Future<void> _sendMessage(int receiverId, String content) async {
    try {
      // 调用发送消息接口
      Map<String, String>? queryParameters = {
        'receiverId': receiverId.toString(),
        'content': content,
      };
      final response = await _httpClient.get(
        '/message/send',
        queryParameters: queryParameters,
      );

      if (response['code'] == 200) {
        // 发送成功提示
        Get.snackbar('success', 'message_sent_successfully'.tr);
        // 刷新消息列表
        await _fetchMessages(refresh: true);
      } else {
        // 发送失败提示
        Get.snackbar('error', response['message'] ?? 'message_sent_failed'.tr);
      }
    } catch (e) {
      LogUtil.error('发送消息失败: $e');
      Get.snackbar('error', 'message_sent_failed'.tr);
    }
  }

  // 添加显示发送消息弹窗的方法
  void _showSendMessageDialog() {
    final TextEditingController receiverIdController = TextEditingController();
    final TextEditingController contentController = TextEditingController();

    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Text('send_message'.tr),
        content: SingleChildScrollView(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              // 移除接收者ID输入框
              TextField(
                controller: contentController,
                maxLines: 3,
                decoration: InputDecoration(
                  labelText: 'message_content'.tr,
                  hintText: 'enter_message_content'.tr,
                ),
              ),
            ],
          ),
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: Text('cancel'.tr),
          ),
          TextButton(
            onPressed: () async {
              final String content = contentController.text;

              if (content.isEmpty) {
                Get.snackbar('error', 'enter_message_content'.tr);
                return;
              }

              // 直接设置接收者ID为0，不再从输入获取
              final int receiverId = 0;

              // 关闭弹窗
              Navigator.pop(context);

              // 发送消息
              await _sendMessage(receiverId, content);
            },
            child: Text('send'.tr),
          ),
        ],
      ),
    );
  }

  // 实现加载更多指示器
  Widget _buildLoadMoreIndicator() {
    return Padding(
      padding: const EdgeInsets.all(16.0),
      child: Center(
        child: _isLoading
            ? Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  CircularProgressIndicator(),
                  const SizedBox(height: 8),
                  Text('loading_more'.tr),
                ],
              )
            : TextButton(
                onPressed: () => _fetchMessages(refresh: false),
                child: Text('load_more'.tr),
              ),
      ),
    );
  }

  void _scrollListener() {
    if (_scrollController.position.pixels >=
            _scrollController.position.maxScrollExtent - _loadMoreThreshold &&
        !_isLoading &&
        _hasMore) {
      _fetchMessages(refresh: false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return BasePage(
      title: 'message_center'.tr,
      actions: [
        // 添加发送消息按钮
        IconButton(
          icon: const Icon(Icons.send),
          onPressed: _showSendMessageDialog,
          tooltip: 'send_message'.tr,
        ),
      ],
      body: RefreshIndicator(
        onRefresh: () => _fetchMessages(refresh: true),
        child: ListView.builder(
          controller: _scrollController,
          padding: const EdgeInsets.fromLTRB(
            12.0,
            64.0,
            12.0,
            12.0,
          ), // 增加顶部内边距以预留appear位置
          itemCount: _messages.length + (_hasMore ? 1 : 0),
          itemBuilder: (context, index) {
            if (index == _messages.length) {
              return _buildLoadMoreIndicator();
            }

            final message = _messages[index];
            LogUtil.info('senderAvatar: ${message.senderAvatar}');
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
                        Row(
                          children: [
                            // 添加用户头像
                            CircleAvatar(
                              backgroundImage:
                                  message.senderAvatar != null &&
                                      message.senderAvatar!.isNotEmpty
                                  ? NetworkImage(message.senderAvatar!)
                                  : null,
                              radius: 20.0,
                              // 如果没有头像URL，显示默认图标
                              child:
                                  message.senderAvatar == null ||
                                      message.senderAvatar!.isEmpty
                                  ? const Icon(Icons.person)
                                  : null,
                            ),
                            const SizedBox(width: 12.0),
                            Text(
                              '${message.senderName}',
                              style: const TextStyle(
                                fontWeight: FontWeight.bold,
                                color: Colors.blue,
                              ),
                            ),
                          ],
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

  @override
  void dispose() {
    _scrollController.dispose();
    super.dispose();
  }
}
