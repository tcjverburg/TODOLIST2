package com.example.gebruiker.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static final int READ_BLOCK_SIZE = 100;
    private String userInput;
    private ListView theListView;
    private ListAdapter theAdapter;
    private EditText editText;

//source: http://www.journaldev.com/9383/android-internal-storage-example-tutorial
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText)findViewById(R.id.userInput);

        if (savedInstanceState != null){
            String input = (String)savedInstanceState.getString("user input");
            editText.setText(input);
        }


        viewAll();


        }
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String userInput = editText.getText().toString();

        outState.putString("user input", userInput);
    }

    public void viewAll() {
        String[] list;
        String story = read();
        list = story.split(",");
        if (read()!="") {
            theAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        }
        else{
            ArrayList<String>emptylist = new ArrayList<String>();
            theAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, emptylist);
        }
            theListView = (ListView) findViewById(R.id.theListView);
            theListView.setAdapter(theAdapter);
            clickDeleteList();
            clickStartActivity();
        }



    public void clickDeleteList(){
        theListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                String listSelected = String.valueOf(adapterView.getItemAtPosition(position));
                deleteList(listSelected);
                return true;
            }
        });
    }
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
    //gets user input from edittext
    // Read text from file
    public String read() {
        String list = "";
        //reading text from file
        //http://stackoverflow.com/questions/5283444/convert-array-of-strings-into-a-string-in-java
        try {
            FileInputStream fileIn = openFileInput("lists.txt");
            InputStreamReader InputRead = new InputStreamReader(fileIn);

            //change reading
            char[] inputBuffer = new char[READ_BLOCK_SIZE];

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

    public String getUserInput() {

        userInput = editText.getText().toString();
        editText.setText("");
        return userInput;
    }

    public void deleteList(String listSelected){
        //still need to delete shared preferences and text files
        try {
            String story = read();
            FileOutputStream fileout = openFileOutput("lists.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            story = story.replace(","+listSelected, "");
            story = story.replace(listSelected, "");
            outputWriter.write(story);
            outputWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(getBaseContext(), "To Do List deleted",
                Toast.LENGTH_SHORT).show();
        viewAll();



    }
    // write text to file
    public void WriteBtn(View v) {
        // add-write text into file
        userInput = getUserInput();
        try {
            String story = read();
            FileOutputStream fileout = openFileOutput("lists.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            if (story != "") {
                outputWriter.write(story + "," + userInput);
                outputWriter.close();
            } else {
                outputWriter.write(userInput);
                outputWriter.close();
            }
            outputWriter.close();
            Toast.makeText(getBaseContext(), "To Do List added",
                    Toast.LENGTH_SHORT).show();

            //display file saved message


        } catch (Exception e) {
            e.printStackTrace();
        }
        viewAll();
    }





}