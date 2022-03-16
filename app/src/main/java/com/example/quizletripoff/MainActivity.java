package com.example.quizletripoff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView question = findViewById(R.id.flashcard_question);
        TextView answer = findViewById(R.id.flashcard_answer);
        TextView option1 = findViewById(R.id.flashcard_option1);
        TextView option2 = findViewById(R.id.flashcard_option2);
        ImageView eye = ((ImageView) findViewById(R.id.toggle_choices_visibility));
        ImageView add = ((ImageView) findViewById(R.id.add_card));
        ImageView edit = ((ImageView) findViewById(R.id.edit_card));

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
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                startActivityForResult(intent, 100);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                intent.putExtra("curQuestion", question.getText().toString());
                intent.putExtra("curAnswer", answer.getText().toString());
                intent.putExtra("curOption1", option1.getText().toString());
                intent.putExtra("curOption2", option2.getText().toString());
                MainActivity.this.startActivityForResult(intent, 100);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setContentView(R.layout.activity_main);

        TextView question = findViewById(R.id.flashcard_question);
        TextView answer = findViewById(R.id.flashcard_answer);
        TextView option1 = findViewById(R.id.flashcard_option1);
        TextView option2 = findViewById(R.id.flashcard_option2);
        ImageView eye = ((ImageView) findViewById(R.id.toggle_choices_visibility));
        ImageView add = ((ImageView) findViewById(R.id.add_card));
        ImageView edit = ((ImageView) findViewById(R.id.edit_card));

       if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            String newQuestion = data.getExtras().getString("newQuestion");
            String newAnswer = data.getExtras().getString("newAnswer");
            String newOption1 = data.getExtras().getString("newOption1");
            String newOption2 = data.getExtras().getString("newOption2");
//            String[] pos = {"@+id/flashcard_answer", "@+id/flashcard_option1", "@+id/flashcard_option2"};
//            List<String> positions = Arrays.asList(pos);
//            Collections.shuffle(positions);
            
            question.setText(newQuestion);
            answer.setText(newAnswer);
            option1.setText(newOption1);
            option2.setText(newOption2);
            Snackbar.make(findViewById(R.id.flashcard_question), "Card created.", Snackbar.LENGTH_SHORT).show();
       }

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
                    if (!option1.getText().toString().equals("")) option1.setVisibility(View.VISIBLE);
                    if (!option1.getText().toString().equals("")) option2.setVisibility(View.VISIBLE);
                }
                else {
                    eye.setImageResource(R.drawable.hiddeneye);
                    answer.setVisibility(View.INVISIBLE);
                    option1.setVisibility(View.INVISIBLE);
                    option2.setVisibility(View.INVISIBLE);
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                startActivityForResult(intent, 100);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                intent.putExtra("curQuestion", question.getText().toString());
                intent.putExtra("curAnswer", answer.getText().toString());
                intent.putExtra("curOption1", option1.getText().toString());
                intent.putExtra("curOption2", option2.getText().toString());
                MainActivity.this.startActivityForResult(intent, 100);
            }
        });
    }
}
