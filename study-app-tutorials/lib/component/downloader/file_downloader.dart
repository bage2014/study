import 'package:flutter_cache_manager/flutter_cache_manager.dart';

class FileDownloader {

  static Stream<FileResponse> download(String url) {
    return DefaultCacheManager().getFileStream(url, withProgress: true);
  }
}
