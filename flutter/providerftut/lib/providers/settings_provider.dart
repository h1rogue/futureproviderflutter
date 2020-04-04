
import 'package:flutter/cupertino.dart';
import 'package:shared_preferences/shared_preferences.dart';

class SettingsProvider extends ChangeNotifier{
  String _units;
  List<String> _waxLines;

  SettingsProvider(){
    _units='Imperial';
    _waxLines = ['Swix','Toko'];
   loadPreferences();
  }

  //Getters
  String get units => _units;
  List<String> get waxLines => _waxLines;

  //Setters
  void setUnits(String units){
    _units = units;
    notifyListeners();
   savePreferences();
  }

  void _setWaxLines(List<String> waxLines){
    _waxLines = waxLines;
    notifyListeners();
  }

  void addWaxLine(String waxLine){
    if (_waxLines.contains(waxLine) == false){
      _waxLines.add(waxLine);
      notifyListeners();
      savePreferences();
    }
  }

  void removeWaxLine(String waxLine){
    if (_waxLines.contains(waxLine) == true){
      _waxLines.remove(waxLine);
      notifyListeners();
      savePreferences();
    }
  }


   savePreferences() async {
    SharedPreferences sharedPreferences = await SharedPreferences.getInstance();
    sharedPreferences.setString('units', _units);
    sharedPreferences.setStringList('list', _waxLines);
  }

  loadPreferences() async {
    SharedPreferences sharedPreferences = await SharedPreferences.getInstance();
    String unit = sharedPreferences.getString('units');
    List<String> unitlist = sharedPreferences.getStringList('list');
    if(unit!=null) setUnits(unit);
    if(unitlist!=null)_setWaxLines(unitlist);
  }
}