import 'package:flutter/material.dart';

class MembersView extends StatelessWidget {
  final String? familyId;

  const MembersView({Key? key, this.familyId}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('成员管理'),
        centerTitle: true,
      ),
      body: Center(
        child: Text('成员管理视图 - 家族ID: $familyId'),
      ),
    );
  }
}
