
import 'package:tutorials/request/model/user.dart';

class UserCaches{

  static int _userId = 0;
  static User _user = User();

  static int getUserId(){
    return _userId;
  }

  static void cacheUserId(int userId){
    _userId = userId;
  }

  static void cacheUser(User user){
    _user = user??_user;
    _user.id = user.id??_userId;
  }

  static User getUser(){
    return _user??User();
  }
}