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
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    static final int READ_BLOCK_SIZE = 100;
    private String list;
    String[] lists = {};
    private String userInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (read()!=null){
        lists = read().split(",");}
        TextView txt = (TextView)findViewById(R.id.title);
        ListAdapter theAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, lists);
        ListView theListView = (ListView)findViewById(R.id.theListView);
        theListView.setAdapter(theAdapter);
        //The on click method that uses an intent to see more info about a particular movie
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
    public String getUserInput(){

        EditText inputText = (EditText) findViewById((R.id.userInput));
        return   userInput = inputText.getText().toString();
    };

    // write text to file
    public void WriteBtn(View v) {
        // add-write text into file
        userInput = getUserInput();
        try {
            String story = read();
            FileOutputStream fileout = openFileOutput("lists.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            if (story!=null){
                outputWriter.write(story + "," + userInput);}
            else{
                outputWriter.write(userInput);
            }
            outputWriter.close();

            //display file saved message
            Toast.makeText(getBaseContext(), "File saved successfully!",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Read text from file
    public String read() {
        //reading text from file
        //http://stackoverflow.com/questions/5283444/convert-array-of-strings-into-a-string-in-java
        try {
            FileInputStream fileIn = openFileInput("lists.txt");
            InputStreamReader InputRead = new InputStreamReader(fileIn);

            //change reading
            char[] inputBuffer = new char[READ_BLOCK_SIZE];
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
}
