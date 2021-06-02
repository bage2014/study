import 'package:flutter/material.dart';
import 'package:flutter_study/component/log/Logs.dart';
import 'package:flutter_study/locale/Translations.dart';

class AboutAuthor extends StatefulWidget {
  @override
  _AboutAuthor createState() => new _AboutAuthor();
}

class _AboutAuthor extends State<AboutAuthor> {
  List<String> list = [];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(Translations.textOf(context, "about.author.title")),
      ),
      body: Container(
        alignment: Alignment.center,
        child: Column(children: <Widget>[
          Container(
            padding: const EdgeInsets.fromLTRB(24.0,8.0,0.0,0.0),
            child: Row(
              children: <Widget>[
                Image(image: AssetImage("assets/images/user_null.png")),
                Container(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center, //垂直方向居中对齐
                    children: <Widget>[
                      Text('陆瑞华',textScaleFactor: 1.5),
                      Text("上海")
                    ],
                  ),
                ),
              ],
            ),
          ),
          Container(
            padding: const EdgeInsets.fromLTRB(16.0,16.0,16.0,16.0),
            child: Text('广西日常饮食主要以清淡为主，但境内南北亦有些许变化，在桂东南的玉林、梧州、贺州、南宁、北海、钦州等主要以清淡，在北部的柳州、桂林等部分地区有吃辣习惯。据旅行家徐霞客所记载，广西境内由于地理环境的关系，其早期的饮食已有种食稻谷和蔬菜、腌菜、饮茶、行酒礼等习惯。其中以桂林米粉、柳州螺蛳粉、南宁老友粉、钦州猪脚粉、桂林两江松花糖、防城牛腩粉、梧州龟苓膏、巴马香猪、宾阳白切狗、宾阳酸粉、玉林牛巴、玉林云吞、玉林酸料以及各地的米粉、切粉、滤粉等为代表。'),
//            child: Text(Translations.textOf(context, "about.author.name")),
          ),
          Container(
            alignment: Alignment.centerLeft,
            child: RefreshIndicator(
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
                  return new GestureDetector(
                      onTap: () {
                        Logs.info('onTap index = $index');
                      },
                      child: ListTile(
                          title: Text(list[index]),
                          trailing: new GestureDetector(
                              child: Icon(
                                Icons.favorite,
                                color: Colors.red,
                              ))));
                },
                separatorBuilder: (context, index) => Divider(height: .0),
              ),
            ),
//            child: Text(Translations.textOf(context, "about.author.name")),
          ),
        ]),
      ),
    );
  }

  Future<Null> _onRefresh() async {
//    Map<String, dynamic> paramJson = new HashMap();
//    paramJson.putIfAbsent("targetPage", () => 1);
//    paramJson.putIfAbsent("pageSize", () => 100);
//    Map<String, String> param = new HashMap();
//    param.putIfAbsent("param", () => json.encode(paramJson));
//    print(json.encode(paramJson));
    list.add("hello");
//    HttpRequests.get(HttpConstant.url_tv_query_page, param, null)
//        .then((result) {
//      Logs.info('_onRefresh responseBody=' + result?.responseBody);
//      setState(() {
//        QueryTvResult tvResult =
//        QueryTvResult.fromJson(json.decode(result?.responseBody));
//        if (tvResult.code == 200) {
//          list = tvResult.data;
//        }
//      });
//    }).catchError((error) {
//      print(error.toString());
//    });
  }

}
