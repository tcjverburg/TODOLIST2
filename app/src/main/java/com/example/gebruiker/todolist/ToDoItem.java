package com.example.gebruiker.todolist;

/**
 * ToDoItem.java
 * TO DO LIST
 *
 * Created by Tom Verburg on 09-10-2016.
 *
 * This is an Object class for the to do items for the lists.
 *
 *
 */
public class ToDoItem {
    private String title;
    private boolean completed =Boolean.FALSE;

    public ToDoItem(String title){
        this.title = title;
    }

    public void toDoItemCompleted(Boolean listcompleted){
        completed = listcompleted;
    }

    public String title(){
        return this.title ;
    }

    public Boolean getState(){
        return completed ;
    }

}
