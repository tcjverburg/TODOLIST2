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
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by Gebruiker on 9-10-2016.
 */
public class ActivityToDoItems extends Activity{
    private String[] items = {};
    private String userInput;
    private String selectedList;
    private ListAdapter theAdapter;
    private ListView theListView;
    private EditText editText;

//source:http://www.journaldev.com/9383/android-internal-storage-example-tutorial
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_items);
        Intent activityThatCalled = getIntent();
        selectedList = activityThatCalled.getExtras().getString("List item");
        editText = (EditText)findViewById(R.id.userInputItem);
        if (savedInstanceState != null){
            String input = (String)savedInstanceState.getString("user input");
            editText.setText(input);
        }
        showAll();
    }

    public void showAll(){
        if (read()!=null){
            items = read().split(",");}
        adapter();

        theListView = (ListView)findViewById(R.id.theListItemView);
        theListView.setAdapter(theAdapter);

        TextView textView = (TextView)findViewById(R.id.title2);
        textView.setText(selectedList);

        colorTextChange();
        clickDeleteList();
    }

    public void adapter(){
        items = read().split(",");
        theAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, items) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                SharedPreferences pref = getApplicationContext().getSharedPreferences(selectedList, 0);
                String itemSelected = (String) text.getText();

                text.setTextColor(Color.DKGRAY);

                if (Arrays.asList(reader(pref)).contains(itemSelected)){
                    text.setTextColor(Color.LTGRAY);}

                return view;
            }};

    }

    public void colorTextChange(){
        theListView = (ListView)findViewById(R.id.theListItemView);
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                TextView tv = (TextView)view.findViewById(android.R.id.text1);
                SharedPreferences pref = getApplicationContext().getSharedPreferences(selectedList, 0);
                SharedPreferences.Editor editor = pref.edit();
                //getstringview
                String itemSelected = String.valueOf(adapterView.getItemAtPosition(position));

                if(tv.getCurrentTextColor()==Color.LTGRAY){
                    tv.setTextColor(Color.DKGRAY);
                    editor.remove(itemSelected);
                }

                else{
                    editor.putString(itemSelected, itemSelected);
                    tv.setTextColor(Color.LTGRAY);

                }
                editor.commit();
            }
        });
    }

    public String[] reader(SharedPreferences pref){
        ArrayList<String> list = new ArrayList<String>();
        Map<String, ?> allEntries = pref.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            list.add(entry.getValue().toString());}
        String[] arr = list.toArray(new String[list.size()]);

        return arr;
    }

    public String getUserInput(){
        String userInput = editText.getText().toString();
        editText.setText("");
        return   userInput;
    };

    public void AddItemButton(View v)
    {
        // add-write text into file
        userInput = getUserInput();
        try {
            String story = read();
            FileOutputStream fileout = openFileOutput(selectedList + "unchecked.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            if (story != "") {
                outputWriter.write(story + "," + userInput);
            } else {
                outputWriter.write(userInput);
            }
            outputWriter.close();

            //display file saved message
            Toast.makeText(getBaseContext(), "To Do Item added!",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
        showAll();
    }

    public String read() {
        //reading text from file
        String list = "";
        try {
            FileInputStream fileIn = openFileInput(selectedList+"unchecked.txt");
            InputStreamReader InputRead = new InputStreamReader(fileIn);

            //change reading
            char[] inputBuffer = new char[100];
            list = "";
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

    public void deleteItem(String itemSelected){
        //still need to delete shared preferences and text files
        try {
            String story = read();
            FileOutputStream fileout = openFileOutput(selectedList + "unchecked.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            story = story.replace(","+itemSelected, "");
            story = story.replace(itemSelected, "");
            outputWriter.write(story);
            outputWriter.close();
            Toast.makeText(getBaseContext(), "To Do Item deleted",
                    Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        showAll();
    }

    public void clickDeleteList(){
        theListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                String itemSelected = String.valueOf(adapterView.getItemAtPosition(position));
                deleteItem(itemSelected);
                return true;
            }
        });
    }
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String userInput = editText.getText().toString();
        outState.putSerializable("user input", userInput);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}

