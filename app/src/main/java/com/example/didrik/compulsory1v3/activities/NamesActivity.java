package com.example.didrik.compulsory1v3.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.didrik.compulsory1v3.persistence.ApplicationDatabase;
import com.example.didrik.compulsory1v3.persistence.Person;
import com.example.didrik.compulsory1v3.R;
import com.example.didrik.compulsory1v3.adapters.NameAdapter;

import java.util.ArrayList;

/**
 * Views clickable items in a ListView. Each item contains a name which is
 * associated to an image (the image is not shown beside the name).
 * @author Didrik Emil Aubert
 * @author Ståle André Mikalsen
 * @author Viljar Buen Rolfsen
 */
public class NamesActivity extends AppCompatActivity {

    /**
     * Database operations
     */
    private ApplicationDatabase myDB;

    /**
     * The list which shall be associated to the adapter
     */
    private ArrayList<Person> list;

    /**
     * Custom adapter extending ArrayAdapter
     */
    private NameAdapter nameAdapter;

    /**
     * Renders an AdapterView after associating the adapter to the list.
     * @param savedInstanceState Information of this activity's previously frozen state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_names);

        myDB = new ApplicationDatabase(this, null, null, 1);
        list = myDB.fetchAll();

        ListView listView = (ListView) findViewById(R.id.listView);
        nameAdapter = new NameAdapter(this, R.layout.list_view_names, list, myDB);

        listView.setAdapter(nameAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent intent = new Intent(NamesActivity.this, ShowPersonActivity.class);
                intent.putExtra(ShowPersonActivity.NAME, list.get(position).getName());
                startActivity(intent);
            }
        });
    }
}
