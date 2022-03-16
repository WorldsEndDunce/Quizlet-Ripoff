package com.example.quizletripoff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        ImageView cancel = ((ImageView) findViewById(R.id.cancel_button));
        ImageView save = ((ImageView) findViewById(R.id.save_button));
        EditText newQuestion = ((EditText) findViewById(R.id.new_question));
        EditText newAnswer = ((EditText) findViewById(R.id.new_answer));
        EditText newOption1 = ((EditText) findViewById(R.id.new_option1));
        EditText newOption2 = ((EditText) findViewById(R.id.new_option2));
        
        String curQuestion = getIntent().getStringExtra("curQuestion");
        String curAnswer = getIntent().getStringExtra("curAnswer");
        String curOption1 = getIntent().getStringExtra("curOption1");
        String curOption2 = getIntent().getStringExtra("curOption2");

        newQuestion.setText(curQuestion);
        newAnswer.setText(curAnswer);
        newOption1.setText(curOption1);
        newOption2.setText(curOption2);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick (View v) {
                 finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if (newQuestion.getText().toString().equals("") || newAnswer.getText().toString().equals("")) Toast.makeText(getApplicationContext(), "Please input both a question and answer", Toast.LENGTH_SHORT).show();
                else {
                    Intent data = new Intent();
                    data.putExtra("newQuestion", newQuestion.getText().toString());
                    data.putExtra("newAnswer", newAnswer.getText().toString());
                    data.putExtra("newOption1", newOption1.getText().toString());
                    data.putExtra("newOption2", newOption2.getText().toString());

                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });

    }
}