import 'package:flutter/material.dart';

class FamilyTreeView extends StatelessWidget {
  final String? familyId;

  const FamilyTreeView({Key? key, this.familyId}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('家族树'),
        centerTitle: true,
      ),
      body: Center(
        child: Text('家族树视图 - 家族ID: $familyId'),
      ),
    );
  }
}
