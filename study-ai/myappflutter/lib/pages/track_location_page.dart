import 'package:flutter/material.dart';

class TrackLocationPage extends StatefulWidget {
  const TrackLocationPage({super.key});

  @override
  State<TrackLocationPage> createState() => _TrackLocationPageState();
}

class _TrackLocationPageState extends State<TrackLocationPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('位置轨迹'),
        backgroundColor: Colors.blue,
      ),
      body: const Center(
        child: Text('地图功能已移除'),
      ),
    );
  }
}
