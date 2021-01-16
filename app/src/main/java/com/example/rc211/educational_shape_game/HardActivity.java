package com.example.rc211.educational_shape_game;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class HardActivity extends AppCompatActivity {
    private TextView commandView, timerView, ansDisp, ptsView;
    private ImageView temp;
    private EditText editText;
    private int points;
    //shapes array (randomly put 15 shapes into it for display)
    private ArrayList<Integer> shapesIntArray;
    //shapes imageView array
    private ArrayList<ImageView> shapesImgArray;
    //current combo array
    private ArrayList<Integer> currentIntCombo;
    private ArrayList<ImageView> currentImgCombo;
    //array of the player's answers as clicked
    private ArrayList<ImageView> ansArray;
    //array of the generated answers
    private ArrayList<ImageView> rArray;

    //points system, right answers: gain 10 points, wrong answers: lose 10 points

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hard);

        commandView = findViewById(R.id.commandView);
        timerView = findViewById(R.id.timerView);
        ansDisp = findViewById(R.id.ansDisp);
        ptsView = findViewById(R.id.ptsView);

        temp = findViewById(R.id.temp);
        editText = findViewById(R.id.editText);

        shapesIntArray = new ArrayList<>();
        shapesImgArray = new ArrayList<>();
        currentIntCombo = new ArrayList<>();
        currentImgCombo = new ArrayList<>();
        ansArray = new ArrayList<>();
        rArray = new ArrayList<>();

        Collections.addAll(shapesImgArray, (ImageView)findViewById(R.id.img1), (ImageView)findViewById(R.id.img2), (ImageView)findViewById(R.id.img3), (ImageView)findViewById(R.id.img4),
                (ImageView)findViewById(R.id.img5), (ImageView)findViewById(R.id.img6), (ImageView)findViewById(R.id.img7), (ImageView)findViewById(R.id.img8),
                (ImageView)findViewById(R.id.img9), (ImageView)findViewById(R.id.img10), (ImageView)findViewById(R.id.img11), (ImageView)findViewById(R.id.img12),
                (ImageView)findViewById(R.id.img13), (ImageView)findViewById(R.id.img14), (ImageView)findViewById(R.id.img15));

        Collections.addAll(ansArray, (ImageView)findViewById(R.id.ans1), (ImageView)findViewById(R.id.ans2), (ImageView)findViewById(R.id.ans3), (ImageView)findViewById(R.id.ans4),
                (ImageView)findViewById(R.id.ans5), (ImageView)findViewById(R.id.ans6), (ImageView)findViewById(R.id.ans7), (ImageView)findViewById(R.id.ans8),
                (ImageView)findViewById(R.id.ans9));

        Collections.addAll(rArray, (ImageView)findViewById(R.id.r1), (ImageView)findViewById(R.id.r2), (ImageView)findViewById(R.id.r3), (ImageView)findViewById(R.id.r4),
                (ImageView)findViewById(R.id.r5), (ImageView)findViewById(R.id.r6), (ImageView)findViewById(R.id.r7), (ImageView)findViewById(R.id.r8),
                (ImageView)findViewById(R.id.r9));

        for (ImageView i : ansArray){
            i.setVisibility(View.INVISIBLE);
        }

        timer(); //starts 15 sec timer

        generateShapesArray();
        generateCommand();
        clickShapesAndDisp();

    }

    public void generateShapesArray(){ //creates the shape array with 15 random shapes put into it. These are the shapes that will be displayed.
        shapesIntArray.clear();

        for (int i = 0; i < 15; i++){
            shapesIntArray.add((int)(Math.random() * 9) + 1); //picks a random number between 1 and 9 to represent a shape.
        }

        for (int i = 0; i < shapesIntArray.size(); i++) { //sets the corresponding shapesImgArray to the same shapes as the shapesIntArray
            if (shapesIntArray.get(i) == 1) {
                shapesImgArray.get(i).setImageResource(R.drawable.pink_semicircle);
            } else if (shapesIntArray.get(i) == 2) {
                shapesImgArray.get(i).setImageResource(R.drawable.quarter_circle);
            } else if (shapesIntArray.get(i) == 3) {
                shapesImgArray.get(i).setImageResource(R.drawable.green_triangle);
            } else if (shapesIntArray.get(i) == 4) {
                shapesImgArray.get(i).setImageResource(R.drawable.orange_trap);
            } else if (shapesIntArray.get(i) == 5) {
                shapesIntArray.set(i, 41);
                shapesImgArray.get(i).setImageResource(R.drawable.red_square);
            } else if (shapesIntArray.get(i) == 6) {
                shapesIntArray.set(i, 5);
                shapesImgArray.get(i).setImageResource(R.drawable.yellow_pentagon);
            } else if (shapesIntArray.get(i) == 7) {
                shapesIntArray.set(i, 6);
                shapesImgArray.get(i).setImageResource(R.drawable.blue_hexagon);
            } else if (shapesIntArray.get(i) == 8) {
                shapesImgArray.get(i).setImageResource(R.drawable.red_octagon);
            } else if (shapesIntArray.get(i) == 9) {
                shapesIntArray.set(i, 10);
                shapesImgArray.get(i).setImageResource(R.drawable.star);
            }
        }
    }

    private int sum, amtShapes;
    public void generateCommand(){ //generates the command array with operations in the middle
        currentIntCombo.clear();
        currentImgCombo.clear();

        int determinant = 0; //if 0, the problem will involve addition/subtraction, if 1, the problem will involve multiplication
        determinant = (int)(Math.random()*2);

        if (determinant == 0){ //involving addition/subtraction
            amtShapes = 0; //the amount of shapes used in the pattern (3 or higher for addition and subtraction, 2 only for multiplication)
            amtShapes = (int)((Math.random() * 3) + 3); //generates 3, 4, or 5

            for (int i = 0; i < amtShapes * 2 - 1; i++){ //sets all the imageViews needed in the answer combo to visible
                ansArray.get(i).setVisibility(View.VISIBLE);
            }

            ArrayList<Integer> usedIndices = new ArrayList<>();

            for (int i = 0; i < amtShapes; i++){
                int pickedIndex = 0;
                pickedIndex = (int)(Math.random() * shapesIntArray.size());

                while (!usedIndices.isEmpty() && usedIndices.contains(pickedIndex)){
                    pickedIndex = (int)(Math.random() * shapesIntArray.size());
                }

                usedIndices.add(pickedIndex);
            }


            do{ //adds the created combo with operations in between to rArray. If the sum created is less than 0, this is done again.
                sum = 0;
                int counter = 0;
                int sign = 100;
                currentIntCombo.clear();

                for (int k : usedIndices){
                    currentIntCombo.add(shapesIntArray.get(k));
                    rArray.get(counter).setImageDrawable(shapesImgArray.get(k).getDrawable());
                    if (sign == 100){
                        if (shapesIntArray.get(k) == 41){
                            sum +=  4;
                        }
                        else{
                            sum += shapesIntArray.get(k);
                        }
                    }
                    else{
                        if (shapesIntArray.get(k) == 41){
                            sum -=  4;
                        }
                        else{
                            sum -= shapesIntArray.get(k);
                        }
                    }

                    counter++;

                    if (usedIndices.indexOf(k) != usedIndices.size() - 1){ //don't want an operation sign after the last number
                        sign = (int)(Math.random() * 2)+100;
                        currentIntCombo.add(sign);

                        if (sign == 100){ //plus
                            rArray.get(counter).setImageResource(R.drawable.pluss);
                            ansArray.get(counter).setImageResource(R.drawable.pluss);
                        }
                        else if (sign == 101){ //minus
                            rArray.get(counter).setImageResource(R.drawable.minuss);
                            ansArray.get(counter).setImageResource(R.drawable.minuss);
                        }
                        counter ++;
                    }
                }
            }
            while (sum < 0); //avoiding negative answers

        }
        else{ //involving multiplication
            sum = 1;

            for (int i = 0; i < 3; i++){ //sets all the imageViews needed in the answer combo to visible
                ansArray.get(i).setVisibility(View.VISIBLE);
            }

            int pickedIndex1 = (int)(Math.random() * shapesIntArray.size());
            int pickedIndex2 = (int)(Math.random() * shapesIntArray.size());

            while (pickedIndex1 == pickedIndex2){
                pickedIndex2 = (int)(Math.random() * shapesIntArray.size());
            }

            currentIntCombo.add(shapesIntArray.get(pickedIndex1));
            currentIntCombo.add(102);
            currentIntCombo.add(shapesIntArray.get(pickedIndex2));

            if (shapesIntArray.get(pickedIndex1) == 41){
                sum *= 4;
            }
            else{
                sum *= shapesIntArray.get(pickedIndex1);
            }

            if (shapesIntArray.get(pickedIndex2) == 41){
                sum *= 4;
            }
            else{
                sum *= shapesIntArray.get(pickedIndex2);
            }

            rArray.get(0).setImageDrawable(shapesImgArray.get(pickedIndex1).getDrawable());
            rArray.get(1).setImageResource(R.drawable.multi);
            rArray.get(2).setImageDrawable(shapesImgArray.get(pickedIndex2).getDrawable());

            ansArray.get(1).setImageResource(R.drawable.multi);
        }

        dispCommand();

        //TO SUMMARIZE:
        //randomly picks a "random" amount of shapes from the shapes array as the combo
        //set up the bottom display too (with empty boxes seperated by operation signs)
    }

    public void dispCommand(){ //displays command line, telling the player what to do. Example: "hexagon + semicircle"
        String s = "";
        for (int i : currentIntCombo){
            if (i == 1){
                s += "Semicircle";
            }
            else if (i == 2){
                s += "Quarter circle";
            }
            else if (i == 3){
                s += "Triangle";
            }
            else if (i == 4){
                s += "Trapezoid";
            }
            else if (i == 41){
                s += "Square";
            }
            else if (i == 5){
                s += "Pentagon";
            }
            else if (i == 6){
                s += "Hexagon";
            }
            else if (i == 8){
                s += "Octagon";
            }
            else if (i == 10){
                s += "Star";
            }
            else if (i == 100){
                s += " + ";
            }
            else if (i == 101){
                s += " - ";
            }
            else if (i == 102){
                s += " x ";
            }
        }
        commandView.setText(s);
    }

    private int clickCount = 0;
    private ArrayList<ImageView> clickedImgViews = new ArrayList<>();

    public void clickShapesAndDisp(){ //if a shape is clicked, it will appear at the bottom of the screen
        for (final ImageView i : shapesImgArray){
            i.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (clickCount <= currentIntCombo.size()){
                        //undoPresses = 0;
                        ansArray.get(clickCount).setImageDrawable(i.getDrawable());
                        i.setVisibility(View.INVISIBLE);
                        clickedImgViews.add(i);
                        clickCount += 2;
                    }
                }
            });
        }
    }

    //private int undoPresses = 0; //the amount of times undo has been pressed in a row
    public void undo(View view){ //if player presses undo button, image reappears in pool and is enabled again. The bottom display is reset back to the box.
        if (clickCount > 0){
            clickCount -= 2;
            ansArray.get(clickCount).setImageDrawable(temp.getDrawable());
            clickedImgViews.get(clickedImgViews.size()-1).setVisibility(View.VISIBLE);
            clickedImgViews.remove(clickedImgViews.size()-1);
            //undoPresses ++;
        }
    }


    public void checkAnswer(){ //if answer is incorrect, right solution appears at bottom of page.
        //System.out.println("Sum: " + sum);
            int k = 0;
            boolean isDispSet = false;
            for (int i = 0; i < clickedImgViews.size(); i++){
                System.out.println("clickedImgViews.get(i)" + clickedImgViews.get(i));
                System.out.println("rArray.get(k)" + rArray.get(k));

                Bitmap bitmap = ((BitmapDrawable)clickedImgViews.get(i).getDrawable()).getBitmap();
                Bitmap bitmap2 = ((BitmapDrawable)rArray.get(k).getDrawable()).getBitmap();

                if (bitmap != bitmap2){ //the combo the player made is incorrect
                    if (!editText.getText().toString().equals("") && Integer.parseInt(editText.getText().toString()) == sum){ //the answer (single number) is still correct
                        ansDisp.setText("Wrong shape expression but right answer!");
                        points += 10;
                    }
                    else if (!editText.getText().toString().equals("") && Integer.parseInt(editText.getText().toString()) != sum){
                        ansDisp.setText("WRONG EXPRESSION AND ANSWER!");
                        points -= 20;
                    }
                    isDispSet = true;

                    for (int t = 0; t < amtShapes * 2 - 1; t++){ //makes answer combo visible, since the player got it wrong (so they can check what they did wrong)
                        rArray.get(t).setVisibility(View.VISIBLE);
                    }
                }
                k += 2;
            }
            if (!isDispSet){
                if (!editText.getText().toString().equals("") && Integer.parseInt(editText.getText().toString()) == sum){
                    ansDisp.setText("CORRECT! WAY TO GO!!!");
                    points += 20;
                }
                else if (!editText.getText().toString().equals("") && Integer.parseInt(editText.getText().toString()) != sum){
                    ansDisp.setText("Wrong Answer! It was: " + sum);
                    points -= 10;
                }
                else if (editText.getText().toString().equals("")){
                    ansDisp.setText("You didn't give an answer!");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() { //everything in here is executed after 5 seconds
                            for (ImageView i : rArray){ //makes all elements of the right combination array invisible again
                                i.setVisibility(View.INVISIBLE);
                            }
                            for (ImageView i : ansArray){ //makes all elements of the right combination array invisible again and sets them back to the original images (the gray color)
                                i.setImageDrawable(temp.getDrawable());
                                i.setVisibility(View.INVISIBLE);
                            }
                            for (ImageView i : shapesImgArray){ //makes the selection shapes on the screen all visible again
                                i.setVisibility(View.VISIBLE);
                            }

                            goRan = false;
                            ansDisp.setText("");
                            editText.setText("");
                            clickCount = 0;
                            clickedImgViews.clear();

                            cTimer.cancel();
                            timer(); //starts 40 sec timer
                            generateShapesArray();
                            generateCommand();
                            clickShapesAndDisp();
                        }
                    }, 5000);

                }
            }
            ptsView.setText("Points: " + points);
    }

    boolean goRan = false;
    public void go(View view){ //checks answer if go button is clicked
        goRan = true;
        if (isTimerRunning){
            checkAnswer();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() { //everything in here is executed after 8 seconds
                for (ImageView i : rArray){ //makes all elements of the right combination array invisible again
                    i.setVisibility(View.INVISIBLE);
                }
                for (ImageView i : ansArray){ //makes all elements of the right combination array invisible again and sets them back to the original images (the gray color)
                    i.setImageDrawable(temp.getDrawable());
                    i.setVisibility(View.INVISIBLE);
                }
                for (ImageView i : shapesImgArray){ //makes the selection shapes on the screen all visible again
                    i.setVisibility(View.VISIBLE);
                }

                goRan = false;
                ansDisp.setText("");
                editText.setText("");
                clickCount = 0;
                clickedImgViews.clear();

                cTimer.cancel();
                timer(); //starts 50 sec timer
                generateShapesArray();
                generateCommand();
                clickShapesAndDisp();
            }
        }, 8000);
    }

    boolean isTimerRunning;
    CountDownTimer cTimer;
    public void timer(){ //50 seconds to answer each question.
        isTimerRunning = true;
        cTimer = new CountDownTimer(50000, 1000) {

            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished / 1000 >= 10){ //if double digit time
                    timerView.setText("   00:" + millisUntilFinished / 1000);
                }
                else{ //if single digit time
                    timerView.setText("   00:0" + millisUntilFinished / 1000);
                }
            }

            public void onFinish() {
                if (!goRan){
                    checkAnswer();
                }
                isTimerRunning = false;
                timerView.setText("TIME'S UP!");
            }
        }.start();
    }

    public void mainMenu(View view){ //goes back to main menu
        Intent intent = new Intent(HardActivity.this, MainActivity.class);
        startActivity(intent);
    }


}
