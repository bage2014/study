import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_study/component/http/HttpRequests.dart';
import 'package:flutter_study/component/log/Logs.dart';
import 'package:flutter_study/constant/RouteNameConstant.dart';
import 'package:flutter_study/model/QueryTvResult.dart';

class TvList extends StatefulWidget {
  @override
  _ScaffoldRouteState createState() => _ScaffoldRouteState();
}

class _ScaffoldRouteState extends State<TvList> {
  int _selectedIndex = 1;
  List<TvItem> list;

  @override
  void initState() {
    super.initState();
    list = [];
    _onRefresh();
  }

  @override
  Widget build(BuildContext context) {
    //获取路由参数
    var args = ModalRoute.of(context).settings.arguments;
    print('args=' + args);

    return Scaffold(
      appBar: AppBar(
        title: Text("TV List"),
      ),
      bottomNavigationBar: BottomNavigationBar(
        // 底部导航
        items: <BottomNavigationBarItem>[
          BottomNavigationBarItem(icon: Icon(Icons.home), label: 'All'),
          BottomNavigationBarItem(
              icon: Icon(Icons.business), label: 'Favorite'),
        ],
        currentIndex: _selectedIndex,
        fixedColor: Colors.blue,
        onTap: _onItemTapped,
      ),
      body: RefreshIndicator(
        onRefresh: _onRefresh,
        child: ListView.separated(
          itemCount: list.length,
          itemBuilder: (context, index) {
            return new GestureDetector(
              onTap: () {
                //处理点击事件
                Navigator.of(context).pushNamed(
                    RouteNameConstant.route_name_tv_player,
                    arguments: list[index]);
              },
              child: ListTile(title: Text(list[index].name)),
            );
            //显示单词列表项
          },
          separatorBuilder: (context, index) => Divider(height: .0),
        ),
      ),

    );
  }

  void _onItemTapped(int index) {
    setState(() {
      _selectedIndex = index;
    });
  }

  Future<Null> _onRefresh() async {
    HttpRequests.get("/tv/query/all", null, null).then((result) {
      Logs.info('responseBody=' + result?.responseBody);
      setState(() {
        QueryTvResult tvResult =
            QueryTvResult.fromJson(json.decode(result?.responseBody));
        if (tvResult.code == 200) {
          list = tvResult.data;
        }
      });
    });
  }
}
