import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:myappflutter/core/config/app_routes.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import 'package:myappflutter/data/api/http_client.dart';
import 'package:myappflutter/presentation/widgets/base_page.dart';

class AiPage extends StatefulWidget {
  const AiPage({super.key});

  @override
  State<AiPage> createState() => _AiPageState();
}

class _AiPageState extends State<AiPage> {
  final TextEditingController _inputController = TextEditingController();
  final HttpClient _httpClient = HttpClient();
  final ScrollController _scrollController = ScrollController();

  bool _isLoading = false;
  String _responseText = '';
  List<Map<String, String>> _conversationHistory = [];

  Future<void> _sendMessage() async {
    if (_inputController.text.trim().isEmpty) return;

    setState(() {
      _isLoading = true;
      _conversationHistory.add({
        'role': 'user',
        'content': _inputController.text.trim(),
      });
    });

    final String userMessage = _inputController.text.trim();
    _inputController.clear();

    try {
      // 构建请求数据
      final Map<String, dynamic> requestData = {
        'model': 'deepseek-chat',
        'messages': [
          {'role': 'user', 'content': userMessage},
        ],
        'stream': false,
      };

      // 发送请求到DeepSeek API
      final response = await _httpClient.post(
        'https://api.deepseek.com/v1/chat/completions',
        body: requestData,
        headers: {
          'Authorization': 'Bearer ', // 请替换为实际的API密钥
          'Content-Type': 'application/json',
        },
      );

      if (response['choices'] != null && response['choices'].isNotEmpty) {
        final String aiResponse = response['choices'][0]['message']['content'];

        setState(() {
          _conversationHistory.add({
            'role': 'assistant',
            'content': aiResponse,
          });
          _responseText = aiResponse;
        });

        // 滚动到底部
        WidgetsBinding.instance.addPostFrameCallback((_) {
          _scrollController.animateTo(
            _scrollController.position.maxScrollExtent,
            duration: const Duration(milliseconds: 300),
            curve: Curves.easeOut,
          );
        });
      } else {
        throw Exception('API响应格式错误');
      }
    } catch (e) {
      LogUtil.error('DeepSeek API调用失败: $e');
      Get.snackbar('错误', '请求失败，请检查网络连接和API密钥');

      setState(() {
        _conversationHistory.add({
          'role': 'assistant',
          'content': '抱歉，请求失败，请稍后重试。',
        });
      });
    } finally {
      setState(() {
        _isLoading = false;
      });
    }
  }

  Widget _buildMessageBubble(String content, bool isUser) {
    return Container(
      margin: const EdgeInsets.symmetric(vertical: 4.0),
      child: Row(
        mainAxisAlignment: isUser
            ? MainAxisAlignment.end
            : MainAxisAlignment.start,
        children: [
          if (!isUser)
            const CircleAvatar(
              backgroundColor: Colors.blue,
              child: Icon(Icons.smart_toy, color: Colors.white, size: 16),
            ),
          const SizedBox(width: 8),
          Flexible(
            child: Container(
              padding: const EdgeInsets.all(12.0),
              decoration: BoxDecoration(
                color: isUser ? Colors.blue.shade100 : Colors.grey.shade100,
                borderRadius: BorderRadius.circular(12.0),
              ),
              child: Text(
                content,
                style: TextStyle(
                  color: isUser ? Colors.blue.shade900 : Colors.black87,
                ),
              ),
            ),
          ),
          const SizedBox(width: 8),
          if (isUser)
            const CircleAvatar(
              backgroundColor: Colors.green,
              child: Icon(Icons.person, color: Colors.white, size: 16),
            ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return BasePage(
      title: 'AI助手',
      body: Column(
        children: [
          // 对话历史区域
          Expanded(
            child: Container(
              padding: const EdgeInsets.all(16.0),
              child: _conversationHistory.isEmpty
                  ? const Center(
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Icon(Icons.smart_toy, size: 64, color: Colors.grey),
                          SizedBox(height: 16),
                          Text(
                            '欢迎使用AI助手！\n请输入您的问题开始对话',
                            textAlign: TextAlign.center,
                            style: TextStyle(fontSize: 16, color: Colors.grey),
                          ),
                        ],
                      ),
                    )
                  : ListView.builder(
                      controller: _scrollController,
                      itemCount: _conversationHistory.length,
                      itemBuilder: (context, index) {
                        final message = _conversationHistory[index];
                        return _buildMessageBubble(
                          message['content']!,
                          message['role'] == 'user',
                        );
                      },
                    ),
            ),
          ),

          // 输入区域
          Container(
            padding: const EdgeInsets.all(16.0),
            decoration: BoxDecoration(
              color: Colors.grey.shade50,
              border: Border(top: BorderSide(color: Colors.grey.shade300)),
            ),
            child: Row(
              children: [
                Expanded(
                  child: TextField(
                    controller: _inputController,
                    decoration: InputDecoration(
                      hintText: '请输入您的问题...',
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(24.0),
                      ),
                      contentPadding: const EdgeInsets.symmetric(
                        horizontal: 16.0,
                        vertical: 12.0,
                      ),
                    ),
                    maxLines: null,
                    onSubmitted: (_) => _sendMessage(),
                  ),
                ),
                const SizedBox(width: 8.0),
                _isLoading
                    ? const CircularProgressIndicator()
                    : IconButton(
                        icon: const Icon(Icons.send, color: Colors.blue),
                        onPressed: _sendMessage,
                      ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  @override
  void dispose() {
    _inputController.dispose();
    _scrollController.dispose();
    super.dispose();
  }
}
