import 'package:flutter/material.dart';
import 'package:flutter_study/constant/RouteNameConstant.dart';
import 'package:flutter_study/locale/Translations.dart';

class HomeDrawer extends StatelessWidget {
  const HomeDrawer({
    Key key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
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
}
