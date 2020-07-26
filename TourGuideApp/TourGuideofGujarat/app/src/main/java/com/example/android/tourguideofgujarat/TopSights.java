package com.example.android.tourguideofgujarat;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class TopSights extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_sights);

        final ArrayList<Gujarat> words = new ArrayList<Gujarat>();
        words.add(new Gujarat(getString(R.string.TS1),getString(R.string.TS1_details),R.drawable.aravalli_range,getString(R.string.TS1_location)));
        words.add(new Gujarat(getString(R.string.TS2),getString(R.string.TS2_details),R.drawable.somnath_temple,getString(R.string.TS2_location)));
        words.add(new Gujarat(getString(R.string.TS3),getString(R.string.TS3_details),R.drawable.sabarmati,getString(R.string.TS3_location)));
        words.add(new Gujarat(getString(R.string.TS4),getString(R.string.TS4_details),R.drawable.girnar,getString(R.string.TS4_location)));
        words.add(new Gujarat(getString(R.string.TS5),getString(R.string.TS5_details),R.drawable.kankaria_lake,getString(R.string.TS5_location)));
        words.add(new Gujarat(getString(R.string.TS6),getString(R.string.TS6_details),R.drawable.luxmi_vilas,getString(R.string.TS6_location)));
        words.add(new Gujarat(getString(R.string.TS7),getString(R.string.TS7_details),R.drawable.akshardham,getString(R.string.TS7_location)));

        GujaratAdapter adapter = new GujaratAdapter(this, words,R.color.top_sights);
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
