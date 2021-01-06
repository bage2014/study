import 'package:flutter/material.dart';
import 'package:flutter_study/component/http/HttpRequests.dart';
import 'package:flutter_study/component/http/HttpResult.dart';
import 'package:flutter_study/component/log/Logs.dart';
import 'package:flutter_study/constant/RouteNameConstant.dart';

class TvList extends StatefulWidget {
  @override
  _ScaffoldRouteState createState() => _ScaffoldRouteState();
}

class _ScaffoldRouteState extends State<TvList> {
  int _selectedIndex = 1;
  static const loadingTag = "##loading##"; //表尾标记
  var _words = <String>[loadingTag];

  @override
  void initState() {
    super.initState();
    _retrieveData();
  }

  @override
  Widget build(BuildContext context) {
    //获取路由参数
    var args=ModalRoute.of(context).settings.arguments;
    print('args=' + args);

    return Scaffold(
      appBar: AppBar(
        title: Text("TV List"),
      ),
      bottomNavigationBar: BottomNavigationBar( // 底部导航
        items: <BottomNavigationBarItem>[
          BottomNavigationBarItem(icon: Icon(Icons.home), label: 'All'),
          BottomNavigationBarItem(icon: Icon(Icons.business), label: 'Favorite'),
        ],
        currentIndex: _selectedIndex,
        fixedColor: Colors.blue,
        onTap: _onItemTapped,
      ),
        body: new Center(
          child: ListView.separated(
            itemCount: _words.length,
            itemBuilder: (context, index) {
              return new GestureDetector(
                onTap: () {
                  //处理点击事件
                  print("index--" + index.toString());
                  Navigator.of(context).pushNamed(RouteNameConstant.route_name_tv_player, arguments: "hi");
                },
                child: ListTile(title: Text(_words[index])),
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

  void _retrieveData() {
    HttpRequests.get("/tv/query/all", null, null).then((result){
      Logs.info('responseBody=' + result?.responseBody);
    });

    Future.delayed(Duration(seconds: 2)).then((e) {
      setState(() {
        //重新构建列表
        for(var i = 0; i< 10; i++){
          _words.add("hello-" + i.toString());
        }
      });
    });
  }

}
