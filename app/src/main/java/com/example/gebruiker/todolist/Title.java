package com.example.gebruiker.todolist;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Title.java
 * TO DO LIST
 *
 * Created by Tom Verburg on 09-10-2016.
 *
 * This is the fragment class, used to create the Header in MainActivity.java.
 *
 *
 */
public class Title extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.title, container, false);
    }
}
