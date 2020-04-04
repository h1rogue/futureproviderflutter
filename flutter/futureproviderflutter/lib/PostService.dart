import 'dart:convert';
import 'package:flutter/foundation.dart';
import 'package:http/http.dart' as http;
import 'package:futureproviderflutter/ListModel.dart';
import 'package:provider/provider.dart';

class PostService {
  String _q="cats";

  PostService(){}

   String getq()=>_q;

  void setq(String value) {
    _q = value;
  }

  Future<List<TileModel>> fetchDatafromApi(value) async {
    String key = "9333513-0b2be1ae0460ba320de873480";
    final Url = "https://pixabay.com/api/?key=$key&q=$value";

    final res = await http.get(Url);
    print(res.statusCode);
    if (res.statusCode == 200) {
      print(res.body.toString());
      var body = jsonDecode(res.body);
      List<TileModel> tilelist = [];
      for (var u in body["hits"]) {
        TileModel tileModel = TileModel(u["previewURL"], u["pageURL"],
            u["user"], u["userImageURL"], u["webformatURL"]);
        tilelist.add(tileModel);
      }

      return tilelist;
    } else {
      throw "Can't get posts..";
    }
  }
}
