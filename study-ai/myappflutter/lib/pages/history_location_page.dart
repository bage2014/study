import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:get/get.dart'; // 添加GetX导入
import '../../api/http_client.dart'; // 导入统一http client
import '../../common/constants.dart';

// 更新数据模型以匹配API响应格式
class LocationRecord {
  final double latitude;
  final double longitude;
  final DateTime time;
  final String address;

  LocationRecord({
    required this.latitude,
    required this.longitude,
    required this.time,
    required this.address,
  });

  // 从JSON创建LocationRecord实例
  factory LocationRecord.fromJson(Map<String, dynamic> json) {
    return LocationRecord(
      latitude: json['latitude'].toDouble(),
      longitude: json['longitude'].toDouble(),
      time: DateTime.parse(json['time']),
      address: json['address'],
    );
  }
}

class HistoryLocationPage extends StatefulWidget {
  const HistoryLocationPage({super.key});

  @override
  State<HistoryLocationPage> createState() => _HistoryLocationPageState();
}

class _HistoryLocationPageState extends State<HistoryLocationPage> {
  final List<LocationRecord> _locationHistory = [];
  bool _isLoading = true;
  String? _errorMessage;
  final HttpClient _httpClient = HttpClient(); // 创建http client实例

  @override
  void initState() {
    super.initState();
    _fetchLocationHistory();
  }

  // 使用统一http client获取历史轨迹数据
  Future<void> _fetchLocationHistory() async {
    try {
      // 使用统一http client的get方法
      final response = await _httpClient.get('/trajectorys/query');

      // 直接使用响应数据，HttpClient已处理JSON解析
      final List<dynamic> content = response['content'];

      setState(() {
        _locationHistory.clear();
        _locationHistory.addAll(
          content.map((json) => LocationRecord.fromJson(json)).toList(),
        );
        _isLoading = false;
      });
    } catch (e) {
      setState(() {
        _errorMessage = e.toString();
        _isLoading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('历史轨迹'), backgroundColor: Colors.blue),
      body: _buildBody(),
    );
  }

  Widget _buildBody() {
    if (_isLoading) {
      return const Center(child: CircularProgressIndicator());
    }

    if (_errorMessage != null) {
      return Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text('加载失败: $_errorMessage'),
            ElevatedButton(
              onPressed: _fetchLocationHistory,
              child: const Text('重试'),
            ),
          ],
        ),
      );
    }

    return _buildLocationHistoryList();
  }

  Widget _buildLocationHistoryList() {
    // 按时间降序排序
    _locationHistory.sort((a, b) => b.time.compareTo(a.time));

    return ListView.separated(
      padding: const EdgeInsets.all(16),
      itemCount: _locationHistory.length,
      separatorBuilder: (context, index) => const Divider(height: 1),
      itemBuilder: (context, index) =>
          _buildLocationItem(_locationHistory[index]),
    );
  }

  Widget _buildLocationItem(LocationRecord record) {
    return ListTile(
      contentPadding: const EdgeInsets.symmetric(vertical: 12),
      leading: const Icon(Icons.location_on, color: Colors.blue, size: 32),
      title: Text(
        record.address,
        style: const TextStyle(fontSize: 16, fontWeight: FontWeight.w500),
      ),
      subtitle: Padding(
        padding: const EdgeInsets.only(top: 4),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text('坐标: ${record.latitude}, ${record.longitude}'),
            Text('时间: ${DateFormat('yyyy-MM-dd HH:mm').format(record.time)}'),
          ],
        ),
      ),
      onTap: () => _onLocationItemTapped(record),
    );
  }

  void _onLocationItemTapped(LocationRecord record) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('位置详情'),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text('地址: ${record.address}'),
            const SizedBox(height: 8),
            Text('纬度: ${record.latitude.toStringAsFixed(6)}'),
            Text('经度: ${record.longitude.toStringAsFixed(6)}'),
            const SizedBox(height: 8),
            Text(
              '记录时间: ${DateFormat('yyyy-MM-dd HH:mm:ss').format(record.time)}',
            ),
          ],
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('关闭'),
          ),
        ],
      ),
    );
  }
}
