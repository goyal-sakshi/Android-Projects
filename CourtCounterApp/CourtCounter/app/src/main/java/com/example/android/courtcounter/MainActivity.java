package com.example.android.courtcounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int scoreTeamA = 0;
    int scoreTeamB = 0;
    int foulForTeamA = 0;
    int foulForTeamB = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    //  Team A
    public void addThreeForTeamA(View View) {
        scoreTeamA = scoreTeamA + 3;
        displayForTeamA(scoreTeamA);
    }
    public void addTwoForTeamA(View View) {
        scoreTeamA = scoreTeamA + 2;
        displayForTeamA(scoreTeamA);
    }
    public void freeThrowTeamA(View View) {
        scoreTeamA = scoreTeamA + 1;
        displayForTeamA(scoreTeamA);
    }
    //  foul for team A
    public void foulTeamA(View View) {
        foulForTeamA = foulForTeamA + 1;
        displayFoulForTeamA(foulForTeamA);
    }
    //  Team B
    public void addThreeForTeamB(View View) {
        scoreTeamB = scoreTeamB + 3;
        displayForTeamB(scoreTeamB);
    }
    public void addTwoForTeamB(View View) {
        scoreTeamB = scoreTeamB + 2;
        displayForTeamB(scoreTeamB);
    }
    public void freeThrowTeamB(View View) {
        scoreTeamB = scoreTeamB + 1;
        displayForTeamB(scoreTeamB);
    }
    //  foul for team B
    public void foulTeamB(View View) {
        foulForTeamB = foulForTeamB + 1;
        displayFoulForTeamB(foulForTeamB);
    }
    // reset
    public void reset(View View) {
        scoreTeamA = 0;
        scoreTeamB = 0;
        foulForTeamA = 0 ;
        foulForTeamB = 0 ;
        displayForTeamA(scoreTeamA);
        displayForTeamB(scoreTeamB);
        displayFoulForTeamA(foulForTeamA);
        displayFoulForTeamB(foulForTeamB);
    }
    /**
     * Displays the given score for Team A.
     */
    public void displayForTeamA(int score) {
        TextView scoreView = (TextView) findViewById(R.id.team_a_score);
        scoreView.setText(String.valueOf(score));
    }
    /**
     * Displays the given score for Team B.
     */
    public void displayForTeamB(int score) {
        TextView scoreView = (TextView) findViewById(R.id.team_b_score);
        scoreView.setText(String.valueOf(score));
    }
    /**
     * Displays the foul for Team A.
     */
    public void displayFoulForTeamA(int score) {
        TextView scoreView = (TextView) findViewById(R.id.team_a_foul);
        scoreView.setText(String.valueOf(score));
    }
    /**
     * Displays the foul for Team B.
     */
    public void displayFoulForTeamB(int score) {
        TextView scoreView = (TextView) findViewById(R.id.team_b_foul);
        scoreView.setText(String.valueOf(score));
    }
}
