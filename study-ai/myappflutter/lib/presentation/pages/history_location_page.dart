import 'package:flutter/material.dart';
import 'package:flutter_bmflocation/flutter_bmflocation.dart';
import 'package:flutter_baidu_mapapi_map/flutter_baidu_mapapi_map.dart';
import 'package:flutter_baidu_mapapi_base/flutter_baidu_mapapi_base.dart';

import 'dart:io';
import 'package:permission_handler/permission_handler.dart';
import '../widgets/base_page.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import 'package:myappflutter/data/api/http_client.dart';
import 'package:intl/intl.dart';

// 轨迹数据模型
class TrajectoryPoint {
  final int id;
  final double latitude;
  final double longitude;
  final String time;
  final String address;

  TrajectoryPoint({
    required this.id,
    required this.latitude,
    required this.longitude,
    required this.time,
    required this.address,
  });

  factory TrajectoryPoint.fromJson(Map<String, dynamic> json) {
    return TrajectoryPoint(
      id: json['id'] ?? 0,
      latitude: double.tryParse(json['latitude'].toString()) ?? 0.0,
      longitude: double.tryParse(json['longitude'].toString()) ?? 0.0,
      time: json['time'] ?? '',
      address: json['address'] ?? '',
    );
  }
}

// 分页数据模型 - 适配新的数据格式
class PageResponse {
  final List<TrajectoryPoint> trajectories; // 修改字段名
  final int totalElements;
  final int totalPages;
  final int currentPage; // 修改字段名
  final int pageSize; // 修改字段名

  PageResponse({
    required this.trajectories,
    required this.totalElements,
    required this.totalPages,
    required this.currentPage,
    required this.pageSize,
  });

  factory PageResponse.fromJson(Map<String, dynamic> json) {
    return PageResponse(
      trajectories:
          (json['trajectories'] as List) // 修改字段名
              .map((item) => TrajectoryPoint.fromJson(item))
              .toList(),
      totalElements: json['totalElements'] ?? 0,
      totalPages: json['totalPages'] ?? 0,
      currentPage: json['currentPage'] ?? 0, // 修改字段名
      pageSize: json['pageSize'] ?? 0, // 修改字段名
    );
  }
}

// API响应模型
class ApiResponse {
  final int code;
  final String message;
  final PageResponse data;

  ApiResponse({required this.code, required this.message, required this.data});

  factory ApiResponse.fromJson(Map<String, dynamic> json) {
    return ApiResponse(
      code: json['code'] ?? 0,
      message: json['message'] ?? '',
      data: PageResponse.fromJson(json['data'] ?? {}),
    );
  }
}

class HistoryLocationPage extends StatefulWidget {
  const HistoryLocationPage({super.key});

  @override
  State<HistoryLocationPage> createState() => _HistoryLocationPageState();
}

class _HistoryLocationPageState extends State<HistoryLocationPage> {
  BaiduLocation? _currentLocation;
  final LocationFlutterPlugin _myLocPlugin = LocationFlutterPlugin();
  bool _permissionDenied = false;
  BMFMapController? _mapController;
  List<TrajectoryPoint> _trajectoryPoints = [];
  bool _isLoading = false;
  String _errorMessage = '';
  // 修改为自定义HttpClient实例
  late HttpClient _httpClient;

  @override
  void initState() {
    super.initState();
    // 初始化自定义HttpClient
    _httpClient = HttpClient();
    _checkLocationPermission().then((granted) {
      if (granted) {
        _setupLocationCallbacks();
        _fetchTrajectoryData();
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
    LogUtil.info('定位结果: 纬度=${result.latitude}, 经度=${result.longitude}');
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

      // 添加当前位置标记点
      _mapController?.addMarker(
        BMFMarker.icon(
          position: BMFCoordinate(result.latitude!, result.longitude!),
          title: 'history_location',
          icon: 'assets/images/user_null.png', // 使用自定义图标
        ),
      );
    }
  }

  Future<void> _fetchTrajectoryData() async {
    setState(() {
      _isLoading = true;
      _errorMessage = '';
    });

    try {
      Map<String, String> queryParameters = {'page': '0', 'size': '5'};
      // 使用自定义HttpClient的get方法
      final response = await _httpClient.get(
        '/trajectorys/query',
        queryParameters: queryParameters,
      );

      if (response['code'] == 200) {
        // 直接使用response作为参数，而不是response.data
        final ApiResponse apiResponse = ApiResponse.fromJson(response);
        LogUtil.info('获取轨迹数据成功: ${apiResponse.data.trajectories.length} 条');
        setState(() {
          _trajectoryPoints = apiResponse.data.trajectories; // 修改字段名
          _drawTrajectoryOnMap();
        });
      } else {
        throw Exception('请求失败: ${response['code']} - ${response['message']}');
      }
    } catch (e) {
      LogUtil.error('获取轨迹数据失败: $e');
      setState(() {
        _errorMessage = '获取轨迹数据失败: $e';
      });
    } finally {
      setState(() {
        _isLoading = false;
      });
    }
  }

  void _drawTrajectoryOnMap() {
    if (_mapController == null || _trajectoryPoints.isEmpty) {
      return;
    }

    // 清除之前的标记
    _mapController?.clearOverlays();

    List<BMFCoordinate> coordinates = [];

    for (int i = 0; i < _trajectoryPoints.length; i++) {
      final point = _trajectoryPoints[i];
      final coordinate = BMFCoordinate(point.latitude, point.longitude);
      coordinates.add(coordinate);

      // 格式化时间
      String formattedTime = '';
      try {
        final DateTime dateTime = DateTime.parse(point.time);
        formattedTime = DateFormat('yyyy-MM-dd HH:mm').format(dateTime);
      } catch (e) {
        formattedTime = point.time;
      }

      // 添加标记点
      _mapController?.addMarker(
        BMFMarker.icon(
          position: coordinate,
          title: '轨迹点 ${i + 1}',
          subtitle: '${point.address}\n$formattedTime',
          icon: null,
          // icon: i == 0
          //     ? 'assets/images/start_point.png'
          //     : i == _trajectoryPoints.length - 1
          //     ? 'assets/images/end_point.png'
          //     : 'assets/images/mid_point.png',
        ),
      );
    }

    // 如果有2个以上的点，绘制轨迹线
    if (coordinates.length >= 2) {
      // 创建轨迹线
      final BMFPolyline polyline = BMFPolyline(
        coordinates: coordinates,
        width: 8, // 线宽
        lineJoinType: BMFLineJoinType.LineJoinRound, // 线连接类型
        lineCapType: BMFLineCapType.LineCapRound, // 线端点类型
      );

      // 添加轨迹线到地图
      _mapController?.addPolyline(polyline);

      // 计算所有坐标的中心点
      double centerLat = 0;
      double centerLng = 0;
      for (final coord in coordinates) {
        centerLat += coord.latitude;
        centerLng += coord.longitude;
      }
      centerLat /= coordinates.length;
      centerLng /= coordinates.length;

      // 设置地图中心点
      _mapController?.setCenterCoordinate(
        BMFCoordinate(centerLat, centerLng),
        true,
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
                  child: Stack(
                    children: [
                      BMFMapWidget(
                        onBMFMapCreated: (controller) {
                          _mapController = controller;
                          // 如果已经有轨迹数据，绘制轨迹
                          if (_trajectoryPoints.isNotEmpty) {
                            _drawTrajectoryOnMap();
                          }
                        },
                        mapOptions: BMFMapOptions(zoomLevel: 15),
                      ),
                      if (_isLoading)
                        const Center(child: CircularProgressIndicator()),
                      if (_errorMessage.isNotEmpty)
                        Positioned(
                          top: 10,
                          left: 10,
                          right: 10,
                          child: Container(
                            padding: const EdgeInsets.all(8),
                            color: Colors.red.withOpacity(0.9),
                            child: Text(
                              _errorMessage,
                              style: const TextStyle(color: Colors.white),
                            ),
                          ),
                        ),
                    ],
                  ),
                ),
                // 底部显示轨迹信息
                if (_trajectoryPoints.isNotEmpty)
                  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text('轨迹点数量: ${_trajectoryPoints.length}'),
                        Text('起点: ${_trajectoryPoints.first.address}'),
                        if (_trajectoryPoints.length > 1)
                          Text('终点: ${_trajectoryPoints.last.address}'),
                      ],
                    ),
                  ),
              ],
            ),
    );
  }

  @override
  void dispose() {
    _myLocPlugin.stopLocation();
    _mapController?.clearOverlays();
    super.dispose();
  }
}
