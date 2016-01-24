package com.example.didrik.compulsory1v3;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class ShowPersonActivity extends AppCompatActivity {

    private ApplicationDatabase myDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_person);

        myDB = new ApplicationDatabase(this, null, null, 1);

        Bundle data = getIntent().getExtras();
        //int pos = data.getInt(NamesActivity.NAME);
        //Person person = MainActivity.PERSON_LIST.get(pos);
        String name = data.getString(NamesActivity.NAME);
        Person person = myDB.find(name);
        assert person != null;
        //String [] filePathColumn = { MediaStore.Images.Media.DATA };
        InputStream stream = null;
        try {
            Uri uri = Uri.parse(person.getUriString());
            stream = getContentResolver().openInputStream(uri);
        } catch (Exception e) {
            throw new Error(e);
        }

        ImageView imageView = (ImageView) findViewById(R.id.imageOfPerson);
        imageView.setImageBitmap(BitmapFactory.decodeStream(stream));

        TextView textView = (TextView) findViewById(R.id.nameOfPerson);
        textView.setText(person.getName());

    }

}
