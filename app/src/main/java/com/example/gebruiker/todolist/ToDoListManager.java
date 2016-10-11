package com.example.gebruiker.todolist;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * ToDoList.java
 * TO DO LIST
 *
 * Created by Tom Verburg on 09-10-2016.
 *
 * This is an Object class with a singleton to manage the ToDoLists.
 *
 *
 */
public class ToDoListManager  {
    private static ToDoListManager mInstance = null;

    private ArrayList<String> listlist;

    private ToDoListManager(){
        listlist = new ArrayList<String>();
    }

    public static ToDoListManager getInstance(){
        if(mInstance == null)
        {
            mInstance = new ToDoListManager();
        }
        return mInstance;
    }

    public ArrayList<String> setArrayString(String story){
        listlist = new ArrayList<String>(Arrays.asList(story.split(",")));
        return listlist;
    }


}
