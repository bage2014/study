import 'package:flutter/material.dart';

typedef DataLoader<T> = Future<T> Function();
typedef ContentBuilder<T> = Widget Function(T data);

class LoadingWrapper<T> extends StatelessWidget {
  final DataLoader<T> loader;
  final ContentBuilder<T> builder;
  final Widget? loadingIndicator;

  const LoadingWrapper({
    super.key,
    required this.loader,
    required this.builder,
    this.loadingIndicator,
  });

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<T>(
      future: loader(),
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.done) {
          if (snapshot.hasData) {
            return builder(snapshot.data!);
          }
          return const Center(child: Text('加载失败'));
        }
        return loadingIndicator ?? const Center(child: CircularProgressIndicator());
      },
    );
  }
}