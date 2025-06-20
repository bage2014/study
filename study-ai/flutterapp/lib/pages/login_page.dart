import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:flutterapp/pages/profile_page.dart';

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