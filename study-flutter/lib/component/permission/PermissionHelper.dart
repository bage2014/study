import 'package:flutter_study/component/log/Logs.dart';
import 'package:permission_handler/permission_handler.dart';

class PermissionHelper {
  static Future<bool> requestPermissions() async {
    Map<Permission, PermissionStatus> statuses =
        await [Permission.storage].request();
    statuses.forEach((key, value) {
      if (!value.isGranted) {
        Logs.info('${key} is not Granted!!');
        return false;
      }
    });
    return true;
  }

  static Future<bool> _requestPermission(Permission permission) async {
    return permission.request().isGranted;
  }
}
