package com.example.rc211.educational_shape_game;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class LearnerModeActivity extends AppCompatActivity {

    private PopupWindow myPopup;

    private Button hintBtn;

    private ArrayList<ImageView> shapeList = new ArrayList<>();
    private ArrayList<Integer> corrShapeList;
    private ArrayList<Integer> currentComboList;

    private int layoutHeight, layoutWidth; //height and width of screen
    int clickCounter = 0; //the number of times the player has clicked on the hint button after the pop up window has opened.

    private int answer, rightAnsCount, points = 0;

    private EditText inputText;

    private TextView commandView, hint1View, hint2View, hint3View, ansView, pointsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learner_mode);
        hintBtn = (Button) findViewById(R.id.hintBtn);

        corrShapeList = new ArrayList<>();
        currentComboList = new ArrayList<>();

        answer = 0;
        rightAnsCount = 0;

        inputText = findViewById(R.id.inputText);
        commandView = findViewById(R.id.commandView);
        pointsView = findViewById(R.id.pointsView);

        //shapeList.add((ImageView)findViewById(R.id.pent1));
        Collections.addAll(shapeList, (ImageView)findViewById(R.id.semi1), (ImageView)findViewById(R.id.semi2), (ImageView)findViewById(R.id.semi3), (ImageView)findViewById(R.id.semi4), (ImageView)findViewById(R.id.semi5),
                (ImageView)findViewById(R.id.quar1), (ImageView)findViewById(R.id.quar2), (ImageView)findViewById(R.id.quar3), (ImageView)findViewById(R.id.quar4), (ImageView)findViewById(R.id.quar5),
                (ImageView)findViewById(R.id.tri1), (ImageView)findViewById(R.id.tri2), (ImageView)findViewById(R.id.tri3), (ImageView)findViewById(R.id.tri4), (ImageView)findViewById(R.id.tri5),
                (ImageView)findViewById(R.id.trap1), (ImageView)findViewById(R.id.trap2), (ImageView)findViewById(R.id.trap3), (ImageView)findViewById(R.id.trap4), (ImageView)findViewById(R.id.trap5),
                (ImageView)findViewById(R.id.square1), (ImageView)findViewById(R.id.square2), (ImageView)findViewById(R.id.square3), (ImageView)findViewById(R.id.square4), (ImageView)findViewById(R.id.square5),
                (ImageView)findViewById(R.id.pent1), (ImageView)findViewById(R.id.pent2), (ImageView)findViewById(R.id.pent3), (ImageView)findViewById(R.id.pent4), (ImageView)findViewById(R.id.pent5),
                (ImageView)findViewById(R.id.hex1), (ImageView)findViewById(R.id.hex2), (ImageView)findViewById(R.id.hex3), (ImageView)findViewById(R.id.hex4), (ImageView)findViewById(R.id.hex5));

        Collections.addAll(corrShapeList, 1, 1, 1, 1, 1, //semicircle
                2, 2, 2, 2, 2, //quarter circle
                3, 3, 3, 3, 3, //triangle
                40, 40, 40, 40, 40, //tapezoid
                41, 41, 41, 41, 41, //square
                5, 5, 5, 5, 5, //pentagon
                6, 6, 6, 6, 6); //hexagon

        //In order to get  screen size x and y coordinates:
        WindowManager windowManager = getWindowManager(); //handles window transitions and animations (when rotating phone or opening/closing app)
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point(); //point object stores the coordinates (x and y) of a specific point
        display.getSize(point); //finds the farthest point (bottom right) on the display, and those x and y coordinates describe the length and width of the display
        layoutHeight = point.y;
        layoutWidth = point.x;

        createCombo();
    }

    public void checkAnswer(View view){ // On-click, checks if the answer inputted is correct or not when "Go" button is pressed.
        if (inputText.getText().toString().equals(answer + "")){ //if the user answers correctly
            rightAnsCount ++;

            if (count == 4){
                count = 2;
            }
            else{
                count++;
            }

            Toast.makeText(this, "GREAT JOB! Next Question!", Toast.LENGTH_LONG).show();

            points += 100;

            //resets boolean values for next question
            firstHintSeen = false;
            secondHintSeen = false;
            thirdHintSeen = false;
            ansSeen = false;

            createCombo();
        }
        else{ //if the user answers incorrectly
            points -= 15;
            Toast.makeText(this, "No, that's wrong! Try again, or use a hint!", Toast.LENGTH_LONG).show();
            if (rightAnsCount != 1){ //if the player is not on spot one.
                //places the player back one spot, so that they have to repeat a scenerio in the same difficulty range and with the same number of shapes.
                rightAnsCount --;

                if (count == 2){
                    count = 4;
                }
                else{
                    count--;
                }
            }
        }
        inputText.setText(""); //clears the input field

        if (points < 0){ //kids don't like having negative scores!
            points = 0;
        }
        pointsView.setText("Points: " + points);
    }


    private boolean firstHintSeen = false;
    private boolean secondHintSeen = false;
    private boolean thirdHintSeen = false;
    private boolean ansSeen = false;

    public void hintClick(View view){ //hint button On-Click
        clickCounter = 0;
        LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        View customView = inflater.inflate(R.layout.activity_pop_up, null);

        myPopup = new PopupWindow( //creates a Popupwindow using the customView xml
                customView,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        myPopup.setWidth(1000);
        myPopup.setHeight(1300);

        ImageButton closeButton = (ImageButton) customView.findViewById(R.id.close);
        Button showBtn = (Button) customView.findViewById(R.id.showBtn);

        ArrayList<ImageView> imageViews = new ArrayList<>(); //array of the imageViews where the current shape combo will appear
        ArrayList<ImageView> signs = new ArrayList<>();
        ArrayList<TextView> nums = new ArrayList<>();

        imageViews.add((ImageView)customView.findViewById(R.id.imageView));
        imageViews.add((ImageView)customView.findViewById(R.id.imageView2));
        imageViews.add((ImageView)customView.findViewById(R.id.imageView3));
        imageViews.add((ImageView)customView.findViewById(R.id.imageView4));

        signs.add((ImageView)customView.findViewById(R.id.sign1));
        signs.add((ImageView)customView.findViewById(R.id.sign2));
        signs.add((ImageView)customView.findViewById(R.id.sign3));

        nums.add((TextView)customView.findViewById(R.id.num1));
        nums.add((TextView)customView.findViewById(R.id.num2));
        nums.add((TextView)customView.findViewById(R.id.num3));
        nums.add((TextView)customView.findViewById(R.id.num4));

        hint1View = (TextView)customView.findViewById(R.id.hint1View);
        hint2View = (TextView)customView.findViewById(R.id.hint2View);
        hint3View = (TextView)customView.findViewById(R.id.hint3View);
        ansView = (TextView)customView.findViewById(R.id.ansView);

        for (ImageView i : imageViews){ //sets the imageViews back to their original backgrounds (incase one of them isn't used in this combo, it remains the color of the background so no one can tell)
            i.setImageResource(R.drawable.white);
        }

        for (TextView n : nums){ //clears all the textViews, in case any of them aren't used in the new combo
            n.setText("");
        }

        for (ImageView s : signs){ //sets the imageViews back to their original backgrounds
            s.setImageResource(R.drawable.white);
        }

        for (int i = 0; i < currentComboList.size()-1; i++){ //sets the "middle" imageViews to the plus sign
            signs.get(i).setImageResource(R.drawable.plus);
        }

        int arrayCount = 0;
        for (Integer i : currentComboList){
            if (i == 1){
                imageViews.get(arrayCount).setImageResource(R.drawable.pink_semicircle);
            }
            else if (i == 2){
                imageViews.get(arrayCount).setImageResource(R.drawable.quarter_circle);
            }
            else if (i == 3){
                imageViews.get(arrayCount).setImageResource(R.drawable.green_triangle);
            }
            else if (i == 40){
                imageViews.get(arrayCount).setImageResource(R.drawable.orange_trap);
            }
            else if (i == 41){
                imageViews.get(arrayCount).setImageResource(R.drawable.red_square);
            }
            else if (i == 5){
                imageViews.get(arrayCount).setImageResource(R.drawable.yellow_pentagon);
            }
            else if (i == 6){
                imageViews.get(arrayCount).setImageResource(R.drawable.blue_hexagon);
            }
            nums.get(arrayCount).setText(i.toString().substring(0,1)+"");
            arrayCount++;
        }

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPopup.dismiss(); //closes out of the popUp window
            }
        });

        showBtn.setOnClickListener(new View.OnClickListener(){ //Every time the hint button is clicked, a new hint is given.
            @Override
            public void onClick(View v) {
                if (clickCounter == 0){ //the first hint is just all the numbers displayed out as circles, so the player can count the circles to get the answer
                    String disp = "";
                    for (int i = 0; i < currentComboList.size(); i++){
                        for (int k = 0; k < Integer.parseInt(currentComboList.get(i).toString().substring(0,1)); k++){
                            disp += "o";
                        }
                        if (i != currentComboList.size()-1){
                            disp += " + ";
                        }
                    }
                    hint1View.setText(disp);

                    if (!firstHintSeen){ //only subtracts points the first time the hint is seen, not if they choose to open and close the popup over and over and see the same hint.
                        firstHintSeen = true;
                        points -= 5;
                    }
                }
                else if (clickCounter == 1){ //second hint is the sum of the first two numbers
                    int sum = Integer.parseInt(currentComboList.get(0).toString().substring(0,1)) + Integer.parseInt(currentComboList.get(1).toString().substring(0,1));
                    hint2View.setText(currentComboList.get(0).toString().substring(0,1) + " + " + currentComboList.get(1).toString().substring(0,1) + " = " + sum);

                    if (!secondHintSeen){ //only subtracts points the first time the hint is seen, not if they choose to open and close the popup over and over and see the same hint.
                        secondHintSeen = true;
                        points -= 10;
                    }
                }
                else if (clickCounter == 2){ //third hint is the sum of the first three numbers, if the combo has atleast 3 numbers. If not, nothing happens
                    if (currentComboList.size() >= 3){
                        String disp = "";
                        int sum = 0;
                        for (int i = 0; i <3; i++){
                            sum += Integer.parseInt(currentComboList.get(i).toString().substring(0,1));
                            if (i != 2){
                                disp += Integer.parseInt(currentComboList.get(i).toString().substring(0,1)) + " + ";
                            }
                            else{
                                disp += Integer.parseInt(currentComboList.get(i).toString().substring(0,1)) + " = ";
                            }
                        }
                        disp += sum;
                        hint3View.setText(disp);

                        if (!thirdHintSeen){
                            thirdHintSeen = true;
                            points -=15;
                        }
                    }
                    else{ //gives answer
                        ansView.setText("The answer is: " + answer);

                        if (!ansSeen){
                            ansSeen = true;
                            points -= 20;
                        }
                    }
                }
                else{ //gives answer
                    ansView.setText("The answer is: " + answer);

                    if (!ansSeen){
                        ansSeen = true;
                        points -= 20;
                    }
                }
                clickCounter++;

                if (points < 0){ //kids don't like having negative scores!
                    points = 0;
                }
                pointsView.setText("Points: " + points);
            }
        });

        //myPopup.showAsDropDown(customView);
        myPopup.showAtLocation(customView,0,20,10);

        customView.setOnTouchListener(new View.OnTouchListener(){
            int myX, myY, offsetX, offsetY;
            @Override
            public boolean onTouch(View v, MotionEvent event){
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        myX = (int) event.getX();
                        myY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        offsetX = (int) event.getRawX() - myX;
                        offsetY = (int) event.getRawY() - myY;
                        myPopup.update(offsetX, offsetY, -1, -1, true);
                        break;
                }
                return true;
            }
        });
    }

    int count = 2; //represents the number of shapes involved (2, 3, or 4)
    public void createCombo(){ //creates a combo and calculates the answer to that combo
        clickCounter = 0;
        //sets all of the imageViews out of the screen initially.
        for (ImageView i: shapeList){
            i.setX(layoutWidth + i.getWidth());
            i.setY(layoutHeight - i.getHeight()-200);
        }

        currentComboList.clear();
        int y = 0;
        answer = 0;
        ArrayList<Integer> randomIndices = new ArrayList<>();

        if (rightAnsCount < 4){ //easy mode, uses the first 15 shapes only.

            for (int i = 0; i < count; i++){

                int indexChosen = (int)(Math.random() * 15);
                while (randomIndices.contains(indexChosen)){ //this way, no imageView will accidently be called twice.
                    indexChosen = (int)(Math.random() * 15);
                }
                randomIndices.add(indexChosen);

                answer += Integer.parseInt(corrShapeList.get(indexChosen).toString().substring(0,1));
                System.out.println("answer: " + answer);
                System.out.println("Index chosen: " + indexChosen);
                currentComboList.add(corrShapeList.get(indexChosen));

                shapeList.get(indexChosen).setX(layoutWidth/2);
                shapeList.get(indexChosen).setY(y);
                y+= 250; //for spacing purposes (on shape display)
            }
        }
        else if (rightAnsCount >= 4 && rightAnsCount < 7){ //medium mode, uses the middle shapes
            for (int i = 0; i < count; i++){

                int indexChosen = (int)(Math.random() * 15 + 10);
                while (randomIndices.contains(indexChosen)){ //this way, no imageView will accidently be called twice.
                    indexChosen = (int)(Math.random() * 25);
                }
                randomIndices.add(indexChosen);

                answer += Integer.parseInt(corrShapeList.get(indexChosen).toString().substring(0,1));
                System.out.println("answer: " + answer);
                System.out.println("Index chosen: " + indexChosen);
                currentComboList.add(corrShapeList.get(indexChosen));

                shapeList.get(indexChosen).setX(layoutWidth/2);
                shapeList.get(indexChosen).setY(y);
                y+= 250; //for spacing purposes (on shape display)
            }
        }
        else if (rightAnsCount >= 7 && rightAnsCount < 10){ //hard mode, uses the shapeList arrayList, including all the shapes
            for (int i = 0; i < count; i++){

                int indexChosen = (int)(Math.random() * shapeList.size());
                while (randomIndices.contains(indexChosen)){ //this way, no imageView will accidently be called twice.
                    indexChosen = (int)(Math.random() * shapeList.size());
                }
                randomIndices.add(indexChosen);

                answer += Integer.parseInt(corrShapeList.get(indexChosen).toString().substring(0,1));
                System.out.println("answer: " + answer);
                System.out.println("Index chosen: " + indexChosen);
                currentComboList.add(corrShapeList.get(indexChosen));

                shapeList.get(indexChosen).setX(layoutWidth/2);
                shapeList.get(indexChosen).setY(y);
                y+= 250; //for spacing purposes (on shape display)
            }
        }
    }

    public void menu(View view){
        Intent intent = new Intent(LearnerModeActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
