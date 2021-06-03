import 'package:app_lu_lu/component/log/Logs.dart';
import 'package:permission_handler/permission_handler.dart';

class PermissionHelper {
  static Future<bool?> requestPermissions() async {
    Map<Permission, PermissionStatus> statuses =
        await [Permission.storage,Permission.activityRecognition].request();
    bool result = true;
    statuses.forEach((key, value) {
      if (!value.isGranted) {
        Logs.info('${key} is not Granted!!');
        result = false;
      }
    });
    return result;
  }

  static Future<bool> _requestPermission(Permission permission) async {
    return permission.request().isGranted;
  }
}
