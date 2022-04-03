package com.example.quizletripoff;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;
    Flashcard cardToEdit;
    int curCard = 0;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView question = findViewById(R.id.flashcard_question);
        TextView answer = findViewById(R.id.flashcard_answer);
        TextView answer2 = findViewById(R.id.flashcard_answer2);
        TextView option1 = findViewById(R.id.flashcard_option1);
        TextView option2 = findViewById(R.id.flashcard_option2);
        TextView timer = (TextView) findViewById(R.id.timer);
        ImageView eye = ((ImageView) findViewById(R.id.toggle_choices_visibility));
        ImageView add = ((ImageView) findViewById(R.id.add_card));
        ImageView edit = ((ImageView) findViewById(R.id.edit_card));
        ImageView next = ((ImageView) findViewById(R.id.next_card));
        ImageView delete = ((ImageView) findViewById(R.id.delete_card));

        countDownTimer = new CountDownTimer(16000, 1000) {
            public void onTick(long millisUntilFinished) {
                timer.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
            }
        };

        flashcardDatabase = new FlashcardDatabase(getApplicationContext());
        allFlashcards = flashcardDatabase.getAllCards();
        if (allFlashcards.isEmpty()) {
            Flashcard og = new Flashcard("What is my favorite color?", "Orange", "Pink", "Blue");
            flashcardDatabase.insertCard(og);
        }
        question.setText(allFlashcards.get(0).getQuestion());
        answer.setText(allFlashcards.get(0).getAnswer());
        option1.setText(allFlashcards.get(0).getWrongAnswer1());
        option2.setText(allFlashcards.get(0).getWrongAnswer2());
        startTimer();
        findViewById(R.id.parent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                answer.setBackgroundColor(getResources().getColor(R.color.purple_200, null));
                option1.setBackgroundColor(getResources().getColor(R.color.purple_200, null));
                option2.setBackgroundColor(getResources().getColor(R.color.purple_200, null));
            }
        });
        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answer2.setText(answer.getText());
                question.setCameraDistance(100000);
                answer.setCameraDistance(100000);
                question.animate().rotationY(90).setDuration(200)
                        .withEndAction(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        question.setVisibility(View.INVISIBLE);
                                        answer2.setVisibility(View.VISIBLE);
                                        // second quarter turn
                                        answer2.setRotationY(-90);
                                        answer2.animate().rotationY(0).setDuration(200).start();
                                    }
                                }
                        ).start();
            }
        });
        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
//                new ParticleSystem(MainActivity.this, 100, R.drawable.confetti, 3000)
//                        .setSpeedRange(0.2f, 0.5f)
//                        .oneShot(answer, 100);
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
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
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
                cardToEdit = allFlashcards.get(curCard);
                startActivityForResult(intent, 3017);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation leftOutAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_out);
                final Animation rightInAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right_in);

                if (allFlashcards.size() == 0 || allFlashcards.size() == 1) return; // no next card
                int nextCard = getRandomNumber(0, allFlashcards.size() - 1);
                while (nextCard == curCard || allFlashcards.get(nextCard).getQuestion().equals(allFlashcards.get(curCard).getQuestion())) nextCard = getRandomNumber(0, allFlashcards.size() - 1);
                int finalNextCard = nextCard;
                leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        question.setRotationY(0);
                        answer2.setTranslationZ(-5);
                        question.setVisibility(View.VISIBLE);
                        answer2.setVisibility(View.INVISIBLE);
                        question.startAnimation(leftOutAnim);
                        if (answer2.getVisibility() == View.VISIBLE) answer2.startAnimation(leftOutAnim);
                        delete.startAnimation(leftOutAnim);
                        timer.startAnimation(leftOutAnim);
                        if (answer.getVisibility() != View.INVISIBLE) {
                            answer.startAnimation(leftOutAnim);
                            option1.startAnimation(leftOutAnim);
                            option2.startAnimation(leftOutAnim);
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        question.setText(allFlashcards.get(finalNextCard).getQuestion());
                        answer.setText(allFlashcards.get(finalNextCard).getAnswer());
                        option1.setText(allFlashcards.get(finalNextCard).getWrongAnswer1());
                        option2.setText(allFlashcards.get(finalNextCard).getWrongAnswer2());
                        if (!option1.getText().equals("") && answer.getVisibility() == View.VISIBLE) option1.setVisibility(View.VISIBLE);
                        else option1.setVisibility(View.INVISIBLE);
                        if (!option2.getText().equals("") && answer.getVisibility() == View.VISIBLE) option2.setVisibility(View.VISIBLE);
                        else option2.setVisibility(View.INVISIBLE);
                        answer2.setVisibility(View.INVISIBLE);
                        delete.startAnimation(rightInAnim);
                        question.startAnimation(rightInAnim);
                        timer.startAnimation(rightInAnim);
                        startTimer();
                        if (answer.getVisibility() != View.INVISIBLE) {
                            answer.startAnimation(rightInAnim);
                            option1.startAnimation(rightInAnim);
                            option2.startAnimation(rightInAnim);
                        }
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // we don't need to worry about this method
                    }
                });
                question.startAnimation(leftOutAnim);
                curCard = nextCard;
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flashcardDatabase.deleteCard(((TextView) findViewById(R.id.flashcard_question)).getText().toString());
                allFlashcards = flashcardDatabase.getAllCards();
                if (allFlashcards.size() == 0) {
                    question.setText("Add a card! :>");
                    answer.setText("");
                    option1.setText("");
                    option2.setText("");
                }
                else if (allFlashcards.size() == 1) {
                    question.setText(allFlashcards.get(0).getQuestion());
                    answer.setText(allFlashcards.get(0).getAnswer());
                    option1.setText(allFlashcards.get(0).getWrongAnswer1());
                    option2.setText(allFlashcards.get(0).getWrongAnswer2());
                    curCard = 0;
                }
                else {
                    int nextCard = getRandomNumber(0, allFlashcards.size() - 1);
                    while (nextCard == curCard) nextCard = getRandomNumber(0, allFlashcards.size() - 1);
                    question.setText(allFlashcards.get(nextCard).getQuestion());
                    answer.setText(allFlashcards.get(nextCard).getAnswer());
                    option1.setText(allFlashcards.get(nextCard).getWrongAnswer1());
                    option2.setText(allFlashcards.get(nextCard).getWrongAnswer2());
                    curCard = nextCard;
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setContentView(R.layout.activity_main);

        TextView question = findViewById(R.id.flashcard_question);
        TextView answer = findViewById(R.id.flashcard_answer);
        TextView answer2 = findViewById(R.id.flashcard_answer2);
        TextView option1 = findViewById(R.id.flashcard_option1);
        TextView option2 = findViewById(R.id.flashcard_option2);
        TextView timer = findViewById(R.id.timer);
        ImageView eye = ((ImageView) findViewById(R.id.toggle_choices_visibility));
        ImageView add = ((ImageView) findViewById(R.id.add_card));
        ImageView edit = ((ImageView) findViewById(R.id.edit_card));
        ImageView next = ((ImageView) findViewById(R.id.next_card));
        ImageView delete = ((ImageView) findViewById(R.id.delete_card));
        countDownTimer = new CountDownTimer(16000, 1000) {
            public void onTick(long millisUntilFinished) {
                timer.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
            }
        };

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            String newQuestion = data.getExtras().getString("newQuestion");
            String newAnswer = data.getExtras().getString("newAnswer");
            String newOption1 = data.getExtras().getString("newOption1");
            String newOption2 = data.getExtras().getString("newOption2");
            flashcardDatabase.insertCard(new Flashcard(newQuestion, newAnswer, newOption1, newOption2));
            allFlashcards = flashcardDatabase.getAllCards();
            curCard = allFlashcards.size() - 1;
            question.setText(newQuestion);
            answer.setText(newAnswer);
            option1.setText(newOption1);
            option2.setText(newOption2);
            Snackbar.make(findViewById(R.id.flashcard_question), "Card created.", Snackbar.LENGTH_SHORT).show();
        }
        else if (requestCode == 3017 && resultCode == RESULT_OK) {
            String newQuestion = data.getExtras().getString("newQuestion");
            String newAnswer = data.getExtras().getString("newAnswer");
            String newOption1 = data.getExtras().getString("newOption1");
            String newOption2 = data.getExtras().getString("newOption2");
            question.setText(newQuestion);
            answer.setText(newAnswer);
            option1.setText(newOption1);
            option2.setText(newOption2);
            Snackbar.make(findViewById(R.id.flashcard_question), "Edit successful.", Snackbar.LENGTH_SHORT).show();

            cardToEdit.setQuestion(newQuestion);
            cardToEdit.setAnswer(newAnswer);
            cardToEdit.setWrongAnswer1(newOption1);
            cardToEdit.setWrongAnswer2(newOption2);
            flashcardDatabase.updateCard(cardToEdit);
            allFlashcards = flashcardDatabase.getAllCards();
        }
        else if (resultCode == RESULT_CANCELED){
            question.setText(allFlashcards.get(curCard).getQuestion());
            answer.setText(allFlashcards.get(curCard).getAnswer());
            option1.setText(allFlashcards.get(curCard).getWrongAnswer1());
            option2.setText(allFlashcards.get(curCard).getWrongAnswer2());
        }
        startTimer();
        findViewById(R.id.parent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                answer.setBackgroundColor(getResources().getColor(R.color.purple_200, null));
                option1.setBackgroundColor(getResources().getColor(R.color.purple_200, null));
                option2.setBackgroundColor(getResources().getColor(R.color.purple_200, null));
            }
        });
        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answer2.setText(answer.getText());
                question.setCameraDistance(100000);
                answer.setCameraDistance(100000);
                question.animate().rotationY(90).setDuration(200)
                        .withEndAction(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        question.setVisibility(View.INVISIBLE);
                                        answer2.setVisibility(View.VISIBLE);
                                        // second quarter turn
                                        answer2.setRotationY(-90);
                                        answer2.animate().rotationY(0).setDuration(200).start();
                                    }
                                }
                        ).start();
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
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
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
                cardToEdit = allFlashcards.get(curCard);
                startActivityForResult(intent, 3017);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation leftOutAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_out);
                final Animation rightInAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right_in);

                if (allFlashcards.size() == 0 || allFlashcards.size() == 1) return; // no next card
                int nextCard = getRandomNumber(0, allFlashcards.size() - 1);
                while (nextCard == curCard || allFlashcards.get(nextCard).getQuestion().equals(allFlashcards.get(curCard).getQuestion())) nextCard = getRandomNumber(0, allFlashcards.size() - 1);
                int finalNextCard = nextCard;
                leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        question.setVisibility(View.VISIBLE);
                        question.setRotationY(0);
                        answer2.setVisibility(View.INVISIBLE);
                        answer2.setTranslationZ(-5);
                        question.startAnimation(leftOutAnim);
                        if (answer2.getVisibility() == View.VISIBLE) answer2.startAnimation(leftOutAnim);
                        delete.startAnimation(leftOutAnim);
                        timer.startAnimation(leftOutAnim);
                        if (answer.getVisibility() != View.INVISIBLE) {
                            answer.startAnimation(leftOutAnim);
                            option1.startAnimation(leftOutAnim);
                            option2.startAnimation(leftOutAnim);
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        question.setText(allFlashcards.get(finalNextCard).getQuestion());
                        answer.setText(allFlashcards.get(finalNextCard).getAnswer());
                        option1.setText(allFlashcards.get(finalNextCard).getWrongAnswer1());
                        option2.setText(allFlashcards.get(finalNextCard).getWrongAnswer2());
                        if (!option1.getText().equals("") && answer.getVisibility() == View.VISIBLE) option1.setVisibility(View.VISIBLE);
                        else option1.setVisibility(View.INVISIBLE);
                        if (!option2.getText().equals("") && answer.getVisibility() == View.VISIBLE) option2.setVisibility(View.VISIBLE);
                        else option2.setVisibility(View.INVISIBLE);
                        answer2.setVisibility(View.INVISIBLE);
                        delete.startAnimation(rightInAnim);
                        question.startAnimation(rightInAnim);
                        timer.startAnimation(rightInAnim);
                        startTimer();
                        if (answer.getVisibility() != View.INVISIBLE) {
                            answer.startAnimation(rightInAnim);
                            option1.startAnimation(rightInAnim);
                            option2.startAnimation(rightInAnim);
                        }
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // we don't need to worry about this method
                    }
                });
                question.startAnimation(leftOutAnim);
                curCard = nextCard;
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flashcardDatabase.deleteCard(((TextView) findViewById(R.id.flashcard_question)).getText().toString());
                allFlashcards = flashcardDatabase.getAllCards();
                if (allFlashcards.size() == 0) {
                    question.setText("Add a card! :>");
                    answer.setText("");
                    option1.setText("");
                    option2.setText("");
                }
                else if (allFlashcards.size() == 1) {
                    question.setText(allFlashcards.get(0).getQuestion());
                    answer.setText(allFlashcards.get(0).getAnswer());
                    option1.setText(allFlashcards.get(0).getWrongAnswer1());
                    option2.setText(allFlashcards.get(0).getWrongAnswer2());
                    curCard = 0;
                }
                else {
                    int nextCard = getRandomNumber(0, allFlashcards.size() - 1);
                    while (nextCard == curCard) nextCard = getRandomNumber(0, allFlashcards.size() - 1);
                    question.setText(allFlashcards.get(nextCard).getQuestion());
                    answer.setText(allFlashcards.get(nextCard).getAnswer());
                    option1.setText(allFlashcards.get(nextCard).getWrongAnswer1());
                    option2.setText(allFlashcards.get(nextCard).getWrongAnswer2());
                    curCard = nextCard;
                }
            }
        });
    }
    private int getRandomNumber(int minNum, int maxNum) {
        Random rand = new Random();
        return rand.nextInt((maxNum - minNum) + 1) + minNum;
    }
    private void startTimer() {
        countDownTimer.cancel();
        countDownTimer.start();
    }
}
