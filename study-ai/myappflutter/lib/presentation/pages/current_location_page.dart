import 'package:flutter/material.dart';
import 'package:flutter_bmflocation/flutter_bmflocation.dart';
import 'package:flutter_baidu_mapapi_map/flutter_baidu_mapapi_map.dart';
import 'package:flutter_baidu_mapapi_base/flutter_baidu_mapapi_base.dart';
import 'dart:io';
import 'package:permission_handler/permission_handler.dart';
import '../widgets/base_page.dart'; // 导入基础页面

class CurrentLocationPage extends StatefulWidget {
  const CurrentLocationPage({super.key});

  @override
  State<CurrentLocationPage> createState() => _CurrentLocationPageState();
}

class _CurrentLocationPageState extends State<CurrentLocationPage> {
  BaiduLocation? _currentLocation;
  LocationFlutterPlugin _myLocPlugin = LocationFlutterPlugin();
  bool _permissionDenied = false;
  BMFMapController? _mapController;
  bool _isLocating = false;
  String _locationStatus = '等待定位...';

  @override
  void initState() {
    super.initState();
    _checkLocationPermission().then((granted) {
      if (granted) {
        _startLocation();
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

  Future<void> _startLocation() async {
    setState(() {
      _isLocating = true;
      _locationStatus = '正在定位中...';
    });

    // 设置定位参数 - 根据百度地图API推荐配置
    final Map locationOption = {
      'isNeedAddress': true,
      'isNeedLocationDescribe': true,
      'isNeedNewVersionRgc': true,
      'locationTimeout': 20000,
      'reGeocodeTimeout': 20000,
      'coorType': 'bd09ll', // 使用百度经纬度坐标
      'openGps': true, // 开启GPS
      'scanSpan': 1000, // 定位间隔1秒
    };

    // 1. 先同意隐私政策
    _myLocPlugin.setAgreePrivacy(true);

    // 3. 启动定位
    if (Platform.isIOS) {
      _myLocPlugin.singleLocation(locationOption);
    } else {
      _myLocPlugin.singleLocation(locationOption);
    }

    // 4. 设置定位回调
    if (Platform.isIOS) {
      _myLocPlugin.singleLocationCallback(
        callback: (BaiduLocation result) {
          _handleLocationResult(result);
        },
      );
    } else if (Platform.isAndroid) {
      _myLocPlugin.seriesLocationCallback(
        callback: (BaiduLocation result) {
          _handleLocationResult(result);
        },
      );
    }
  }

  void _handleLocationResult(BaiduLocation result) {
    print(
      '定位结果: id=${result.locationID}, c=${result.country}, p = ${result.province}, city=${result.city},ss=${result.street}',
    );
    print('定位结果: errorCode=${result.errorCode}, errorInfo=${result.errorInfo}');
    print(
      '定位结果: 纬度=${result.latitude}, 经度=${result.longitude}, 地址=${result.locationDetail}',
    );

    setState(() {
      _isLocating = false;

      if (result.longitude != null && result.latitude != null) {
        // 定位成功
        _currentLocation = result;
        _locationStatus = '定位成功';
        _updateMapWithCurrentLocation();
      } else {
        // 定位失败
        _locationStatus = '定位失败: ${result.errorInfo}';
      }
    });
  }

  void _updateMapWithCurrentLocation() {
    if (_mapController == null || _currentLocation == null) {
      return;
    }

    // 清除之前的标记
    _mapController?.clearOverlays();

    // 添加当前位置标记 - 使用百度坐标
    final coordinate = BMFCoordinate(
      _currentLocation!.latitude!,
      _currentLocation!.longitude!,
    );

    // 添加定位标记
    _mapController?.addMarker(
      BMFMarker.icon(
        position: coordinate,
        title: '当前位置',
        subtitle: _currentLocation!.address ?? '未知地址',
        icon: 'assets/images/logo128.png',
      ),
    );

    // 设置地图中心点和缩放级别
    _mapController?.setCenterCoordinate(coordinate, true);
    _mapController?.setZoomTo(17); // 设置合适的缩放级别
  }

  void _refreshLocation() {
    if (_permissionDenied) {
      _checkLocationPermission().then((granted) {
        if (granted) {
          _startLocation();
        }
      });
    } else {
      _startLocation();
    }
  }

  @override
  Widget build(BuildContext context) {
    return BasePage(
      title: 'current_location',
      body: Column(
        children: [
          // 状态提示栏
          Container(
            padding: const EdgeInsets.all(12),
            color: _isLocating
                ? Colors.blue[50]
                : _currentLocation != null
                ? Colors.green[50]
                : Colors.grey[50],
            child: Row(
              children: [
                Icon(
                  _isLocating
                      ? Icons.gps_fixed
                      : _currentLocation != null
                      ? Icons.location_on
                      : Icons.gps_off,
                  color: _isLocating
                      ? Colors.blue
                      : _currentLocation != null
                      ? Colors.green
                      : Colors.grey,
                ),
                const SizedBox(width: 8),
                Expanded(
                  child: Text(
                    _locationStatus,
                    style: TextStyle(
                      color: _isLocating
                          ? Colors.blue
                          : _currentLocation != null
                          ? Colors.green
                          : Colors.grey,
                      fontWeight: FontWeight.w500,
                    ),
                  ),
                ),
                if (!_isLocating)
                  IconButton(
                    icon: const Icon(Icons.refresh),
                    onPressed: _refreshLocation,
                    tooltip: '重新定位',
                  ),
              ],
            ),
          ),

          Expanded(
            child: Stack(
              children: [
                // 百度地图
                BMFMapWidget(
                  onBMFMapCreated: (controller) {
                    _mapController = controller;
                    // 如果已经有当前位置数据，更新地图
                    if (_currentLocation != null) {
                      _updateMapWithCurrentLocation();
                    }
                  },
                  mapOptions: BMFMapOptions(
                    zoomLevel: 16,
                    mapType: BMFMapType.Standard,
                    showZoomControl: true, // 显示缩放控件
                  ),
                ),

                // 加载指示器
                if (_isLocating)
                  const Center(child: CircularProgressIndicator()),

                // 权限被拒绝提示
                if (_permissionDenied)
                  Center(
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        const Icon(
                          Icons.location_off,
                          size: 48,
                          color: Colors.red,
                        ),
                        const SizedBox(height: 16),
                        const Text('位置权限被拒绝', style: TextStyle(fontSize: 18)),
                        const SizedBox(height: 8),
                        const Text('请授予位置权限以使用定位功能'),
                        const SizedBox(height: 16),
                        ElevatedButton(
                          onPressed: () {
                            openAppSettings();
                          },
                          child: const Text('打开设置'),
                        ),
                      ],
                    ),
                  ),
              ],
            ),
          ),

          // 底部位置信息面板
          if (_currentLocation != null)
            Container(
              padding: const EdgeInsets.all(16),
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: const BorderRadius.vertical(
                  top: Radius.circular(12),
                ),
                boxShadow: [
                  BoxShadow(
                    color: Colors.black.withOpacity(0.1),
                    blurRadius: 8,
                    offset: const Offset(0, -2),
                  ),
                ],
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Text(
                    '📍 当前位置信息',
                    style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                  ),
                  const SizedBox(height: 12),

                  // 地址信息
                  _buildInfoRow('位置', _buildAddressInfo(_currentLocation!)),

                  // 经纬度信息
                  Row(
                    children: [
                      Expanded(
                        child: _buildInfoRow(
                          '纬度',
                          _currentLocation!.latitude?.toStringAsFixed(6) ??
                              '未知',
                        ),
                      ),
                      const SizedBox(width: 16),
                      Expanded(
                        child: _buildInfoRow(
                          '经度',
                          _currentLocation!.longitude?.toStringAsFixed(6) ??
                              '未知',
                        ),
                      ),
                    ],
                  ),

                  // 行政区划信息
                  if (_currentLocation!.province != null ||
                      _currentLocation!.city != null ||
                      _currentLocation!.district != null)
                    Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        const SizedBox(height: 8),
                        const Text(
                          '行政区划',
                          style: TextStyle(
                            fontWeight: FontWeight.w500,
                            color: Colors.grey,
                          ),
                        ),
                        if (_currentLocation!.province != null)
                          Text('省份: ${_currentLocation!.province}'),
                        if (_currentLocation!.city != null)
                          Text('城市: ${_currentLocation!.city}'),
                        if (_currentLocation!.district != null)
                          Text('区域: ${_currentLocation!.district}'),
                      ],
                    ),
                ],
              ),
            ),
        ],
      ),
    );
  }

  String _buildAddressInfo(BaiduLocation location) {
    // 优先使用locationDetail字段
    if (location.locationDetail != null && location.locationDetail!.isNotEmpty) {
      return location.locationDetail!;
    }
    
    // 如果locationDetail为空，尝试拼接其他地址字段
    List<String> addressParts = [];
    if (location.country != null && location.country!.isNotEmpty) {
      addressParts.add(location.country!);
    }
    if (location.province != null && location.province!.isNotEmpty) {
      addressParts.add(location.province!);
    }
    if (location.city != null && location.city!.isNotEmpty) {
      addressParts.add(location.city!);
    }
    if (location.district != null && location.district!.isNotEmpty) {
      addressParts.add(location.district!);
    }
    if (location.street != null && location.street!.isNotEmpty) {
      addressParts.add(location.street!);
    }
    if (location.streetNumber != null && location.streetNumber!.isNotEmpty) {
      addressParts.add(location.streetNumber!);
    }
    
    if (addressParts.isNotEmpty) {
      return addressParts.join(' ');
    }
    
    // 如果所有地址字段都为空，显示经纬度信息
    if (location.latitude != null && location.longitude != null) {
      return '纬度: ${location.latitude!.toStringAsFixed(6)}, 经度: ${location.longitude!.toStringAsFixed(6)}';
    }
    
    return '未知位置';
  }

  Widget _buildInfoRow(String label, String value) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(label, style: TextStyle(fontSize: 12, color: Colors.grey[600])),
        Text(
          value,
          style: const TextStyle(fontSize: 14, fontWeight: FontWeight.w500),
        ),
        const SizedBox(height: 8),
      ],
    );
  }

  @override
  void dispose() {
    _myLocPlugin.stopLocation();
    _mapController?.clearOverlays();
    super.dispose();
  }
}
