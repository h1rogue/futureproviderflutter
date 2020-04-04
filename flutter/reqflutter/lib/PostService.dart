import 'dart:convert';

import 'package:flutter/foundation.dart';
import 'package:reqflutter/PostModel.dart';
import 'package:http/http.dart' as http;

class PostService extends ChangeNotifier {
  int id=1;
  List<PostModel> _postlist = [];

  List<PostModel> get postlist => _postlist;

  set postlist(List<PostModel> value) {
    _postlist = value;
    notifyListeners();
  }

  Future<List<PostModel>> fetchApi(int id) async {
    final res = await http.get("https://jsonplaceholder.typicode.com/posts/?id=${id}");
    print(res.statusCode);
    if (res.statusCode == 200) {
      var body = jsonDecode(res.body);
      List<PostModel> posts = [];
      for (var u in body["hits"]) {
        PostModel tileModel = PostModel(u["userId"],u["id"],u["title"],u["body"]);
        posts.add(tileModel);
      }
      postlist = posts;
      return posts;
    } else {}
  }
}
