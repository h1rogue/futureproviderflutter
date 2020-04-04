import 'package:flutter/material.dart';
import 'package:futureproviderflutter/PostService.dart';
import 'package:futureproviderflutter/Screen.dart';
import 'package:provider/provider.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  final PostService postService = PostService();

  @override
  Widget build(BuildContext context) {
    String q = postService.getq();
    return FutureProvider(
      create: (context)=>postService.fetchDatafromApi(q),
      child: MaterialApp(
        home: Scaff(),
      ),
    );
  }
}
