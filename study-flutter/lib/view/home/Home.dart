import 'package:flutter/material.dart';
import 'package:flutter_study/locale/translations.dart';
import 'package:flutter_study/view/home/HomeDrawer.dart';
import 'package:flutter_study/constant/RouteNameConstant.dart';
import 'package:flutter_study/view/home/MenuItem.dart';

class Home extends StatefulWidget {
  @override
  _ScaffoldRouteState createState() => _ScaffoldRouteState();
}

class _ScaffoldRouteState extends State<Home> {
  List<MenuItem> menuItems = []; //保存Icon数据

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
          title: Text(Translations.textOf(context, "home.title")),
          actions: <Widget>[
            //导航栏右侧菜单
            IconButton(icon: Icon(Icons.share), onPressed: () {}),
          ],
        ),
        drawer: new HomeDrawer(), //抽屉
        floatingActionButton: FloatingActionButton(
            //悬浮按钮
            child: Icon(Icons.add),
            onPressed: _onAdd),
        body: Padding(
            padding: const EdgeInsets.fromLTRB(0, 32.0, 0, 0),
            child: new Center(
              child: GridView.builder(
                gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                    crossAxisCount: 3, //每行三列
                    childAspectRatio: 1.5 //显示区域宽高相等
                    ),
                itemCount: menuItems.length,
                itemBuilder: (context, index) {
                  return new GestureDetector(
                      onTap: () {
                        //处理点击事件
                        print(index);
                        Navigator.of(context).pushNamed(
                            RouteNameConstant.route_name_tv_list,
                            arguments: "hi");
                      },
                      child: Column(
                          crossAxisAlignment: CrossAxisAlignment.center,
                          children: <Widget>[
                            Icon(menuItems[index].icon,size: 42,),
                            Text(menuItems[index].text)
                          ]));
                },
              ),
            )));
  }

  //模拟异步获取数据
  void _retrieveIcons() {
    Future.delayed(Duration(milliseconds: 200)).then((e) {
      setState(() {
        menuItems.addAll([
          new MenuItem(Icons.tv, 'TV'),
//          new MenuItem(Icons.tv, 'TV2'),
//          new MenuItem(Icons.tv, 'TV3'),
//          new MenuItem(Icons.tv, 'TV4'),
        ]);
      });
    });
  }

  void _onAdd() {}
}
