import 'package:flutter/material.dart';
import 'package:flutter_study/MyDrawer.dart';

import 'constant/RouteNameConstant.dart';

class Home extends StatefulWidget {
  @override
  _ScaffoldRouteState createState() => _ScaffoldRouteState();
}

class _ScaffoldRouteState extends State<Home> {
  List<IconData> _icons = []; //保存Icon数据

  // 初始化数据
  @override
  void initState() {
    // 初始化数据
    _retrieveIcons();
  }

  @override
  Widget build(BuildContext context) {
    //获取路由参数
    var args = ModalRoute.of(context).settings.arguments;
    print('args=' + args);

    return Scaffold(
        appBar: AppBar(
          //导航栏
          title: Text("App Name"),
          actions: <Widget>[
            //导航栏右侧菜单
            IconButton(icon: Icon(Icons.share), onPressed: () {}),
          ],
        ),
        drawer: new MyDrawer(), //抽屉
        floatingActionButton: FloatingActionButton(
            //悬浮按钮
            child: Icon(Icons.add),
            onPressed: _onAdd),
        body: new Center(
          child: GridView.builder(
            gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                crossAxisCount: 3, //每行三列
                childAspectRatio: 1.0 //显示区域宽高相等
                ),
            itemCount: _icons.length,
            itemBuilder: (context, index) {
              return new GestureDetector(
                onTap: () {
                  //处理点击事件
                  print(index);
                  Navigator.of(context).pushNamed(RouteNameConstant.route_name_tv_list, arguments: "hi");
                },
                child: Icon(_icons[index]),
              );
            },
          ),
        )
    );
  }

//模拟异步获取数据
  void _retrieveIcons() {
    Future.delayed(Duration(milliseconds: 200)).then((e) {
      setState(() {
        _icons.addAll([
          Icons.tv,
        ]);
      });
    });
  }

  void _onAdd() {}
}
