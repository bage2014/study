
class TvCaches{

  static int _index = 0;

  static int getTvIndex(int id){
    return _index;
  }

  static void setTvIndex(int id,int index){
    _index = index;
  }

}