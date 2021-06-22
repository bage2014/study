import 'package:app_lu_lu/locale/Translations.dart';
import 'package:flutter/material.dart';

import 'PieChartSample.dart';

class FamilyEvents extends StatefulWidget {
  @override
  _FamilyEvents createState() => new _FamilyEvents();
}

class _FamilyEvents extends State<FamilyEvents> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text(Translations.textOf(context, "about.author.title")),
        ),
        body: PieChartSample());
  }
}
