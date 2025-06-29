import 'package:flutter/material.dart';
import 'package:flutter_baidu_mapapi_map/flutter_baidu_mapapi_map.dart';
import 'package:flutter_baidu_mapapi_base/flutter_baidu_mapapi_base.dart';

class MapTrajectory extends StatefulWidget {
  const MapTrajectory({Key? key}) : super(key: key);

  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MapTrajectory> {
  late BMFMapController _myMapController;
  bool _suc = false;

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    List<Widget> resultWidgets = [];

    return MaterialApp(
      home: Scaffold(
        body: Column(
          children: [
            _createMapContainer(),
            Container(height: 20),
            SizedBox(
              height: MediaQuery.of(context).size.height - 500,
              child: ListView(children: resultWidgets),
            ),
            _createButtonContainer(),
          ],
        ),
      ),
    );
  }

  Widget _createMapContainer() {
    return SizedBox(
      height: 300,
      child: BMFMapWidget(
        onBMFMapCreated: (controller) {
          onBMFMapCreated(controller);
        },
        mapOptions: initMapOptions(),
      ),
    );
  }

  Widget _createButtonContainer() {
    return Container(
      alignment: Alignment.bottomCenter,
      child: ElevatedButton(
        onPressed: () {
          ///设置定位参数
        },
        child: const Text('开始定位'),
        style: ElevatedButton.styleFrom(
          backgroundColor:
              Colors.blueAccent, //change background color of button
          foregroundColor: Colors.yellow, //change text color of button
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(5)),
        ),
      ),
    );
  }

  Widget _resultWidget(key, value) {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          Text(
            '$key:'
            ' $value',
          ),
        ],
      ),
    );
  }

  /// 设置地图参数
  BMFMapOptions initMapOptions() {
    BMFMapOptions mapOptions = BMFMapOptions(
      center: BMFCoordinate(39.917215, 116.380341),
      zoomLevel: 12,
      mapPadding: BMFEdgeInsets(top: 0, left: 0, right: 0, bottom: 0),
    );
    return mapOptions;
  }

  /// 创建完成回调
  void onBMFMapCreated(BMFMapController controller) {
    _myMapController = controller;

    /// 地图加载回调
    _myMapController.setMapDidLoadCallback(
      callback: () {
        print('mapDidLoad-地图加载完成');
      },
    );
  }
}
