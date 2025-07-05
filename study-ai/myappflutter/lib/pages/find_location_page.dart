import 'package:flutter/material.dart';
import 'package:flutter_bmflocation/flutter_bmflocation.dart';
import 'package:flutter_baidu_mapapi_base/flutter_baidu_mapapi_base.dart';
import 'package:flutter_baidu_mapapi_map/flutter_baidu_mapapi_map.dart';
import 'dart:io';
import 'package:permission_handler/permission_handler.dart';

class FindLocationPage extends StatefulWidget {
  const FindLocationPage({super.key});

  @override
  State<FindLocationPage> createState() => _FindLocationPageState();
}

class _FindLocationPageState extends State<FindLocationPage> {
  BaiduLocation? _currentLocation;
  final LocationFlutterPlugin _locationPlugin = LocationFlutterPlugin();
  bool _permissionDenied = false;
  BMFMapController? _mapController;
  BMFCoordinate? _centerCoordinate;
  bool _isFirstLocationReceived = false;

  @override
  void initState() {
    super.initState();
    // 初始化百度地图SDK
    BMFMapSDK.setAgreePrivacy(true);
    _checkLocationPermission().then((granted) {
      if (granted) {
        _setupLocationService();
      } else {
        setState(() => _permissionDenied = true);
      }
    });
  }

  // 权限检查
  Future<bool> _checkLocationPermission() async {
    var status = await Permission.location.status;
    if (status.isGranted) return true;
    if (status.isDenied) {
      status = await Permission.location.request();
      return status.isGranted;
    } else if (status.isPermanentlyDenied) {
      openAppSettings();
      return false;
    }
    return false;
  }

  // 初始化定位服务
  void _setupLocationService() {
    _locationPlugin.setAgreePrivacy(true);
    final Map<String, Object> locationOption = {
      'isNeedAddress': true,
      'isNeedLocationDescribe': true,
      'coorType': 'bd09ll', // 与地图SDK坐标系保持一致
      'locationTimeout': 20000,
      'scanSpan': 1000, // 连续定位间隔(ms)
    };

    // 启动定位
    if (Platform.isIOS) {
      _locationPlugin.singleLocation(locationOption);
    } else {
      _locationPlugin.startLocation();
    }

    // 设置定位回调
    if (Platform.isIOS) {
      _locationPlugin.singleLocationCallback(callback: _onLocationReceived);
    } else {
      _locationPlugin.seriesLocationCallback(callback: _onLocationReceived);
    }
  }

  // 地图初始化回调
  void _onMapCreated(BMFMapController controller) {
    _mapController = controller;
    _initLocationLayer();
  }

  // 初始化定位图层
  void _initLocationLayer() {
    // 1. 开启定位图层
    _mapController?.showUserLocation(true);

    // 2. 配置定位图层样式
    BMFUserLocationDisplayParam param = BMFUserLocationDisplayParam(
      accuracyCircleFillColor: Colors.blue.withOpacity(0.3),
      accuracyCircleStrokeColor: Colors.blue,
      enableDirection: true,
      locationViewImage: 'images/location_marker.png', // 自定义定位图标
    );
    // _mapController?.updateUserLocationDisplayParam(param);

    // 3. 设置定位模式为跟随
    _mapController?.setUserTrackingMode(BMFUserTrackingMode.Follow);
  }

  // 定位结果处理
  void _onLocationReceived(BaiduLocation result) {
    if (result.errorCode != 0) {
      print('定位错误: ${result.errorCode} - ${result.errorInfo}');
      return;
    }

    setState(() {
      _currentLocation = result;
      _centerCoordinate = BMFCoordinate(result.latitude!, result.longitude!);

      // 首次定位时移动地图
      if (!_isFirstLocationReceived) {
        _moveMapToCurrentLocation();
        _isFirstLocationReceived = true;
      }

      // 更新定位图层数据
      _updateLocationLayer(result);
    });
  }

  // 移动地图到当前位置
  void _moveMapToCurrentLocation() {
    // if (_mapController != null && _centerCoordinate != null) {
    //   BMFMapStatusUpdate update = BMFMapStatusUpdate.newLatLngZoom(
    //     _centerCoordinate!,
    //     18.0,
    //   );
    //   _mapController?.updateMapStatus(update);
    // }
  }

  // 更新定位图层数据
  void _updateLocationLayer(BaiduLocation location) {
    // 替换
    _centerCoordinate = BMFCoordinate(location.latitude!, location.longitude!);

    // 替换
    center:
    _centerCoordinate ?? BMFCoordinate(39.917215, 116.397507);
    // ... existing code ...
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('查找位置')),
      body: _buildBody(),
    );
  }

  Widget _buildBody() {
    if (_permissionDenied) {
      return const Center(child: Text('需要位置权限，请在设置中启用'));
    }

    // 地图初始化配置
    BMFMapOptions mapOptions = BMFMapOptions(
      center: _centerCoordinate ?? BMFCoordinate(39.917215, 116.397507),
      zoomLevel: 18,
      mapType: BMFMapType.Standard,
      maxZoomLevel: 20,
      minZoomLevel: 5,
    );

    return BMFMapWidget(
      onBMFMapCreated: _onMapCreated,
      mapOptions: mapOptions,
      // onBMFMapStatusChange: (status) {
      // 可选：处理地图状态变化
      // },
    );
  }

  @override
  void dispose() {
    _locationPlugin.stopLocation();
    // _mapController?.dispose();
    super.dispose();
  }
}
