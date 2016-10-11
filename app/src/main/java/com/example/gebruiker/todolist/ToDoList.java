package com.example.gebruiker.todolist;

import java.util.ArrayList;

/**
 * ToDoList.java
 * TO DO LIST
 *
 * Created by Tom Verburg on 09-10-2016.
 *
 * This is an Object class for the individual to do lists.
 *
 *
 */
public class ToDoList {
    private String title;
    private ArrayList<String> itemlist;

    public ToDoList(String listTitle)
    {
        this.title = listTitle;
    }

    public ArrayList<String> mItemlist(ArrayList<String> mitemlist){
        itemlist = mitemlist;
        return itemlist;
    }

    public String getTitle(){
        return title;
    }

}
