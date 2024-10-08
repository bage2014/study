class DateTimeUtils {
  ///
  /// 2021-07-26 12:31:05
  ///
  static DateTime parse(String? dateTimeStr) {
    if (dateTimeStr == null) {
      return DateTime.now();
    }
    return DateTime.parse(dateTimeStr);
  }

  ///
  /// 2021-07-26 12:31:05
  ///
  static String? subYear(String? dateTimeStr) {
    if (dateTimeStr == null) {
      return '';
    }
    return dateTimeStr.substring(0,4);
  }

  ///
  /// 2021-07-26T12:31:05
  ///
  static DateTime parseFromJson(String? dateTimeStr) {
    if (dateTimeStr == null) {
      return DateTime.now();
    }
    dateTimeStr = dateTimeStr.replaceAll('T', ' ');
    return DateTime.parse(dateTimeStr);
  }

  ///
  /// 2021-07-26T12:31:05
  ///
  static String format(DateTime dateTime) {
    return dateTime.toIso8601String();
  }
}
