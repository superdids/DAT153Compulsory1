package com.example.didrik.compulsory1v3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Views clickable items in a ListView. Each item contains an image which is
 * associated to a name (the name is not shown beside the image).
 * @author Didrik Emil Aubert
 * @author Ståle André Mikalsen
 * @author Viljar Buen Rolfsen
 */
public class ImagesActivity extends AppCompatActivity {


    /**
     * Custom adapter extending ArrayAdapter.
     */
    private ImageAdapter imageAdapter;

    /**
     * Database operations
     */
    private ApplicationDatabase myDB;

    /**
     * The list which shall be associated to the adapter.
     */
    private ArrayList<Person> list;


    /**
     * Renders an AdapterView after associating the adapter to the list.
     * @param savedInstanceState Information of this activity's previously frozen state.
     */
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
                intent.putExtra(ShowPersonActivity.NAME, list.get(position).getName());
                startActivity(intent);
            }
        });

    }
}
