import 'dart:io';

import 'package:open_file/open_file.dart';
import 'package:path_provider/path_provider.dart';
import 'package:tutorials/component/log/Logs.dart';

class FileUtils {

  static void openFile(File file) {
    OpenFile.open(file.path)
        .then((value) => print('openFile then value = ${value.message}'))
        .catchError((error) => {print('openFile catchError error = $error')});
  }

  static Future<Directory?> getDownloadDir() async {
    return getTemporaryDirectory();
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
