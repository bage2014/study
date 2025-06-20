import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter App',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const LoginPage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key, required this.title}) : super(key: key);

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;

  void _incrementCounter() {
    setState(() {
      _counter++;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[ 
            const Text(
              'You have pushed the button this many times:',
            ),
            Text(
              '$_counter',
              style: ThemeData.light().textTheme.headlineLarge,
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: 'Increment',
        child: const Icon(Icons.add),
      ),
    );
  }
}

class LoginPage extends StatefulWidget {
  const LoginPage({Key? key}) : super(key: key);

  @override
  _LoginPageState createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final TextEditingController _usernameController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('登录页面'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[ 
            TextField(
              controller: _usernameController,
              decoration: const InputDecoration(labelText: '用户名'),
            ),
            TextField(
              controller: _passwordController,
              decoration: const InputDecoration(labelText: '密码'),
              obscureText: true,
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: () async {
                Navigator.pushReplacement(
                    context,
                    MaterialPageRoute(
                      builder: (context) => ProfilePage(username: _usernameController.text),
                    ),
                  );
                // var response = await http.post(
                //   Uri.parse('http://localhost:8080/user/login'),
                //   body: {
                //     'username': _usernameController.text,
                //     'password': _passwordController.text,
                //   },
                // );
                // if (response.statusCode == 200) {
                //   Navigator.pushReplacement(
                //     context,
                //     MaterialPageRoute(
                //       builder: (context) => ProfilePage(username: _usernameController.text),
                //     ),
                //   );
                // } else {
                //   print('登录失败');
                // }
              },
              child: const Text('登录'),
            ),
          ],
        ),
      ),
    );
  }
}

class ProfilePage extends StatefulWidget {
  final String username;

  const ProfilePage({Key? key, required this.username}) : super(key: key);

  @override
  _ProfilePageState createState() => _ProfilePageState();
}

class _ProfilePageState extends State<ProfilePage> {
  List<dynamic> _posts = [];
  int _page = 1;
  bool _isLoading = false;
  final String avatarUrl = 'https://example.com/default_avatar.png'; // 示例头像 URL
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

    final response = await http.get(Uri.parse('http://localhost:8080/user/activities?page=$_page'));

    if (response.statusCode == 200) {
      final List<dynamic> newPosts = json.decode(response.body);
      setState(() {
        _posts.addAll(newPosts);
        _page++;
      });
    } else {
      throw Exception('Failed to load posts');
    }

    setState(() {
      _isLoading = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('个人信息主页面'),
      ),
      body: SingleChildScrollView(
        child: Column(
          children: <Widget>[ 
            // 个人头像
            Padding(
              padding: const EdgeInsets.all(16.0),
              child: CircleAvatar(
                radius: 50,
                backgroundImage: NetworkImage(avatarUrl),
                // 删除 errorBuilder
              ),
            ),
            // 用户名
            Text(
              widget.username,
              style: Theme.of(context).textTheme.headlineMedium,
            ),
            // 个性签名
            Padding(
              padding: const EdgeInsets.all(16.0),
              child: Text(
                signature,
                style: Theme.of(context).textTheme.bodyLarge,
              ),
            ),
            // 最近动态标题
            Padding(
              padding: const EdgeInsets.symmetric(vertical: 16.0),
              child: Text(
                '最近动态',
                style: Theme.of(context).textTheme.titleLarge,
              ),
            ),
            // 帖子列表
            ListView.builder(
              controller: _scrollController,
              shrinkWrap: true,
              physics: const NeverScrollableScrollPhysics(),
              itemCount: _posts.length + (_isLoading ? 1 : 0),
              itemBuilder: (context, index) {
                if (index < _posts.length) {
                  return Card(
                    margin: const EdgeInsets.symmetric(vertical: 8.0, horizontal: 16.0),
                    child: Padding(
                      padding: const EdgeInsets.all(16.0),
                      child: Text(_posts[index]['content']),
                    ),
                  );
                } else {
                  return const Center(
                    child: CircularProgressIndicator(),
                  );
                }
              },
            ),
            // 移除加载更多按钮
          ],
        ),
      ),
    );
  }
}