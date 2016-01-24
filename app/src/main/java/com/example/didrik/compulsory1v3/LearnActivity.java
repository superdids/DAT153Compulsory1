package com.example.didrik.compulsory1v3;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LearnActivity extends AppCompatActivity {

    //private ArrayList<Person> personList;
    private ApplicationDatabase myDB;
    private ArrayList<Person> list;
    private int total;
    private int current;
    private int correct = 0;
    private TextView input;
    private TextView score;
    private TextView imagesLeft;
    private ImageView image;
    private Person person;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        //personList = MainActivity.PERSON_LIST;
        myDB = new ApplicationDatabase(this, null, null, 1);
        list = myDB.fetchAll();
        //total = personList.size();
        total = list.size();
        if(total <= 0) {
            //TODO return to MainActivity or print a msg
            Toast.makeText(getApplicationContext(), "DONE", Toast.LENGTH_SHORT).show();
            return;
        } else {
            //shuffle(personList);
            shuffle(list);
            //total = total / 2;
            current = 0;
        }
        imagesLeft = (TextView) findViewById(R.id.imagesLeft);
        imagesLeft.setText(total + "");

        score = (TextView) findViewById(R.id.learnScore);
        score.setText(correct + "");

        //person = personList.get(current);
        person = list.get(current);

        image = (ImageView) findViewById(R.id.imageView);
        // InputStream stream = fetchImage(getApplicationContext(), person.getImageURL());
        Uri uri = Uri.parse(person.getUriString());
        InputStream stream = fetchImage(getApplicationContext(), uri);
        if(stream != null)
            image.setImageBitmap(BitmapFactory.decodeStream(stream));
        input = (EditText) findViewById(R.id.learnEditText);
    }

    public void onClick(View view) {

        person = list.get(current);
        //Person person = personList.get(current);
        ++current;

        if(person.getName().equalsIgnoreCase(input.getText().toString())) {
            correct++;
        }
        if(current >= total) {
            //TODO start "ResultActivity". This is a temp solution.
            Intent intent = new Intent(LearnActivity.this, ScoreActivity.class);
            intent.putExtra(ScoreActivity.CORRECT, correct);
            intent.putExtra(ScoreActivity.TOTAL, total);
            startActivity(intent);
            return;
        }
        score.setText(correct + "");
        imagesLeft.setText((total - current) + "");
        input.setText("");

        //person = personList.get(current);
        person = list.get(current);

        Uri uri = Uri.parse(person.getUriString());
        InputStream stream = fetchImage(getApplicationContext(), uri);
        // InputStream stream = fetchImage(getApplicationContext(), person.getImageURL());
        if(stream != null)
            image.setImageBitmap(BitmapFactory.decodeStream(stream));



    }

    private static InputStream fetchImage(Context context, Uri uri) {
        InputStream stream = null;
        try {
            stream = context.getContentResolver().openInputStream(uri);
        } catch(Exception e) {
            throw new Error(e);
        }
        return stream;
    }

    private static void shuffle(List<Person> list) {
        int n = list.size();
        Random random = new Random();
        random.nextInt();
        for(int i = 0; i < n; ++i) {
            int change = i + random.nextInt(n-i);
            swap(list, i, change);
        }
    }
    private static void swap(List<Person> list, int i, int change) {
        Person tmp = list.get(i);
        list.set(i, list.get(change));
        list.set(change, tmp);
    }
}
