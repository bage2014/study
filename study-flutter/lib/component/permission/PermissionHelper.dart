import 'package:permission_handler/permission_handler.dart';

class PermissionHelper {

  static Future<bool> requestPermission() async {
    return _requestPermissions(Permission.storage);
  }

  static Future<bool> _requestPermissions(Permission permission) async {
    return permission.isGranted;
  }
}
