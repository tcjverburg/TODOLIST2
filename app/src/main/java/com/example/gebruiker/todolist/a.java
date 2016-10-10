package com.example.gebruiker.todolist;

/**
 * Created by Gebruiker on 10-10-2016.
 */
public class a {
    private static a ourInstance = new a();

    public static a getInstance() {
        return ourInstance;
    }

    private a() {
    }
}
