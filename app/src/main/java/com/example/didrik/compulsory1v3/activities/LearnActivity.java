package com.example.didrik.compulsory1v3.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.didrik.compulsory1v3.persistence.ApplicationDatabase;
import com.example.didrik.compulsory1v3.persistence.Person;
import com.example.didrik.compulsory1v3.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Learning mode, presents a sequence of images and asks the user
 * the name of the person on the current image.
 * @author Didrik Emil Aubert
 * @author Ståle André Mikalsen
 * @author Viljar Buen Rolfsen
 */
public class LearnActivity extends AppCompatActivity {
    //TODO add "Leave learning mode" button functionality!
    /**
     * Database operations.
     */
    private ApplicationDatabase myDB;

    /**
     * Persons in the database.
     */
    private ArrayList<Person> list;

    /**
     * Amount of persons
     */
    private int total;

    /**
     * The amount of answers the user has submitted.
     */
    private int current;

    /**
     * The amount of correct answers the user has submitted.
     */
    private int correct = 0;

    /**
     * The answer the user is submitting.
     */
    private TextView input;

    /**
     * View showing the user's score.
     */
    private TextView score;

    /**
     * View showing how many images there is left to show.
     */
    private TextView imagesLeft;

    /**
     * The current image to be shown.
     */
    private ImageView image;

    /**
     * The current person
     */
    private Person person;

    /**
     * Renders the view, shuffles the list of persons and shows the
     * first person in the shuffled list.
     * @param savedInstanceState Information of this activity's previously frozen state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);


        myDB = new ApplicationDatabase(this, null, null, 1);
        list = myDB.fetchAll();

        total = list.size();
        if(total <= 0) {
            Intent resumeMain = new Intent(LearnActivity.this, MainActivity.class);
            resumeMain.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(resumeMain);
            return;
        } else {

            shuffle(list);
            current = 0;
        }
        imagesLeft = (TextView) findViewById(R.id.imagesLeft);
        imagesLeft.setText(total + "");

        score = (TextView) findViewById(R.id.learnScore);
        score.setText(correct + "");

        person = list.get(current);

        image = (ImageView) findViewById(R.id.imageView);
        Uri uri = Uri.parse(person.getUriString());
        try {
            InputStream stream = fetchImage(getApplicationContext(), uri);
            image.setImageBitmap(BitmapFactory.decodeStream(stream));
        } catch(Exception e) { throw new Error(e); }
        input = (EditText) findViewById(R.id.learnEditText);
    }

    /**
     * Computes score and proceeds by either viewing the next image,
     * or invoking ScoreActivity to view results.
     * @param view The button which was clicked.
     */
    public void onClick(View view) {

        person = list.get(current);
        ++current;

        if(person.getName().equalsIgnoreCase(input.getText().toString())) {
            correct++;
        }
        if(current >= total) {
            Intent intent = new Intent(LearnActivity.this, ScoreActivity.class);
            intent.putExtra(ScoreActivity.CORRECT, correct);
            intent.putExtra(ScoreActivity.TOTAL, total);
            startActivity(intent);
            return;
        }
        score.setText(correct + "");
        imagesLeft.setText((total - current) + "");
        input.setText("");

        person = list.get(current);

        Uri uri = Uri.parse(person.getUriString());
        try {
            InputStream stream = fetchImage(getApplicationContext(), uri);
            image.setImageBitmap(BitmapFactory.decodeStream(stream));
        } catch(Exception e) { throw new Error(e); }
    }

    /**
     * Retrieves an image's stream by an URI object.
     * @param context The context of the activity.
     * @param uri The URI object.
     * @return The InputStream of the URI object.
     */
    private static InputStream fetchImage(Context context, Uri uri) throws IOException {
        return context.getContentResolver().openInputStream(uri);
    }

    /**
     * Shuffles a list
     * @param list The list to be shuffled.
     */
    private static void shuffle(List<Person> list) {
        int n = list.size();
        Random random = new Random();
        random.nextInt();
        for(int i = 0; i < n; ++i) {
            int change = i + random.nextInt(n-i);
            swap(list, i, change);
        }
    }

    /**
     * Swaps the placement of two elements in a list.
     * @param list The list to perform operations on.
     * @param a The index of the first placement.
     * @param b The index of the other placement.
     */
    private static void swap(List<Person> list, int a, int b) {
        Person tmp = list.get(a);
        list.set(a, list.get(b));
        list.set(b, tmp);
    }
}
