import 'dart:collection';
import 'dart:convert';

import 'package:app_lu_lu/component/cache/TvCaches.dart';
import 'package:app_lu_lu/component/cache/UserCaches.dart';
import 'package:app_lu_lu/component/dialog/Dialogs.dart';
import 'package:app_lu_lu/component/http/HttpRequests.dart';
import 'package:app_lu_lu/component/log/Logs.dart';
import 'package:app_lu_lu/constant/HttpConstant.dart';
import 'package:app_lu_lu/constant/RouteNameConstant.dart';
import 'package:app_lu_lu/locale/Translations.dart';
import 'package:app_lu_lu/model/QueryTvResult.dart';
import 'package:flutter/material.dart';

class TV extends StatefulWidget {
  @override
  _TV createState() => _TV();
}

class _TV extends State<TV> {
  List<TvItem> list = [];
  int _currentIndex = 1;

  @override
  void initState() {
    super.initState();
    list = [];
    _onRefresh();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(Translations.textOf(context, "tv.list.title")),
      ),
      bottomNavigationBar: BottomNavigationBar(
        // 底部导航
        items: <BottomNavigationBarItem>[
          BottomNavigationBarItem(
              icon: Icon(Icons.favorite_border),
              label: Translations.textOf(context, "tv.list.bottomAll")),
          BottomNavigationBarItem(
              icon: Icon(Icons.favorite),
              label: Translations.textOf(context, "tv.list.bottomFavorite")),
        ],
        currentIndex: _currentIndex,
        fixedColor: Colors.blue,
        onTap: _onItemTapped,
      ),
      body: RefreshIndicator(
        onRefresh: _onRefresh,
        child: list.length == 0
            ? Center(
                child: Text(
                  Translations.textOf(context, "all.list.view.no.data"),
                  textAlign: TextAlign.center,
                ),
              )
            : ListView.separated(
                itemCount: list.length,
                itemBuilder: (context, index) {
                  int urlIndex = TvCaches.getTvIndex(list[index]?.id ?? 0);
                  String urlName = "【路线${urlIndex + 1}】";
                  String name = '${urlName}${list[index].name ?? ""}';
                  bool isFavorite = list[index].isFavorite ?? false;
                  return new GestureDetector(
                      onTap: () {
                        Navigator.of(context).pushNamed(
                            RouteNameConstant.route_name_tv_player,
                            arguments: list[index]);
                      },
                      onDoubleTap: () {
                        List<String> urls = list[index]?.urls ?? [];
                        List<String> contents = [];
                        for (int i = 0; i < urls.length; i++) {
                          contents.add("【路线${i + 1}】");
                        }
                        Dialogs.showButtonSelectDialog(context, contents).then(
                            (value) =>
                                {updateUrlIndex(list[index]?.id ?? 0, value)});
                      },
                      child: ListTile(
                          title: Text(name),
                          trailing: new GestureDetector(
                              onTap: () {
                                _setFavorite(list[index]);
                              },
                              child: (isFavorite != null && isFavorite)
                                  ? Icon(
                                      Icons.favorite,
                                      color: Colors.red,
                                    )
                                  : Icon(Icons.favorite_border))));
                },
                separatorBuilder: (context, index) => Divider(height: .0),
              ),
      ),
    );
  }

  void _onItemTapped(int index) {
    setState(() {
      _currentIndex = index;
      _onRefresh();
    });
  }

  Future<Null> _onRefresh() async {
    Map<String, dynamic> header = new HashMap();
    header.putIfAbsent("favoriteUserId", () => UserCaches.getUserId());

    Map<String, dynamic> paramJson = new HashMap();
    paramJson.putIfAbsent("favoriteUserId", () => UserCaches.getUserId());
    paramJson.putIfAbsent("isOnlyFavorite", () => _currentIndex == 1);
    paramJson.putIfAbsent("targetPage", () => 1);
    paramJson.putIfAbsent("pageSize", () => 100);
    Map<String, String> param = new HashMap();
    param.putIfAbsent("param", () => json.encode(paramJson));
    HttpRequests.get(HttpConstant.url_tv_query_page, param, null)
        .then((result) {
      Logs.info('_onRefresh responseBody=' + (result?.responseBody ?? ""));
      setState(() {
        QueryTvResult tvResult =
            QueryTvResult.fromJson(json.decode(result?.responseBody ?? ""));
        if (tvResult.code == 200) {
          list = tvResult.data ?? [];
        }
      });
    }).catchError((error) {
      print(error.toString());
    });
  }

  Future<Null> _setFavorite(TvItem item) async {
    item.isFavorite = !(item?.isFavorite ?? false);
    item.userId = UserCaches.getUserId();
    Map<String, String> param = new HashMap();
    param.putIfAbsent("param", () => json.encode(item.toJson()));
    print(json.encode(item.toJson()));
    HttpRequests.post(HttpConstant.url_tv_set_favorite, param, null)
        .then((result) {
      Logs.info('_setFavorite responseBody=' + (result?.responseBody ?? ""));
      _onRefresh();
    });
  }

  updateUrlIndex(int id, int? index) {
    Logs.info('index = $index');
    if (index != null) {
      Logs.info('index = $index');
      TvCaches.setTvIndex(id,index);
    }
  }
}
