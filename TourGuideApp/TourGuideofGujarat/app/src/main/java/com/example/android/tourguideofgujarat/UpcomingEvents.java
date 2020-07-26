package com.example.android.tourguideofgujarat;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class UpcomingEvents extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events);

        final ArrayList<Gujarat> words = new ArrayList<Gujarat>();
        words.add(new Gujarat(getString(R.string.UE1),getString(R.string.UE1_details),getString(R.string.UE1_location)));
        words.add(new Gujarat(getString(R.string.UE2),getString(R.string.UE2_details),getString(R.string.UE2_location)));
        words.add(new Gujarat(getString(R.string.UE3),getString(R.string.UE3_details),getString(R.string.UE3_location)));
        words.add(new Gujarat(getString(R.string.UE4),getString(R.string.UE4_details),getString(R.string.UE4_location)));
        words.add(new Gujarat(getString(R.string.UE5),getString(R.string.UE5_details),getString(R.string.UE5_location)));
        words.add(new Gujarat(getString(R.string.UE6),getString(R.string.UE6_details),getString(R.string.UE6_location)));
        words.add(new Gujarat(getString(R.string.UE7),getString(R.string.UE7_details),getString(R.string.UE7_location)));
        words.add(new Gujarat(getString(R.string.UE8),getString(R.string.UE8_details),getString(R.string.UE8_location)));

        GujaratAdapter adapter = new GujaratAdapter(this, words,R.color.events);
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
