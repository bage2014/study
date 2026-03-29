import 'package:flutter/material.dart';

class FamilyManagementView extends StatelessWidget {
  final String? familyId;

  const FamilyManagementView({Key? key, this.familyId}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('家族管理'),
        centerTitle: true,
      ),
      body: Center(
        child: Text('家族管理视图 - 家族ID: $familyId'),
      ),
    );
  }
}
