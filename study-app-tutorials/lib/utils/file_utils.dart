import 'dart:io';

import 'package:open_file/open_file.dart';
import 'package:tutorials/component/cache/setting_caches.dart';
import 'package:tutorials/component/log/logs.dart';

class FileUtils {
  static void openFile(File file) {
    OpenFile.open(file.path)
        .then((value) => Logs.info('openFile then value = ${value.message}'))
        .catchError((error) => {Logs.info('openFile catchError error = $error')});
  }

  static Future<String> getDownloadDirectory() async {
    return SettingCaches.getDownloadDirectory();
  }

  static Future<bool> write(File file, var bytes) async {
    try {
      var raf = file.openSync(mode: FileMode.write);
      raf.writeFromSync(bytes);
      await raf.close();
    } catch (e) {
      Logs.info(e.toString());
      return false;
    }
    return true;
  }
}
