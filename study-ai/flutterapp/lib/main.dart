import 'package:flutter/material.dart';
import 'package:flutterapp/pages/login_page.dart';
import 'package:flutterapp/pages/profile_page.dart';

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
      home: const ProfilePage(username: 'hello'),
    
            // home: const LoginPage(),

    );
  }
}