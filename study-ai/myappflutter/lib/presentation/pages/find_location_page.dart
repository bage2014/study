import 'package:flutter/material.dart';
import 'package:flutter_bmflocation/flutter_bmflocation.dart';
import 'package:flutter_baidu_mapapi_map/flutter_baidu_mapapi_map.dart';
import 'package:flutter_baidu_mapapi_base/flutter_baidu_mapapi_base.dart';
import 'dart:io';
import 'package:permission_handler/permission_handler.dart';
import '../widgets/base_page.dart';
import 'package:myappflutter/core/utils/log_util.dart';

class FindLocationPage extends StatefulWidget {
  const FindLocationPage({super.key});

  @override
  State<FindLocationPage> createState() => _FindLocationPageState();
}

class _FindLocationPageState extends State<FindLocationPage> {
  BaiduLocation? _currentLocation;
  final LocationFlutterPlugin _myLocPlugin = LocationFlutterPlugin();
  bool _permissionDenied = false;
  BMFMapController? _mapController;

  @override
  void initState() {
    super.initState();
    _checkLocationPermission().then((granted) {
      if (granted) {
        _setupLocationCallbacks();
      } else {
        setState(() => _permissionDenied = true);
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
    final Map locationOption = {
      'isNeedAddress': true,
      'isNeedLocationDescribe': true,
      'isNeedNewVersionRgc': true,
      'locationTimeout': 20000,
      'reGeocodeTimeout': 20000,
      'coorType': 'bd09ll',
    };

    _myLocPlugin.setAgreePrivacy(true);

    // 4. 启动定位
    if (Platform.isIOS) {
      _myLocPlugin.singleLocation(locationOption);
    } else {
      _myLocPlugin.startLocation();
    }

    if (Platform.isIOS) {
      _myLocPlugin.singleLocationCallback(
        callback: (BaiduLocation result) {
          _updateLocation(result);
        },
      );
      _myLocPlugin.singleLocation(locationOption);
    } else {
      _myLocPlugin.seriesLocationCallback(
        callback: (BaiduLocation result) {
          _updateLocation(result);
        },
      );
      _myLocPlugin.startLocation();
    }
  }

  void _updateLocation(BaiduLocation result) {
    LogUtil.info('定位结果: 纬度=\${result.latitude}, 经度=\${result.longitude}');
    setState(() {
      _currentLocation = result;
    });

    // 更新地图中心点
    if (_mapController != null &&
        result.latitude != null &&
        result.longitude != null) {
      _mapController?.setCenterCoordinate(
        BMFCoordinate(result.latitude!, result.longitude!),
        true,
      );

      // 添加标记点
      _mapController?.addMarker(
        BMFMarker(
          position: BMFCoordinate(result.latitude!, result.longitude!),
          title: '当前位置',
        ),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return BasePage(
      title: 'find_location',
      body: _permissionDenied
          ? const Center(child: Text('需要位置权限才能使用此功能'))
          : Column(
              children: [
                Expanded(
                  child: BMFMapWidget(
                    onBMFMapCreated: (controller) {
                      _mapController = controller;
                      if (_currentLocation != null) {
                        controller.setCenterCoordinate(
                          BMFCoordinate(
                            _currentLocation!.latitude!,
                            _currentLocation!.longitude!,
                          ),
                          true,
                        );
                      }
                    },
                    mapOptions: BMFMapOptions(
                      center: BMFCoordinate(39.915, 116.404),
                      zoomLevel: 15,
                    ),
                  ),
                ),
                if (_currentLocation != null)
                  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Text('当前位置: ${_currentLocation!.address}'),
                  ),
              ],
            ),
    );
  }

  @override
  void dispose() {
    _myLocPlugin.stopLocation();
    super.dispose();
  }
}
