package com.example.didrik.compulsory1v3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ImagesActivity extends AppCompatActivity {


    private ImageAdapter imageAdapter;
    private ApplicationDatabase myDB;
    private ArrayList<Person> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        myDB = new ApplicationDatabase(this, null, null, 1);
        list = myDB.fetchAll();

        ListView listView = (ListView) findViewById(R.id.imagesActivityListView);
        imageAdapter = new ImageAdapter(getApplicationContext(), R.layout.list_view_images, list);


        listView.setAdapter(imageAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(ImagesActivity.this, ShowPersonActivity.class);
                intent.putExtra(NamesActivity.NAME, list.get(position).getName());
                startActivity(intent);
            }
        });

    }
}
