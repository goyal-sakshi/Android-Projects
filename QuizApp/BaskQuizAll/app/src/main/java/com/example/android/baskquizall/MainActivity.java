package com.example.android.baskquizall;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

//This method is called when submit button is clicked

    public void onSubmit(View View) {

        count = 0;

        //getting answer of first question
        RadioButton ans_1 = (RadioButton) findViewById(R.id.correct_ans_1);
        boolean A1 = ans_1.isChecked();
        forAns_1(A1);

        //getting answer of second question
        CheckBox ans_2a = (CheckBox) findViewById(R.id.correct_ans_2a);
        CheckBox ans_2b = (CheckBox) findViewById(R.id.correct_ans_2b);
        CheckBox ans_2aa = (CheckBox) findViewById(R.id.incorrect_1);
        CheckBox ans_2bb = (CheckBox) findViewById(R.id.incorrect_2);
        boolean A2a = ans_2a.isChecked();
        boolean A2b = ans_2b.isChecked();
        boolean A2aa = ans_2aa.isChecked();
        boolean A2bb = ans_2bb.isChecked();
        forAns_2(A2a, A2b, A2aa, A2bb);

        //getting answer of third question
        RadioButton ans_3 = (RadioButton) findViewById(R.id.correct_ans_3);
        boolean A3 = ans_3.isChecked();
        forAns_3(A3);

        //getting answer of 4th question
        EditText ans_4 = (EditText) findViewById(R.id.correct_ans_4);
        boolean Ans4 = "foul".equals(ans_4.getText().toString());
        forAns_4(Ans4);

        //getting answer of 5th question
        RadioButton ans_5 = (RadioButton) findViewById(R.id.correct_ans_5);
        boolean A5 = ans_5.isChecked();
        forAns_5(A5);

        //displaying toast message
        Toast.makeText(this, "Your Score: " + count + "/5", Toast.LENGTH_SHORT).show();
    }

    /**checking whether first answer is correct
     *
     * @param isCorrect holds the answer which the user has selected
     */
    public void forAns_1(boolean isCorrect) {
        if (isCorrect) {
            count += 1;
        }
    }

    /**checking whether second answer is correct
     *
     * @param isCorrect1 holds the answer which the user has selected
     * @param isCorrect2 holds the answer which the user has selected
     * @param notCorrect1 holds the answer which the user has selected
     * @param notCorrect2 holds the answer which the user has selected
     */
    public void forAns_2 (boolean isCorrect1, boolean isCorrect2, boolean notCorrect1, boolean notCorrect2) {
        if (notCorrect1) {
            count += 0;
        }
        else if (notCorrect2) {
            count += 0;
        }
        else if (isCorrect1 && isCorrect2) {
            count += 1;
        }
    }

    /**checking whether 3rd answer is correct
     *
     * @param isCorrect holds the answer which the user has selected
     */
    public void forAns_3(boolean isCorrect) {
        if (isCorrect) {
            count += 1;
        }
    }

    /**checking whether 4th answer is correct
     *
     * @param isCorrect holds the answer which the user has selected
     */
    public void forAns_4 (boolean isCorrect) {
        if (isCorrect) {
            count += 1;
        }

    }

    /**checking whether 6th answer is correct
     *
     * @param isCorrect holds the answer which the user has selected
     */
    public void forAns_5(boolean isCorrect) {
        if (isCorrect) {
            count += 1;
        }

    }
}
