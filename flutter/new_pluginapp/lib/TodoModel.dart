import 'package:flutter/cupertino.dart';
import 'package:newpluginapp/TaskModel.dart';

class TodoModel extends ChangeNotifier{
  List<TaskModel> taskList = []; //contians all the task

  addTaskInList(TaskModel t){
    taskList.add(t);
    notifyListeners();
    //code to do
  }

}