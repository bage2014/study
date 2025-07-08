import 'dart:async'; // 添加此行
import 'package:flutter/material.dart';
import 'package:flutter_bmflocation/flutter_bmflocation.dart';
import 'dart:io';
import 'package:permission_handler/permission_handler.dart';
import 'package:intl/intl.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import '../../common/constants.dart';

class TrackLocationPage extends StatefulWidget {
  const TrackLocationPage({super.key});

  @override
  State<TrackLocationPage> createState() => _TrackLocationPageState();
}

class _TrackLocationPageState extends State<TrackLocationPage> {
  final LocationFlutterPlugin _locationPlugin = LocationFlutterPlugin();
  BaiduLocation? _currentLocation;
  bool _permissionDenied = false;
  bool _isTracking = false;
  List<String> _locationLogs = [];
  Timer? _trackingTimer;

  @override
  void initState() {
    super.initState();
    _checkLocationPermission().then((granted) {
      if (granted) {
        _setupLocationService();
      } else {
        setState(() => _permissionDenied = true);
      }
    });
  }

  @override
  // 替换 dispose 中的 destroyLocation
  void dispose() {
    _stopTracking();
    _locationPlugin.stopLocation(); // 替换 destroyLocation()
    super.dispose();
  }

  // 权限检查（与CurrentLocationPage保持一致）
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
    // 同意隐私政策（必须设置）
    _locationPlugin.setAgreePrivacy(true);

    // 配置定位参数（与CurrentLocationPage保持一致）
    final Map<String, Object> locationOption = {
      'isNeedAddress': true,
      'isNeedLocationDescribe': true,
      'isNeedNewVersionRgc': true,
      'locationTimeout': 20000,
      'reGeocodeTimeout': 20000,
      'coorType': 'bd09ll', // 百度经纬度坐标
    };

    // Initialize location client before setting up callbacks
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

  // 定位回调处理
  void _onLocationReceived(BaiduLocation result) {
    if (result.errorCode != 0) {
      print('定位错误: ${result.errorCode} - ${result.errorInfo}');
      _addLog('定位失败: ${result.errorInfo}');
      return;
    }

    setState(() => _currentLocation = result);
    _addLogAndUpload(result);
  }

  // 添加日志并上传到百度鹰眼
  void _addLogAndUpload(BaiduLocation location) {
    final String timestamp = DateFormat(
      'yyyy-MM-dd HH:mm:ss',
    ).format(DateTime.now());
    final String log =
        '[${timestamp}] 纬度: ${location.latitude}, 经度: ${location.longitude}, 地址: ${location.address ?? "未知"}\n';

    // 添加到日志列表
    setState(() {
      _locationLogs.insert(0, log);
      if (_locationLogs.length > 50) _locationLogs.removeLast();
    });

    // 打印到控制台
    print('轨迹记录: $log');

    // 上传到百度鹰眼
    _uploadToBaiduYingYan(location, timestamp);
  }

  // 上传到百度鹰眼服务
  Future<void> _uploadToBaiduYingYan(
    BaiduLocation location,
    String timestamp,
  ) async {
    if (Constants.baiduAK.isEmpty || Constants.yingYanServiceId <= 0) {
      _addLog('百度鹰眼配置未完成，无法上传');
      return;
    }

    try {
      final response = await http.post(
        Uri.parse('https://yingyan.baidu.com/api/v3/track/addpoints'),
        headers: {'Content-Type': 'application/json'}, 
        body: jsonEncode({
          'ak': Constants.baiduAK,
          'service_id': Constants.yingYanServiceId,
          'entity_name':
              'user_${DateTime.now().millisecondsSinceEpoch % 10000}',
          'points': [
            {
              'latitude': location.latitude!,
              'longitude': location.longitude!,
              'loc_time': DateTime.now().millisecondsSinceEpoch ~/ 1000,
              'speed': location.speed ?? 0,
              'direction': 0,
              'height': location.altitude ?? 0,
              'radius': location.radius ?? 0,
            },
          ],
        }),
      );

      final responseBody = utf8.decode(response.bodyBytes);
      final data = jsonDecode(responseBody);

      if (data['code'] == 200) {
        _addLog('轨迹上传成功');
      } else {
        final errorMessage = data['message'] ?? '请求后台异常';
        _addLog('轨迹上传失败: $errorMessage');
      }
    } catch (e) {
      _addLog('上传异常: ${e.toString()}');
    }
  }

  // 添加日志（内部方法）
  void _addLog(String message) {
    setState(() {
      _locationLogs.insert(
        0,
        '[${DateFormat('HH:mm:ss').format(DateTime.now())}] $message',
      );
      if (_locationLogs.length > 50) _locationLogs.removeLast();
    });
  }

  // 开始轨迹跟踪
  void _startTracking() {
    if (_isTracking) return;

    setState(() => _isTracking = true);
    _addLog('开始轨迹跟踪...');

    // 启动定位
    if (Platform.isIOS) {
      // iOS使用单次定位+定时器实现连续定位
      _locationPlugin.startLocation(); // 立即获取一次
    } else {
      // Android直接使用连续定位
      _locationPlugin.startLocation();
    }
  }

  // 停止轨迹跟踪
  void _stopTracking() {
    if (!_isTracking) return;

    setState(() => _isTracking = false);
    _trackingTimer?.cancel();
    _locationPlugin.stopLocation();
    _addLog('轨迹跟踪已停止');
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('位置轨迹'),
        backgroundColor: Colors.blue,
        actions: [
          IconButton(
            icon: Icon(_isTracking ? Icons.stop : Icons.play_arrow),
            onPressed: _isTracking ? _stopTracking : _startTracking,
          ),
        ],
      ),
      body: _buildBody(),
    );
  }

  // 构建页面主体（模仿CurrentLocationPage的UI结构）
  Widget _buildBody() {
    if (_permissionDenied) {
      return const Center(child: Text('需要位置权限才能获取位置信息，请在设置中启用权限。'));
    }

    return Column(
      children: [
        // 当前位置信息卡片（与CurrentLocationPage风格一致）
        Card(
          margin: const EdgeInsets.all(16),
          elevation: 4,
          child: Padding(
            padding: const EdgeInsets.all(16),
            child: _currentLocation != null
                ? Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        '当前位置信息',
                        style: Theme.of(context).textTheme.titleMedium,
                      ),
                      const Divider(height: 16),
                      Text('纬度: ${_currentLocation!.latitude}'),
                      Text('经度: ${_currentLocation!.longitude}'),
                      if (_currentLocation!.address != null)
                        Text('地址: ${_currentLocation!.address}'),
                      Text(
                        '采集时间: ${DateFormat('yyyy-MM-dd HH:mm:ss').format(DateTime.now())}',
                      ),
                    ],
                  )
                : const Center(child: Text('等待定位...')),
          ),
        ),

        // 跟踪状态显示
        Padding(
          padding: const EdgeInsets.symmetric(horizontal: 16),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text('跟踪状态: ${_isTracking ? "正在跟踪" : "已停止"}'),
              ElevatedButton(
                onPressed: _isTracking ? _stopTracking : _startTracking,
                child: Text(_isTracking ? "停止跟踪" : "开始跟踪"),
              ),
            ],
          ),
        ),

        // 日志列表
        const Padding(
          padding: EdgeInsets.all(16),
          child: Align(
            alignment: Alignment.centerLeft,
            child: Text('轨迹日志:', style: TextStyle(fontWeight: FontWeight.bold)),
          ),
        ),
        Expanded(
          child: ListView.builder(
            padding: const EdgeInsets.symmetric(horizontal: 16),
            itemCount: _locationLogs.length,
            itemBuilder: (context, index) => Text(_locationLogs[index]),
          ),
        ),
      ],
    );
  }
}
