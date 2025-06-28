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
                final client = LoggingClient(http.Client());
                var response = await client.post(
                  Uri.parse('http://localhost:8080/user/login'),
                  body: {
                    'username': _usernameController.text,
                    'password': _passwordController.text,
                  },
                );
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

// Remove the original LoggingClient class definition
class LoggingClient extends http.BaseClient {
  final http.Client _inner;

  LoggingClient(this._inner);

  @override
  Future<http.StreamedResponse> send(http.BaseRequest request) async {
    print('Request: ${request.method} ${request.url}');
    print('Headers: ${request.headers}');
    if (request is http.Request) {
      print('Body: ${request.body}');
    }

    final response = await _inner.send(request);

    final http.Response decodedResponse = await http.Response.fromStream(response);
    final responseBody = utf8.decode(decodedResponse.bodyBytes);
    print('Response status: ${response.statusCode}');
    print('Response body: $responseBody');

    return http.StreamedResponse(Stream.fromIterable([utf8.encode(responseBody)]), response.statusCode, headers: response.headers);
  }
}