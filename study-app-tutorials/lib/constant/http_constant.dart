

class HttpConstant{

  static int server_not_response = 205;
  static String server_not_response_msg = 'server not response';

  static String url_login = "/oauth/token";
  static String url_register = "/oauth/token";
  static String url_user_profile = "/api/authUser/profile/detail";
  static String url_validate_code = "/validate/code/generate";

  static String url_tv_query_page = "/ignore/api/tv/query/page";
  static String url_tv_set_favorite = "/ignore/android/tv/favorite/set";

  static String url_settings_version_check = "/ignore/android/app/version/check/{version}";
  static String url_settings_app_download = "/ignore/android/app/version/check/{version}";
  static String url_settings_app_latest = "/ignore/android/app/version/latest";
  static String url_settings_app_versions = "/ignore/android/app/version/query";

  static String url_settings_app_feedback_query = "/ignore/feedbackMessage/query/page";
  static String url_settings_app_feedback_insert = "/ignore/feedbackMessage/insert";
  static String url_settings_app_feedback_delete = "/ignore/feedbackMessage/delete";

  static String url_profile_activity = "/ignore/api/profile/query/page";

}


