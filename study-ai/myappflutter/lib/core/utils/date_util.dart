class DateUtil {
  // 日期格式：yyyy-MM-dd
  static String formatDate(DateTime date) {
    return '${date.year}-${_twoDigits(date.month)}-${_twoDigits(date.day)}';
  }

  // 日期时间格式：yyyy-MM-dd HH:mm:ss
  static String formatDateTime(DateTime date) {
    return '${date.year}-${_twoDigits(date.month)}-${_twoDigits(date.day)} ${_twoDigits(date.hour)}:${_twoDigits(date.minute)}:${_twoDigits(date.second)}';
  }

  // 确保数字是两位数
  static String _twoDigits(int n) {
    return n >= 10 ? '$n' : '0$n';
  }

  // 解析日期字符串 yyyy-MM-dd
  static DateTime? parseDate(String dateString) {
    try {
      List<String> parts = dateString.split('-');
      if (parts.length != 3) return null;

      int year = int.parse(parts[0]);
      int month = int.parse(parts[1]);
      int day = int.parse(parts[2]);

      return DateTime(year, month, day);
    } catch (e) {
      return null;
    }
  }

  // 解析日期时间字符串 yyyy-MM-dd HH:mm:ss
  static DateTime? parseDateTime(String dateTimeString) {
    try {
      List<String> parts = dateTimeString.split(' ');
      if (parts.length != 2) return null;

      List<String> dateParts = parts[0].split('-');
      if (dateParts.length != 3) return null;

      List<String> timeParts = parts[1].split(':');
      if (timeParts.length != 3) return null;

      int year = int.parse(dateParts[0]);
      int month = int.parse(dateParts[1]);
      int day = int.parse(dateParts[2]);
      int hour = int.parse(timeParts[0]);
      int minute = int.parse(timeParts[1]);
      int second = int.parse(timeParts[2]);

      return DateTime(year, month, day, hour, minute, second);
    } catch (e) {
      return null;
    }
  }
}