import 'dart:io';

import 'package:open_file/open_file.dart';
import 'package:path_provider/path_provider.dart';
import 'package:share/share.dart';
import 'package:tutorials/component/cache/setting_caches.dart';
import 'package:tutorials/component/log/Logs.dart';
import 'package:tutorials/component/sp/SharedPreferenceHelper.dart';
import 'package:tutorials/constant/sp_constant.dart';

class FileUtils {
  static void openFile(File file) {
    OpenFile.open(file.path)
        .then((value) => print('openFile then value = ${value.message}'))
        .catchError((error) => {print('openFile catchError error = $error')});
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
