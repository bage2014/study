
class Caches{

  static int _userId = 0;

  static int getUserId(){
    return _userId;
  }

  static void setUserId(int userId){
    _userId = userId;
  }

}