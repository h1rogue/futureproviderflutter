import 'package:flutter/material.dart';
import 'package:futureproviderflutter/ListModel.dart';
import 'package:futureproviderflutter/PostService.dart';
import 'package:provider/provider.dart';

class Scaff extends StatelessWidget {
  PostService postService = PostService();

  @override
  Widget build(BuildContext context) {
    List<TileModel> posts = Provider.of<List<TileModel>>(context);
    return Scaffold(
        appBar: AppBar(
          title: Text('Future Api'),
          actions: <Widget>[
            IconButton(
              icon: Icon(Icons.search),
              onPressed: () async {
                var res = await showSearch(context: context, delegate: SearchDel());
                    postService.setq(res);
              },
            )
          ],
        ),
        body: (posts == null)
            ? Center(
                child: CircularProgressIndicator(),
              )
            : ListView.builder(
                itemCount: posts.length,
                itemBuilder: (context, index) {
                  return GestureDetector(
                    onTap: () {
                      //TODO
                    },
                    child: Container(
                      child: Card(
                          elevation: 4,
                          child: Image.network(posts[index].webformatURL)),
                    ),
                  );
                }));
  }
}

class SearchDel extends SearchDelegate<String> {
  final SearchList = ["cats", "dogs", "girl","child","moon", "love", "horse","aeroplane","tiger","panda"];

  @override
  List<Widget> buildActions(BuildContext context) {
    return [
      IconButton(
        icon: Icon(Icons.clear),
        onPressed: () {
          query = "";
        },
      )
    ];
  }

  @override
  Widget buildLeading(BuildContext context) {
    return IconButton(
      icon: Icon(Icons.arrow_back),
      onPressed: () {
        close(context, null);
      },
    );
  }

  @override
  Widget buildResults(BuildContext context) {
    close(context, query);
    return Container();
  }

  @override
  Widget buildSuggestions(BuildContext context) {
    return ListView.builder(
        itemCount: SearchList.length,
        itemBuilder: (context, index) {
          return ListTile(
            onTap: () {
              close(context, SearchList[index]);
            },
            title: Text(SearchList[index]),
          );
        });
  }
}
