import 'package:flutter/cupertino.dart';
import 'package:shared_preferences/shared_preferences.dart';

class providers extends ChangeNotifier{
  bool _heelo =false;
  bool _smil =false;

  bool getheelo() => _heelo;
  bool getsmil() => _smil;

  providers(){
    loadSharedPref();
  }
  void setheelo(bool value) {
    _heelo = value;
    notifyListeners();
    print("setHello${value}");
    setSharedPrefhell();
  }

  void setsmil(bool value) {
    _smil = value;
    notifyListeners();
    print("setSmil${value}");
    setSharedPrefsmile();
  }

  void setSharedPrefhell() async{
    SharedPreferences sharedPreferences = await SharedPreferences.getInstance();
    sharedPreferences.setBool('hell', _heelo);
  }
  void setSharedPrefsmile() async{
    SharedPreferences sharedPreferences = await SharedPreferences.getInstance();
    sharedPreferences.setBool('smile', _smil);
  }

  void loadSharedPref() async{
    SharedPreferences sharedPreferences = await SharedPreferences.getInstance();
    bool Hello = sharedPreferences.getBool('hell');
    bool smil = sharedPreferences.getBool('smile');
    if(Hello!=null)setheelo(Hello);
    if(smil!=null)setsmil(smil);
  }

}