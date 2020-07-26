package com.example.android.tourguideofgujarat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView topSights = (TextView) findViewById(R.id.top_sights);
        topSights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent topSights = new Intent(MainActivity.this,TopSights.class);
                startActivity(topSights);
            }
        });

        TextView famousRestaurants = (TextView) findViewById(R.id.famous_restaurants);
        famousRestaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent famousRestaurants = new Intent(MainActivity.this,FamousRestaurants.class);
                startActivity(famousRestaurants);
            }
        });

        TextView historicalMonuments = (TextView) findViewById(R.id.historical_monuments);
        historicalMonuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent historicalMonuments = new Intent(MainActivity.this,HistoricalMonuments.class);
                startActivity(historicalMonuments);
            }
        });

        TextView upcomingEvents = (TextView) findViewById(R.id.upcoming_events);
        upcomingEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent upcomingEvents = new Intent(MainActivity.this,UpcomingEvents.class);
                startActivity(upcomingEvents);
            }
        });
    }
}
