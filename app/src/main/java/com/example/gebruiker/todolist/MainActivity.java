package com.example.gebruiker.todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
/**
 * MainActivity.java
 * TO DO LIST
 *
 * Created by Tom Verburg on 3-10-2016.
 *
 * This class is the starting main screen of the application. Here you
 * can see the to do lists and add/delete them or navigate to the activity
 * ActivityToDoItems.java to see the specific items of the selected list.
 *
 *
 */
public class MainActivity extends Activity {

    private EditText editText;
    private ArrayList<String> allLists;
    private ListAdapter theAdapter;
    private ListView theListView;

//source: http://www.journaldev.com/9383/android-internal-storage-example-tutorial

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.userInput);


        allLists = new ArrayList<>(ToDoListManager.getInstance().setArrayString(read()));

        if (allLists == null || allLists.get(0).isEmpty()) {
            allLists = new ArrayList<>();
        }
        viewAll();
        editText.setText("");
    }

//Loads in the list into the adapter to show the listview
    public void viewAll(){
        theAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allLists);
        theListView = (ListView) findViewById(R.id.theListView);
        theListView.setAdapter(theAdapter);
        clickDeleteList();
        clickStartActivity();
    }

//Button method which adds new list
    public void Btn(View v) {
        String listTitle = editText.getText().toString();
        ToDoList newlist = new ToDoList(listTitle);
        if (listTitle!="") {
            allLists.add(listTitle);
        }
        viewAll();
        editText.setText("");
    }

//Clickmethod for deleting a list
    public void clickDeleteList() {
        theListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                String listSelected = String.valueOf(adapterView.getItemAtPosition(position));
                allLists.remove(listSelected);
                viewAll();
                return true;
            }
        });
    }

//Clickmethod for starting new activity after selecting a list
    public void clickStartActivity(){
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String listPicked = String.valueOf(adapterView.getItemAtPosition(position));
                Intent getNameScreen = new Intent(getApplicationContext(), ActivityToDoItems.class);
                getNameScreen.putExtra("List item", listPicked);
                startActivity(getNameScreen);
            }
        });
    }

//Saving the list of lists in a textfile.
    protected void save() {
        try {
            String story = read();
            String savingList = "";
            for(String list : allLists){
                if (savingList =="") {
                    savingList += list;}
                else{
                savingList += ","+list;}
            }
            FileOutputStream fileout = openFileOutput("listnieuw.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
                outputWriter.write(savingList);
                outputWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//Reads the lists out of textfile.
    public String read() {
        String list = "";

        try {
            FileInputStream fileIn = openFileInput("listnieuw.txt");
            InputStreamReader InputRead = new InputStreamReader(fileIn);
            char[] inputBuffer = new char[100];
            int charRead;
            while ((charRead = InputRead.read(inputBuffer)) > 0) {
                // char to string conversion
                String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                list += readstring;
            }
            InputRead.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

//saves the list of lists onDestroy()
    @Override
    protected void onDestroy() {
        super.onDestroy();
        save();
    }

}





