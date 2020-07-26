package com.example.android.tourguideofgujarat;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class FamousRestaurants extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_famous_restaurants);

        final ArrayList<Gujarat> words = new ArrayList<Gujarat>();
        words.add(new Gujarat(getString(R.string.FR1),getString(R.string.FR1_details),getString(R.string.FR1_location)));
        words.add(new Gujarat(getString(R.string.FR2),getString(R.string.FR2_details),getString(R.string.FR2_location)));
        words.add(new Gujarat(getString(R.string.FR3),getString(R.string.FR3_details),getString(R.string.FR3_location)));
        words.add(new Gujarat(getString(R.string.FR4),getString(R.string.FR4_details),getString(R.string.FR4_location)));
        words.add(new Gujarat(getString(R.string.FR5),getString(R.string.FR5_details),getString(R.string.FR5_location)));
        words.add(new Gujarat(getString(R.string.FR6),getString(R.string.FR6_details),getString(R.string.FR6_location)));
        words.add(new Gujarat(getString(R.string.FR7),getString(R.string.FR7_details),getString(R.string.FR7_location)));
        words.add(new Gujarat(getString(R.string.FR8),getString(R.string.FR8_details),getString(R.string.FR8_location)));

        GujaratAdapter adapter = new GujaratAdapter(this, words,R.color.restaurants);
        ListView listview = (ListView) findViewById(R.id.list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Gujarat word = words.get(position);

                String url = word.getLocation();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }
}
