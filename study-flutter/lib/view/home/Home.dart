import 'package:flutter/material.dart';
import 'package:app_lu_lu/component/dialog/Dialogs.dart';
import 'package:app_lu_lu/component/log/Logs.dart';
import 'package:app_lu_lu/component/toast/Toasts.dart';
import 'package:app_lu_lu/constant/LocaleConstant.dart';
import 'package:app_lu_lu/constant/RouteNameConstant.dart';
import 'package:app_lu_lu/locale/Translations.dart';
import 'package:app_lu_lu/view/home/HomeDrawer.dart';
import 'package:app_lu_lu/view/home/MenuItem.dart';

class Home extends StatefulWidget {
  @override
  _ScaffoldRouteState createState() => _ScaffoldRouteState();
}

class _ScaffoldRouteState extends State<Home> {
  List<MenuItem> menuItems = []; //保存Icon数据
  DateTime _lastTime = DateTime.now(); //上次点击时间

  // 初始化数据
  @override
  void initState() {
    // 初始化数据
    _retrieveIcons();
  }

  @override
  Widget build(BuildContext context) {
    //获取路由参数
    var args = ModalRoute.of(context)?.settings?.arguments;
    print('args=${args}');

    return new WillPopScope(
        onWillPop: onWillPop, //重点此举
        child: Scaffold(
          appBar: AppBar(
            //导航栏
            title: Text(Translations.textOf(context, "app.name")),
//          actions: <Widget>[
//            //导航栏右侧菜单
//            IconButton(icon: Icon(Icons.share), onPressed: () {}),
//          ],
          ),
          drawer: new HomeDrawer(), //抽屉
//          floatingActionButton: FloatingActionButton(
//              //悬浮按钮
//              child: Icon(Icons.add),
//              onPressed: _onAdd),
          body: GridView.builder(
            gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                crossAxisCount: 3, //每行三列
                childAspectRatio: 1.4 //显示区域宽高相等
                ),
            itemCount: menuItems.length,
            itemBuilder: (context, index) {
              return new GestureDetector(
                  onTap: () {
                    MenuItem current = menuItems[index];
                    Navigator.of(context).pushNamed(current.route);
                  },
                  child: Column(
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: <Widget>[
                        Padding(
                            padding: const EdgeInsets.fromLTRB(0, 16.0, 0, 0),
                            child: Icon(
                              menuItems[index].icon,
                              size: 42,
                            )),
                        Padding(
                            padding: const EdgeInsets.fromLTRB(0, 8.0, 0, 0),
                            child: Text(menuItems[index].text))
                      ]));
            },
          ),
        ));
  }

  //模拟异步获取数据
  void _retrieveIcons() {
    Future.delayed(Duration(milliseconds: 200)).then((e) {
      setState(() {
        menuItems.addAll([
          new MenuItem(Icons.tv, Translations.textOf(context, "home.menu.tv"),
              RouteNameConstant.route_name_tv),
//          new MenuItem(Icons.person, Translations.textOf(c
//          ontext, "home.menu.profile"), RouteNameConstant.route_name_profile),
        ]);
      });
    });
  }

  Future<bool> onWillPop() async {
    if (DateTime.now().difference(_lastTime) > Duration(seconds: 1)) {
      //两次点击间隔超过1s重新计时
      _lastTime = DateTime.now();
      Toasts.show("再点一次退出应用");
      return false;
    }
    return true;
  }

  void _onAdd() {}
}
