package com.example.gebruiker.todolist;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * ActivityToDoItems.java
 * TO DO LIST
 *
 * Created by Tom Verburg on 09-10-2016.
 *
 * This class is the other activity, besides the MainActivity.
 * Here you can see the individual items of a list that was selected
 * in the mainactivity. Items can be added, checked/unchecked and deleted.
 *
 *
 */
public class ActivityToDoItems extends Activity{

    private String selectedList;
    private ListAdapter theAdapter;
    private ListView theListView;
    private EditText editText;
    private ArrayList<String> itemlist = new ArrayList<String>();
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private TextView textView;


    //source:http://www.journaldev.com/9383/android-internal-storage-example-tutorial
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_items);


        Intent activityThatCalled = getIntent();
        selectedList = activityThatCalled.getExtras().getString("List item");
        theListView = (ListView) findViewById(R.id.theListItemView);
        textView = (TextView)findViewById(R.id.title2);
        textView.setText("List: " + selectedList);

        ToDoList List = new ToDoList(selectedList);
        itemlist = List.mItemlist(new ArrayList<String>(Arrays.asList(read().split(","))));
        editText = (EditText)findViewById(R.id.userInputItem);



        pref = getApplicationContext().getSharedPreferences(selectedList, 0);

        if (itemlist == null || itemlist.get(0).isEmpty()) {
            itemlist = new ArrayList<>();
        }

        adapter();
        colorTextChange();
        clickDeleteItem();
    }
//Sets adapter for the listview of items.
    public void adapter(){
        theAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, itemlist) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                String itemSelected = (String) text.getText();
                text.setTextColor(Color.DKGRAY);

                if (reader(pref).get(itemSelected)==Boolean.TRUE){
                    text.setTextColor(Color.LTGRAY);}
                return view;
            }};
        theListView.setAdapter(theAdapter);
    }
//Changes color of text in the selected textview.
    public void colorTextChange(){
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                TextView tv = (TextView)view.findViewById(android.R.id.text1);
                editor = pref.edit();


                String itemSelected = String.valueOf(adapterView.getItemAtPosition(position));
                ToDoItem item = new ToDoItem(itemSelected);
                String titleItem = item.title();


                if(tv.getCurrentTextColor()==Color.LTGRAY){
                    tv.setTextColor(Color.DKGRAY);
                }

                else{
                    item.toDoItemCompleted(Boolean.TRUE);
                    tv.setTextColor(Color.LTGRAY);

                }
                editor.putBoolean(titleItem, item.getState());
                editor.commit();
            }
        });
    }
//Returns a Map<String, ?> with items and the checked/unchecked boolean value.
    public Map<String, ?> reader(SharedPreferences pref){
        Map<String, ?> allEntries = pref.getAll();
        return allEntries;
    }
//The button for adding new items.
    public void AddItemButton(View v){
        String listItem = editText.getText().toString();
        if (listItem!="") {
            itemlist.add(listItem);
        }
        adapter();
        editText.setText("");
    }
//Saves the list of items of the selected list.
    protected void save() {
        try {
            String story = read();
            String savingList = "";
            for(String list : itemlist){
                if (savingList =="") {
                    savingList += list;}
                else{
                    savingList += ","+list;}
            }
            FileOutputStream fileout = openFileOutput("items.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            outputWriter.write(savingList);
            outputWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//Reads the textfile and gets the items.
    public String read() {
        String list = "";
        //reading text from file
        try {
            FileInputStream fileIn = openFileInput("items.txt");
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
//Onclick method for deleting the specific item.
    public void clickDeleteItem() {
        theListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                String itemSelected = String.valueOf(adapterView.getItemAtPosition(position));
                itemlist.remove(itemSelected);
                adapter();
                return true;
            }
        });
    }
//Saves list of items onDestroy().
    @Override
    protected void onDestroy() {
        super.onDestroy();
        save();
    }
}


