import 'package:shared_preferences/shared_preferences.dart';

class SharedPreferenceHelper {
  static Future<SharedPreferences> _getInstance() {
    return SharedPreferences.getInstance();
  }

  static Future<bool> set(String key, String value) async {
    SharedPreferences _prefs = await _getInstance();
    return _prefs.setString(key, value);
  }

  static Future<String> get(String key, String defaultValue) async {
    SharedPreferences _prefs = await _getInstance();
    return _prefs.getString(key)??defaultValue;
  }
}
