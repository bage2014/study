import 'package:flutter/material.dart';
import 'package:flutter_baidu_mapapi/map/map_flutter.dart';

class MapTrajectory extends StatefulWidget {
  @override
  _MapTrajectoryState createState() => _MapTrajectoryState();
}

class _MapTrajectoryState extends State<MapTrajectory> {
  BMFMapController? _controller;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('轨迹地图')),
      body: BMFMapWidget(
        onBMFMapCreated: (controller) {
          _controller = controller;
          _initLocation();
        },
      ),
    );
  }

  void _initLocation() {
    // 初始化定位
    // 待配置百度地图API Key后实现
  }
}
