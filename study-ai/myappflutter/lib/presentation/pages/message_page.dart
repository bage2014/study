import 'package:flutter/material.dart';
import 'package:get/get.dart';

class MessagePage extends StatefulWidget {
  const MessagePage({super.key});

  @override
  State<MessagePage> createState() => _MessagePageState();
}

class _MessagePageState extends State<MessagePage> {
  final List<Map<String, dynamic>> _messages = [];

  @override
  void initState() {
    super.initState();
    // TODO: 初始化消息监听
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('消息中心')),
      body: ListView.builder(
        itemCount: _messages.length,
        itemBuilder: (context, index) {
          final message = _messages[index];
          return ListTile(
            leading: const Icon(Icons.message),
            title: Text(message['title'] ?? '无标题'),
            subtitle: Text(message['content'] ?? '无内容'),
            trailing: Text(message['time'] ?? ''),
            onTap: () {
              // TODO: 处理消息点击
            },
          );
        },
      ),
    );
  }
}
