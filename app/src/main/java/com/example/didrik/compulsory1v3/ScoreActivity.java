package com.example.didrik.compulsory1v3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    public static final String CORRECT = "dat153.hib.no.oblig1v2.CORRECT";
    public static final String TOTAL = "dat153.hib.no.oblig1.v2.TOTAL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Bundle data = getIntent().getExtras();
        int correct = data.getInt(CORRECT);
        int total = data.getInt(TOTAL);

        TextView headingView = (TextView) findViewById(R.id.scoreResultHeading);
        TextView scoreView = (TextView) findViewById(R.id.scoreResultText);

        String resultHeadingText = "Results";
        if(correct == total)
            resultHeadingText = "Congratulations!";
        String resultScoreText = "You scored " + correct + " out of " + total;

        headingView.setText(resultHeadingText);
        scoreView.setText(resultScoreText);
    }

    public void onClick(View view) {
        Intent resumeMain = new Intent(ScoreActivity.this, MainActivity.class);
        resumeMain.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(resumeMain);
    }
}
