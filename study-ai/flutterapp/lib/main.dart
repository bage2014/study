import 'package:flutter/material.dart';
import 'package:newflutterapp/pages/map_trajectory_page.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      home: const MapTrajectoryPage(),
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),

    );
  }
}