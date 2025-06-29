import 'package:flutter/material.dart';

import 'package:flutterapp/pages/login_page.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      initialRoute: '/',
      routes: {
        '/': (context) =>
            const Scaffold(body: Center(child: Text('Home Page'))),
        '/baidu_map': (context) => const LoginPage(),
      },
      theme: ThemeData(primarySwatch: Colors.blue),
    );
  }
}
