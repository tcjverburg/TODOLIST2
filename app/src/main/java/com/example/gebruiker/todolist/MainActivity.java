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

public class MainActivity extends AppCompatActivity {
    static final int READ_BLOCK_SIZE = 100;
    private String userInput;
    private ListView theListView;
    private ListAdapter theAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewAll();


        }

    public void viewAll() {
        String[] list;
        String story = read();
        list = story.split(",");

            theAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
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

        EditText inputText = (EditText) findViewById((R.id.userInput));
        userInput = inputText.getText().toString();
        inputText.setText("");
        return userInput;
    }
    public void deleteList(String listSelected){
        try {
            String story = read();
            FileOutputStream fileout = openFileOutput("lists.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            story = story.replace(","+listSelected, "");
            story = story.replace(listSelected, "");
            outputWriter.write(story);
            outputWriter.close();
            Toast.makeText(getBaseContext(), story,
                    Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



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
            outputWriter.close();Toast.makeText(getBaseContext(), read(),
                    Toast.LENGTH_SHORT).show();

            //display file saved message


        } catch (Exception e) {
            e.printStackTrace();
        }

    }





}