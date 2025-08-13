// import 'dart:collection';
// import 'dart:convert';
// import 'dart:io';


// class TV extends StatefulWidget {
//   @override
//   _TV createState() => _TV();
// }

// class _TV extends State<TV> {
//   List<TvItem> list = [];
//   int _currentIndex = 1;
//   bool _isLoading = false;

//   @override
//   void initState() {
//     super.initState();
//     list = [];
//     _isLoading = false;
//     _onRefresh();
//   }

//   @override
//   Widget build(BuildContext context) {
//     return Scaffold(
//       appBar: AppBar(
//         title: Text(Translations.textOf(context, "tv.list.title")),
//       ),
//       bottomNavigationBar: BottomNavigationBar(
//         // 底部导航
//         items: <BottomNavigationBarItem>[
//           BottomNavigationBarItem(
//               icon: Icon(Icons.favorite_border),
//               label: Translations.textOf(context, "tv.list.bottomAll")),
//           BottomNavigationBarItem(
//               icon: Icon(Icons.favorite),
//               label: Translations.textOf(context, "tv.list.bottomFavorite")),
//         ],
//         currentIndex: _currentIndex,
//         fixedColor: Colors.blue,
//         onTap: _onItemTapped,
//       ),
//       body: ConstrainedBox(
//         constraints: BoxConstraints.expand(),
//         child: Stack(
//           alignment: Alignment.center, //指定未定位或部分定位widget的对齐方式
//           children: <Widget>[
//             RefreshIndicator(
//               onRefresh: _onRefresh,
//               child: list.length == 0
//                   ? Center(
//                 child: Text(
//                   Translations.textOf(context, "all.list.view.no.data"),
//                   textAlign: TextAlign.center,
//                 ),
//               )
//                   : ListView.separated(
//                 itemCount: list.length,
//                 itemBuilder: (context, index) {
//                   int urlIndex =
//                   TvCaches.getTvIndex(list[index]?.id ?? 0);
//                   String urlName = "【路线${urlIndex + 1}】";
//                   String name = '${urlName}${list[index].name ?? ""}';
//                   bool isFavorite = list[index].isFavorite ?? false;
//                   return new GestureDetector(
//                       onTap: () {
//                         Navigator.of(context).pushNamed(
//                             RouteNameConstant.route_name_tv_player,
//                             arguments: list[index]);
//                       },
//                       onDoubleTap: () {
//                         List<String> urls = list[index]?.urls ?? [];
//                         List<String> contents = [];
//                         List<Icon> icons = [];
//                         for (int i = 0; i < urls.length; i++) {
//                           contents.add("播放路线${i + 1}");
//                           icons.add(i ==
//                               TvCaches.getTvIndex(
//                                   list[index]?.id ?? 0)
//                               ? Icon(
//                             Icons.check_circle,
//                             color: Colors.blue,
//                           )
//                               : Icon(Icons.check_circle_outline));
//                         }
//                         Dialogs.showButtonSelectDialog(
//                             context, contents, icons)
//                             .then((value) =>
//                         {
//                           updateUrlIndex(
//                               list[index]?.id ?? 0, value)
//                         });
//                       },
//                       child: ListTile(
//                           title: Text(name),
//                           trailing: new GestureDetector(
//                               onTap: () {
//                                 _setFavorite(list[index]);
//                               },
//                               child: (isFavorite != null && isFavorite)
//                                   ? Icon(
//                                 Icons.favorite,
//                                 color: Colors.red,
//                               )
//                                   : Icon(Icons.favorite_border))));
//                 },
//                 separatorBuilder: (context, index) => Divider(height: .0),
//               ),
//             ),
//             Container(
//               child: _isLoading ? CircularProgressIndicator(
//                 backgroundColor: Colors.grey[200],
//                 valueColor: AlwaysStoppedAnimation(Colors.blue),
//               ) : null,
//             ),
//           ],
//         ),
//       ),
//     );
//   }

//   void _onItemTapped(int index) {
//     setState(() {
//       _currentIndex = index;
//       _isLoading = true;
//     });
//     _onRefresh();
//   }

//   Future<Null> _onRefresh() async {
//     Map<String, dynamic> header = new HashMap();
//     header.putIfAbsent("favoriteUserId", () => UserCaches.getUserId());

//     Map<String, dynamic> paramJson = new HashMap();
//     paramJson.putIfAbsent("favoriteUserId", () => UserCaches.getUserId());
//     paramJson.putIfAbsent("isOnlyFavorite", () => _currentIndex == 1);
//     paramJson.putIfAbsent("targetPage", () => 1);
//     paramJson.putIfAbsent("pageSize", () => 100);
//     Map<String, String> param = new HashMap();
//     param.putIfAbsent("param", () => json.encode(paramJson));
//     HttpRequests.get(HttpConstant.url_tv_query_page, param, null)
//         .then((result) {
//       Logs.info('_onRefresh responseBody=' + (result?.responseBody ?? ""));
//       hideLoading();
//       setState(() {
//         QueryTvResult tvResult =
//         QueryTvResult.fromJson(json.decode(result?.responseBody ?? ""));
//         if (tvResult.code == 200) {
//           list = tvResult.data ?? [];
//         }
//       });
//     }).catchError((error) {
//       print(error.toString());
//       hideLoading();
//     });
//   }

//   Future<Null> _setFavorite(TvItem item) async {
//     item.isFavorite = !(item?.isFavorite ?? false);
//     item.userId = UserCaches.getUserId();
//     Map<String, String> param = new HashMap();
//     param.putIfAbsent("param", () => json.encode(item.toJson()));
//     print(json.encode(item.toJson()));
//     HttpRequests.post(HttpConstant.url_tv_set_favorite, param, null)
//         .then((result) {
//       Logs.info('_setFavorite responseBody=' + (result?.responseBody ?? ""));
//       _onRefresh();
//     });
//   }

//   updateUrlIndex(int id, int? index) {
//     Logs.info('index = $index');
//     if (index != null) {
//       TvCaches.setTvIndex(id, index);
//       _onRefresh();
//     }
//   }

//   hideLoading() {
//     setState(() {
//       _isLoading = false;
//     });
//   }

// }
