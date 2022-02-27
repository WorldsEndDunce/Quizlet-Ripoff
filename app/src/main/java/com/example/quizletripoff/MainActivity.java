package com.example.quizletripoff;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView answer = findViewById(R.id.flashcard_answer);
        TextView option1 = findViewById(R.id.flashcard_option1);
        TextView option2 = findViewById(R.id.flashcard_option2);
        ImageView eye = ((ImageView) findViewById(R.id.toggle_choices_visibility));


        findViewById(R.id.parent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                answer.setBackgroundColor(getResources().getColor(R.color.purple_200, null));
                option1.setBackgroundColor(getResources().getColor(R.color.purple_200, null));
                option2.setBackgroundColor(getResources().getColor(R.color.purple_200, null));
            }
        });
        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                answer.setBackgroundColor(getResources().getColor(R.color.green, null));
            }
        });
        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                option1.setBackgroundColor(getResources().getColor(R.color.red, null));
            }
        });
        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                option2.setBackgroundColor(getResources().getColor(R.color.red, null));
            }
        });
        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if (answer.getVisibility() == View.INVISIBLE) {
                    eye.setImageResource(R.drawable.ic_eye);
                    answer.setVisibility(View.VISIBLE);
                    option1.setVisibility(View.VISIBLE);
                    option2.setVisibility(View.VISIBLE);
                }
                else {
                    eye.setImageResource(R.drawable.hiddeneye);
                    answer.setVisibility(View.INVISIBLE);
                    option1.setVisibility(View.INVISIBLE);
                    option2.setVisibility(View.INVISIBLE);
                }
            }
        });

    }
}
