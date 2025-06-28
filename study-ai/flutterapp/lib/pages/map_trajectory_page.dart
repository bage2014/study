import 'package:flutter/material.dart';
import 'package:amap_map_fluttify/amap_map_fluttify.dart';
import 'package:permission_handler/permission_handler.dart';


class MapTrajectoryPage extends StatefulWidget {
  const MapTrajectoryPage({Key? key}) : super(key: key);

  @override
  _MapTrajectoryPageState createState() => _MapTrajectoryPageState();
}

class _MapTrajectoryPageState extends State<MapTrajectoryPage> {
  AmapController? _mapController; // Made nullable with ?
  LatLng? _currentLocation;

  @override
  void initState() {
    super.initState();
    _initMap();
    _getCurrentLocation();
  }

  Future<void> _initMap() async {
    await AmapCore.init('YOUR_AMAP_API_KEY');
  }

  Future<void> _getCurrentLocation() async {
    final status = await Permission.location.request();
    if (status.isGranted) {
      final location = await AmapLocation.instance.fetchLocation();
    setState(() {
      _currentLocation = location.latLng;
    });
    // if (_currentLocation != null && _mapController != null) {
    //   log('currentLocation: $_currentLocation');
    //   _mapController!.animateCamera(CameraUpdate.newLatLngZoom(_currentLocation!, 15));
    // }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Map Trajectory'),
      ),
      body: AmapView(
        onMapCreated: (controller) async {
          _mapController = controller;
          _mapController!.showMyLocation(MyLocationOption(show: true)); // 使用MyLocationOption对象
          // if (_currentLocation != null) {
          //   log('currentLocation: $_currentLocation');
          //   controller.animateCamera(CameraUpdate.newLatLngZoom(LatLng(_currentLocation!.latitude, _currentLocation!.longitude), 15));
          // }
        },
        showZoomControl: true,
        showCompass: true,
      ),
    );
  }
}