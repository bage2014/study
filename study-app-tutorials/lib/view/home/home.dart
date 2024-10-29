import 'package:tutorials/component/cache/user_caches.dart';
import 'package:tutorials/component/event/event_bus.dart';
import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/utils/app_utils.dart';
import 'package:flutter/material.dart';
import 'package:tutorials/component/toast/Toasts.dart';
import 'package:tutorials/constant/route_constant.dart';
import 'package:tutorials/locale/translations.dart';
import 'package:tutorials/view/home/home_drawer.dart';
import 'package:tutorials/view/home/menu_item.dart';

import 'locale_update_event.dart';

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
    super.initState();
    EventBus.consume<LocaleUpdateEvent>((event) {
      Logs.info('event = ${event.toString()}');
      _retrieveIcons();
    });
  }

  @override
  Widget build(BuildContext context) {
    //获取路由参数
    var args = ModalRoute.of(context)?.settings?.arguments;
    Logs.info('args=${args}');
    var userName = UserCaches.getUser()?.userName;

    return WillPopScope(
        onWillPop: onWillPop, //重点此举
        child: Scaffold(
          appBar: AppBar(
            //导航栏
            title: Text(userName??Translations.textOf(context, "all.app.name")),
//          actions: <Widget>[
//            //导航栏右侧菜单
//            IconButton(icon: Icon(Icons.share), onPressed: () {}),
//          ],
          ),
          drawer: HomeDrawer(), //抽屉
//          floatingActionButton: FloatingActionButton(
//              //悬浮按钮
//              child: Icon(Icons.add),
//              onPressed: _onAdd),
//             body: Text('Test '),
          body: GridView.builder(
            gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                crossAxisCount: 3, //每行三列
                childAspectRatio: 1.4 //显示区域宽高相等

                ),
            itemCount: menuItems.length,
            itemBuilder: (context, index) {
              return GestureDetector(
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
    menuItems.clear();
    Future.delayed(const Duration(milliseconds: 200)).then((e) {
      setState(() {
        menuItems.addAll([
          MenuItem(
              Icons.school_outlined,
              Translations.textOf(context, "home.menu.school.card"),
              RouteNameConstant.route_name_school_card),

          // MenuItem(
          //     Icons.card_travel_outlined,
          //     Translations.textOf(context, "home.menu.name.card"),
          //     RouteNameConstant.route_name_name_card),

          MenuItem(Icons.tv, Translations.textOf(context, "home.menu.tv"),
              RouteNameConstant.route_name_tv_list),
        ]);
      });
    });
  }

  Future<bool> onWillPop() async {
    if (DateTime.now().difference(_lastTime) > Duration(seconds: 1)) {
      //两次点击间隔超过1s重新计时
      _lastTime = DateTime.now();
      Toasts.show(Translations.textOf(context, "home.back.exit.double.tips"));
      return false;
    }
    AppUtils.exitApp();
    return true;
  }

}
