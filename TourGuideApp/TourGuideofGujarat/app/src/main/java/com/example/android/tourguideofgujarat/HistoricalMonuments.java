package com.example.android.tourguideofgujarat;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoricalMonuments extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical_monuments);

        final ArrayList<Gujarat> words = new ArrayList<Gujarat>();
        words.add(new Gujarat(getString(R.string.HM1),getString(R.string.HM1_details),R.drawable.kevda_masjid,getString(R.string.HM1_location)));
        words.add(new Gujarat(getString(R.string.HM2),getString(R.string.HM2_details),R.drawable.rudra_mahalaya,getString(R.string.HM2_location)));
        words.add(new Gujarat(getString(R.string.HM3),getString(R.string.HM3_details),R.drawable.navlakha_kothar,getString(R.string.HM3_location)));
        words.add(new Gujarat(getString(R.string.HM4),getString(R.string.HM4_details),R.drawable.stepwell,getString(R.string.HM4_location)));
        words.add(new Gujarat(getString(R.string.HM5),getString(R.string.HM5_details),R.drawable.jhulta_minara,getString(R.string.HM5_location)));
        words.add(new Gujarat(getString(R.string.HM6),getString(R.string.HM6_details),R.drawable.kamini_masjid,getString(R.string.HM6_location)));
        words.add(new Gujarat(getString(R.string.HM7),getString(R.string.HM7_details),R.drawable.talaja_caves,getString(R.string.HM7_location)));
        words.add(new Gujarat(getString(R.string.HM8),getString(R.string.HM8_details),R.drawable.teen_darwaza,getString(R.string.HM8_location)));

        GujaratAdapter adapter = new GujaratAdapter(this, words,R.color.monuments);
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
