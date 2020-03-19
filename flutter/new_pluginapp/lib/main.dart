import 'package:flutter/material.dart';
import 'package:newpluginapp/TaskModel.dart';
import 'package:provider/provider.dart';
import 'package:newpluginapp/TodoModel.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        title: 'Flutter Demo',
        theme: ThemeData(
          primarySwatch: Colors.blue,
        ),
        home: ChangeNotifierProvider(
          builder: (context) => TodoModel(),
          child: MyHomePage(),
        )

    );
  }
}

class MyHomePage extends StatefulWidget {
  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  TextEditingController _controllertitle = TextEditingController();
  TextEditingController _controllerdesp = TextEditingController();
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      floatingActionButton: FloatingActionButton(
        child: Icon(Icons.add),
        onPressed: (){
          String tities = _controllertitle.text;
          String titdesp = _controllerdesp.text;
          _controllertitle.clear();
          _controllerdesp.clear();
          TaskModel t = TaskModel(tities,titdesp);
          Provider.of<TodoModel>(context).addTaskInList(t);
        },
      ),
      backgroundColor: Colors.lightBlue[900],
      appBar: AppBar(
        backgroundColor: Colors.transparent,
        elevation: 0,
        title: Text("Todo Application", style: TextStyle(color: Colors.white),),
        leading: IconButton(icon: Icon(Icons.menu, color: Colors.white70,),),
      ),
      body: Column(
        children: <Widget>[
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: <Widget>[
              SizedBox(height: 20,),
              Padding(
                padding: const EdgeInsets.all(8.0),
                child: TextField(
                  controller: _controllertitle,
                  decoration: InputDecoration(
                    enabledBorder: OutlineInputBorder(
                      borderSide: BorderSide(color: Colors.white),
                      borderRadius: BorderRadius.all(Radius.circular(4))
                    ),
                    focusedBorder:  OutlineInputBorder(
                        borderSide: BorderSide(color: Colors.white),
                        borderRadius: BorderRadius.all(Radius.circular(4))
                    ),
                  ),
                  style: TextStyle(color: Colors.white,fontSize: 20),
                ),
              ),

              Padding(
                padding: const EdgeInsets.all(8.0),
                child: TextField(
                  controller: _controllerdesp,
                  decoration: InputDecoration(
                    enabledBorder: OutlineInputBorder(
                        borderSide: BorderSide(color: Colors.white),
                        borderRadius: BorderRadius.all(Radius.circular(4))
                    ),
                    focusedBorder:  OutlineInputBorder(
                        borderSide: BorderSide(color: Colors.white),
                        borderRadius: BorderRadius.all(Radius.circular(4))
                    ),
                  ),
                  style: TextStyle(color: Colors.white,fontSize: 20),
                ),
              ),
              SizedBox(height: 20,)

            ],
          ), //to show the clock

          Expanded(
            child: Container(

                decoration: BoxDecoration(borderRadius: BorderRadius.only(topRight: Radius.circular(50), topLeft: Radius.circular(60)), color: Colors.white),
                child: Consumer<TodoModel>(
                  builder: (context, todo, child){
                    return ListView.builder(
                        itemCount: todo.taskList.length,
                        itemBuilder: (context, index){
                          return Container(
                            child: ListTile(
                              contentPadding: EdgeInsets.only(left: 32, right: 32, top: 8, bottom: 8),
                              title: Text(todo.taskList[index].title, style : TextStyle(color: Colors.black87,
                                  fontWeight: FontWeight.bold),),
                              subtitle: Text(todo.taskList[index].detail, style: TextStyle(color: Colors.black45,
                                  fontWeight: FontWeight.bold),),

                              trailing: Icon(Icons.check_circle, color: Colors.greenAccent,),
                            ),
                            margin: EdgeInsets.only(bottom: 8, left: 16, right: 16),
                          );
                        }
                    );
                  },
                )
            ),
          )

        ],
      ),
    );
  }
}