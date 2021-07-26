import 'package:app_lu_lu/component/dialog/Dialogs.dart';
import 'package:app_lu_lu/component/log/Logs.dart';
import 'package:app_lu_lu/constant/RouteNameConstant.dart';
import 'package:app_lu_lu/locale/Translations.dart';
import 'package:app_lu_lu/utils/AppUtils.dart';
import 'package:flutter/material.dart';

class HomeDrawer extends StatelessWidget {
  HomeDrawer({
    Key? key,
  }) : super(key: key);

  late BuildContext _context;

  @override
  Widget build(BuildContext context) {
    _context = context;
    return Drawer(
      child: MediaQuery.removePadding(
        context: context,
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            Padding(
              padding: const EdgeInsets.only(top: 48),
              child: Row(
                children: <Widget>[
                  Padding(
                    padding: const EdgeInsets.only(left: 24),
                    child: ClipOval(
                      child:
                          Image(image: AssetImage("assets/images/logo128.png")),
                    ),
                  ),
                  Text(Translations.textOf(context, "all.app.name"),
                      style: TextStyle(
                          fontSize: 20.0, fontWeight: FontWeight.bold))
                ],
              ),
            ),
            Expanded(
              child: ListView(
                padding: const EdgeInsets.only(left: 24),
                children: <Widget>[
                  ListTile(
                    leading: const Icon(Icons.settings),
                    title: Text(
                        Translations.textOf(context, "home.drawer.settings")),
                    onTap: () {
                      Navigator.of(context)
                          .pushNamed(RouteNameConstant.route_name_settings);
                    },
                  ),
                  ListTile(
                    leading: const Icon(Icons.info),
                    title:
                        Text(Translations.textOf(context, "home.drawer.about")),
                    onTap: () {
                      Navigator.of(context)
                          .pushNamed(RouteNameConstant.route_name_about);
                    },
                  ),
                  ListTile(
                    leading: const Icon(Icons.exit_to_app),
                    title:
                        Text(Translations.textOf(context, "home.drawer.exit")),
                    onTap: () {
                      // 确认框
                      checkExitApp();
                    },
                  ),
//                  ListTile(
//                    leading: const Icon(Icons.person),
//                    title: Text(Translations.textOf(context, "home.drawer.profile")),
//                    onTap: () {
//                      Navigator.of(context)
//                          .pushNamed(RouteNameConstant.route_name_profile);
//                    },
//                  ),
//                  ListTile(
//                    leading: const Icon(Icons.logout),
//                    title: const Text('Logout'),
//                    onTap: () {
//
//                    },
//                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  Future<void> checkExitApp() async {
    String? showConfirmDialog = await Dialogs.showConfirmDialog(
        _context, Translations.textOf(_context, "home.back.confirm"), null);
    Logs.info('checkExitApp showConfirmDialog = $showConfirmDialog');
    if ("true" == showConfirmDialog) {
      AppUtils.exitApp();
    }
  }
}
