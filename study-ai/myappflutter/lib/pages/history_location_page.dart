import 'package:flutter/material.dart';
import 'package:intl/intl.dart';  // 添加此行

// 历史位置数据模型
class LocationRecord {
  final String id;
  final double latitude;
  final double longitude;
  final DateTime timestamp;
  final String address;

  LocationRecord({
    required this.id,
    required this.latitude,
    required this.longitude,
    required this.timestamp,
    required this.address,
  });
}

class HistoryLocationPage extends StatefulWidget {
  const HistoryLocationPage({super.key});

  @override
  State<HistoryLocationPage> createState() => _HistoryLocationPageState();
}

class _HistoryLocationPageState extends State<HistoryLocationPage> {
  // 模拟历史位置数据
  final List<LocationRecord> _locationHistory = [
    LocationRecord(
      id: '1',
      latitude: 31.2150,
      longitude: 121.4347,
      timestamp: DateTime.now().subtract(const Duration(hours: 2)),
      address: '上海市长宁区虹桥路1号',
    ),
    LocationRecord(
      id: '2',
      latitude: 31.2200,
      longitude: 121.4500,
      timestamp: DateTime.now().subtract(const Duration(hours: 4)),
      address: '上海市长宁区中山公园',
    ),
    LocationRecord(
      id: '3',
      latitude: 31.2050,
      longitude: 121.4200,
      timestamp: DateTime.now().subtract(const Duration(hours: 8)),
      address: '上海市长宁区愚园路',
    ),
    LocationRecord(
      id: '4',
      latitude: 31.2300,
      longitude: 121.4400,
      timestamp: DateTime.now().subtract(const Duration(days: 1)),
      address: '上海市长宁区天山路',
    ),
    LocationRecord(
      id: '5',
      latitude: 31.2180,
      longitude: 121.4600,
      timestamp: DateTime.now().subtract(const Duration(days: 1, hours: 3)),
      address: '上海市长宁区古北路',
    ),
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('历史位置'),
        backgroundColor: Colors.blue,
      ),
      body: _buildLocationHistoryList(),
    );
  }

  Widget _buildLocationHistoryList() {
    // 按时间戳降序排序（最新的在前）
    _locationHistory.sort((a, b) => b.timestamp.compareTo(a.timestamp));

    return ListView.separated(
      padding: const EdgeInsets.all(16),
      itemCount: _locationHistory.length,
      separatorBuilder: (context, index) => const Divider(height: 1),
      itemBuilder: (context, index) => _buildLocationItem(_locationHistory[index]),
    );
  }

  Widget _buildLocationItem(LocationRecord record) {
    return ListTile(
      contentPadding: const EdgeInsets.symmetric(vertical: 12),
      leading: const Icon(Icons.location_on, color: Colors.blue, size: 32),
      title: Text(
        record.address,
        style: const TextStyle(
          fontSize: 16,
          fontWeight: FontWeight.w500,
        ),
      ),
      subtitle: Padding(
        padding: const EdgeInsets.only(top: 4),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text('坐标: ${record.latitude}, ${record.longitude}'),
            Text('时间: ${DateFormat('yyyy-MM-dd HH:mm').format(record.timestamp)}'),
          ],
        ),
      ),
      onTap: () {
        // 点击事件可以导航到详情页或地图查看
      },
    );
  }

  void _onLocationItemTapped(LocationRecord record) {
    // 点击历史位置项的处理逻辑
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
            Text('记录时间: ${DateFormat('yyyy-MM-dd HH:mm:ss').format(record.timestamp)}'),
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