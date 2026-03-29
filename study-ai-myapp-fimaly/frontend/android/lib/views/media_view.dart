import 'package:flutter/material.dart';

class MediaView extends StatelessWidget {
  final String? familyId;

  const MediaView({Key? key, this.familyId}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('媒体管理'),
        centerTitle: true,
      ),
      body: Center(
        child: Text('媒体管理视图 - 家族ID: $familyId'),
      ),
    );
  }
}
