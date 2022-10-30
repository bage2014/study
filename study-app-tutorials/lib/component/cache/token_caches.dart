
import 'package:tutorials/component/sp/shared_preference_helper.dart';
import 'package:tutorials/constant/sp_constant.dart';

class TokenCaches{

  static Future<String> getAccessToken(){
    return SharedPreferenceHelper.get(SpConstant.token_access_key,'');
  }
  
  static Future<bool> cacheAccessToken(String accessToken){
    return SharedPreferenceHelper.set(SpConstant.token_access_key, accessToken);
  }

  static Future<bool> clearAccessToken(){
    return SharedPreferenceHelper.set(SpConstant.token_access_key, '');
  }

  static Future<String> getRefreshToken(){
    return SharedPreferenceHelper.get(SpConstant.token_refresh_key,'');
  }
  
  static Future<bool> cacheRefreshToken(String refreshToken){
    return SharedPreferenceHelper.set(SpConstant.token_refresh_key, refreshToken);
  }

  static Future<bool> clearRefreshToken(){
    return SharedPreferenceHelper.set(SpConstant.token_refresh_key, '');
  }

}