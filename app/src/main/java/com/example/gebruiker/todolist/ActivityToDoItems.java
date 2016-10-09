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
import java.io.FileOutputStream;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_items);
        Intent activityThatCalled = getIntent();
        selectedList = activityThatCalled.getExtras().getString("List item");

        if (read()!=null){
            items = read().split(",");}

        ListAdapter theAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, items) {
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

        final ListView theListView = (ListView)findViewById(R.id.theListItemView);
        theListView.setAdapter(theAdapter);

        TextView textView = (TextView)findViewById(R.id.title2);
        textView.setText(selectedList);


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

        EditText inputText = (EditText) findViewById((R.id.userInputItem));
        return   userInput = inputText.getText().toString();
    };

    public void AddItemButton(View v) {
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
            Toast.makeText(getBaseContext(), "File saved successfully!",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String read() {
        //reading text from file
        //http://stackoverflow.com/questions/5283444/convert-array-of-strings-into-a-string-in-java
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
}
