import 'package:flutter/material.dart';

class EventsView extends StatelessWidget {
  final String? familyId;

  const EventsView({Key? key, this.familyId}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('事件管理'),
        centerTitle: true,
      ),
      body: Center(
        child: Text('事件管理视图 - 家族ID: $familyId'),
      ),
    );
  }
}
