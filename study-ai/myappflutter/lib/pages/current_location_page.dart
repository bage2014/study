import 'package:flutter/material.dart';
import 'package:flutter_bmflocation/flutter_bmflocation.dart';
import 'dart:io';
import 'package:permission_handler/permission_handler.dart';

class CurrentLocationPage extends StatefulWidget {
  const CurrentLocationPage({super.key});

  @override
  State<CurrentLocationPage> createState() => _CurrentLocationPageState();
}

class _CurrentLocationPageState extends State<CurrentLocationPage> {
  BaiduLocation? _currentLocation;
  final LocationFlutterPlugin _myLocPlugin = LocationFlutterPlugin();
  bool _permissionDenied = false;

  @override
  void initState() {
    super.initState();
    _checkLocationPermission().then((granted) {
      if (granted) {
        _setupLocationCallbacks();
      } else {
        setState(() {
          _permissionDenied = true;
        });
      }
    });
  }

  Future<bool> _checkLocationPermission() async {
    var status = await Permission.location.status;
    if (status.isGranted) {
      return true;
    } else if (status.isDenied) {
      status = await Permission.location.request();
      return status.isGranted;
    } else if (status.isPermanentlyDenied) {
      openAppSettings();
      return false;
    }
    return false;
  }

  Future<void> _setupLocationCallbacks() async {
    // 设置定位参数
    final Map locationOption = {
      'isNeedAddress': true,
      'isNeedLocationDescribe': true,
      'isNeedNewVersionRgc': true,
      'locationTimeout': 20000,
      'reGeocodeTimeout': 20000,
      'coorType': 'bd09ll',
    };
    // Agree to privacy policy before setting up location
    _myLocPlugin.setAgreePrivacy(true);

    // Initialize location client before setting up callbacks
    // 启动定位
    if (Platform.isIOS) {
      _myLocPlugin.singleLocation(locationOption);
    } else {
      _myLocPlugin.startLocation();
    }

    if (Platform.isIOS) {
      _myLocPlugin.singleLocationCallback(
        callback: (BaiduLocation result) {
          print(
            '定位结果: errorCode=${result.errorCode}, errorInfo=${result.errorInfo}',
          );
          print(
            '定位结果: 纬度=${result.latitude}, 经度=${result.longitude}, 地址=${result.address}',
          );
          setState(() {
            _currentLocation = result;
          });
        },
      );
    } else if (Platform.isAndroid) {
      _myLocPlugin.seriesLocationCallback(
        callback: (BaiduLocation result) {
          print(
            '定位结果: errorCode=${result.errorCode}, errorInfo=${result.errorInfo}',
          );
          print(
            '定位结果: 纬度=${result.latitude}, 经度=${result.longitude}, 地址=${result.address}',
          );
          setState(() {
            _currentLocation = result;
          });
        },
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('当前位置')),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            if (_permissionDenied)
              Text('需要位置权限才能获取位置信息，请在设置中启用权限。')
            else if (_currentLocation != null)
              Column(
                children: [
                  Text('纬度: ${_currentLocation!.latitude}'),
                  Text('经度: ${_currentLocation!.longitude}'),
                  if (_currentLocation!.address != null)
                    Text('地址: ${_currentLocation!.address}'),
                ],
              )
            else
              const Text('正在获取位置...'),
          ],
        ),
      ),
    );
  }
}
