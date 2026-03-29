import 'package:flutter/material.dart';

class MemberDetailView extends StatelessWidget {
  final String? memberId;

  const MemberDetailView({Key? key, this.memberId}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('成员详情'),
        centerTitle: true,
      ),
      body: Center(
        child: Text('成员详情视图 - 成员ID: $memberId'),
      ),
    );
  }
}
