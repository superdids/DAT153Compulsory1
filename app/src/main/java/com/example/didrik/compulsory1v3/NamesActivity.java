package com.example.didrik.compulsory1v3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class NamesActivity extends AppCompatActivity {

    public static final String NAME = "dat153.hib.no.oblig1v2.NAME";

    private ApplicationDatabase myDB;
    private ArrayList<Person> list;
    private ArrayAdapter<Person> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_names);

        myDB = new ApplicationDatabase(this, null, null, 1);
        list = myDB.fetchAll();

        ListView listView = (ListView) findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<>(this, R.layout.list_view_names,
                R.id.listViewNames, list);

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent intent = new Intent(NamesActivity.this, ShowPersonActivity.class);
                intent.putExtra(NAME, list.get(position).getName());
                startActivity(intent);
            }
        });
    }
}
