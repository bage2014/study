import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:flutterapp/utils/logging_client.dart';

class ProfilePage extends StatefulWidget {
  final String username;

  const ProfilePage({Key? key, required this.username}) : super(key: key);

  @override
  _ProfilePageState createState() => _ProfilePageState();
}

class Activity {
  final int? id;
  final String time;
  final String description;
  final String creator;

  Activity({
    this.id,
    required this.time,
    required this.description,
    required this.creator,
  });

  factory Activity.fromJson(Map<String, dynamic> json) {
    return Activity(
      id: json['id'] as int?,
      time: json['time'] as String,
      description: json['description'] as String,
      creator: json['creator'] as String,
    );
  }
}

// Remove the original LoggingClient class definition
class _ProfilePageState extends State<ProfilePage> {
  List<Activity> _posts = [];
  int _page = 1;
  bool _isLoading = false;
  final String avatarUrl = 'assets/images/default_avatar.png'; // 修改为本地图片路径
  final String signature = '这个人很懒，什么都没留下'; // 示例个性签名
  late ScrollController _scrollController;

  @override
  void initState() {
    super.initState();
    _fetchPosts();
    _scrollController = ScrollController();
    _scrollController.addListener(_scrollListener);
  }

  @override
  void dispose() {
    _scrollController.dispose();
    super.dispose();
  }

  void _scrollListener() {
    if (_scrollController.position.pixels == _scrollController.position.maxScrollExtent) {
      _fetchPosts();
    }
  }

  Future<void> _fetchPosts() async {
    if (_isLoading) return;

    setState(() {
      _isLoading = true;
    });

    try {
      final client = LoggingClient(http.Client());
      final response = await client.get(Uri.parse('http://127.0.0.1:8080/activities?page=$_page'));

      if (response.statusCode == 200) {
        final Map<String, dynamic> responseData = json.decode(response.body);
        final List<dynamic> newPostsJson = responseData['content'];
        final List<Activity> newPosts = newPostsJson.map((json) => Activity.fromJson(json)).toList();

        setState(() {
          _posts.addAll(newPosts);
          _page = responseData['pageable']['pageNumber'] + 1;
        });
      } else {
        throw Exception('Failed to load posts');
      }
    } catch (e) {
      print('Fetch posts error: $e');
    } finally {
      setState(() {
        _isLoading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('个人信息主页面'),
      ),
      body: ListView.builder(
        controller: _scrollController,
        itemCount: _posts.length + (_isLoading ? 1 : 0),
        itemBuilder: (context, index) {
          if (index == 0) {
            return Column(
              children: <Widget>[ 
                Text(
                  widget.username,
                  style: Theme.of(context).textTheme.headlineMedium,
                ),
                Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: Text(
                    signature,
                    style: Theme.of(context).textTheme.bodyLarge,
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.symmetric(vertical: 16.0),
                  child: Text(
                    '最近动态',
                    style: Theme.of(context).textTheme.titleLarge,
                  ),
                ),
              ],
            );
          }
          index -= 1;
          if (index < _posts.length) {
            final Activity post = _posts[index];
            return Card(
              margin: const EdgeInsets.symmetric(vertical: 8.0, horizontal: 16.0),
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text('时间: ${post.time}', style: Theme.of(context).textTheme.titleMedium),
                    Text('描述: ${post.description}', style: Theme.of(context).textTheme.bodyMedium),
                    Text('创建人: ${post.creator}', style: Theme.of(context).textTheme.bodySmall),
                  ],
                ),
              ),
            );
          } else {
            return const Center(
              child: CircularProgressIndicator(),
            );
          }
        },
      ),
    );
  }
}