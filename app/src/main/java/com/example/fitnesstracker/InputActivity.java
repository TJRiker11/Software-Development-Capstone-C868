package com.example.fitnesstracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.sql.Date;

public class InputActivity extends CalorieTrackerActivity {
    private boolean existing; // whether editing existing meal or adding a new meal
    private int mealID; // meal id of this meal
    private Meal m; // Meal object being edited
    private DatabaseHandler db; // database handler
    private Date date; // date meal was eaten
    private static final int RESULT_OK = 400; // result ok code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        // set up database handler and get intent extras
        db = new DatabaseHandler(getApplicationContext());
        existing = getIntent().getBooleanExtra("EXISTING", false);
        mealID = getIntent().getIntExtra("MEAL_ID", -1);
        long intentDate = getIntent().getLongExtra("date", System.currentTimeMillis());
        date = new Date(intentDate);


        if (existing) {
            // fetch meal from db
            m = db.getMeal(mealID);
            int type = m.getTypeCode();

            // set spinner value to the correct one
            Spinner mealTypeSpinner = findViewById(R.id.mealtype_spinner);
            mealTypeSpinner.setSelection(type);

            // EditText fields
            EditText calories = findViewById(R.id.calories);
            EditText meal = findViewById(R.id.meal);
            EditText fat = findViewById(R.id.fat);
            EditText carbs = findViewById(R.id.carbs);
            EditText protein = findViewById(R.id.protein);

            // set text fields to the correct values
            meal.setText(m.getName());
            calories.setText(Integer.toString(m.getCalories()));
            fat.setText(Double.toString(m.getfat()));
            carbs.setText(Double.toString(m.getCarbs()));
            protein.setText(Double.toString(m.getProtein()));
        } else {
            // hide delete button if this meal is not new
            Button deleteButton = (Button) findViewById(R.id.delete_button);
            deleteButton.setVisibility(View.INVISIBLE);
        }
    }


    // Helper function to take user to main menu
    private void toMainMenu() {
        Intent menu = new Intent(this, MainActivity.class);
        startActivity(menu);
    }

    // click handler for submit button
    public void onSubmitClick(View v){
        // returning intent
        Intent i = new Intent();

        // EditText fields
        EditText calories = (EditText) findViewById(R.id.calories);
        EditText meal = (EditText) findViewById(R.id.meal);
        EditText fat = (EditText) findViewById(R.id.fat);
        EditText carbs = (EditText) findViewById(R.id.carbs);
        EditText protein = (EditText) findViewById(R.id.protein);

        if (!existing) {
            // get spinner value and set meal type correctly
            Spinner mealTypeSpinner = (Spinner) findViewById(R.id.mealtype_spinner);
            int typeCode = mealTypeSpinner.getSelectedItemPosition();

            if(meal.getText().toString().length() == 0){
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);

                dialog.setMessage("Please enter a name for the meal.");

                // Set up the buttons
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                dialog.show();
                return;
            }

            Meal thisMeal = new Meal(meal.getText().toString(), date, typeCode, 0);

            if (calories.length() != 0)
                thisMeal.setCalories(Integer.parseInt(calories.getText().toString()));
            if (carbs.length() != 0)
                thisMeal.setCarbs(Double.parseDouble(carbs.getText().toString()));
            if (protein.length() != 0)
                thisMeal.setProtein(Double.parseDouble(protein.getText().toString()));
            if (fat.length() != 0)
                thisMeal.setfat(Double.parseDouble(fat.getText().toString()));

            db.addMeal(thisMeal);

            setResult(RESULT_OK, i);
            finish();
        } else {
            // get spinner value and set meal type correctly
            Spinner mealTypeSpinner = (Spinner) findViewById(R.id.mealtype_spinner);

            // make sure meal name isn't empty
            if(meal.getText().toString().length() == 0){
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);

                dialog.setMessage("Please enter a name for the meal.");

                // Set up the buttons
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                dialog.show();
                return;
            }

            if (calories.length() != 0)
                m.setCalories(Integer.parseInt(calories.getText().toString()));
            if (carbs.length() != 0)
                m.setCarbs(Double.parseDouble(carbs.getText().toString()));
            if (protein.length() != 0)
                m.setProtein(Double.parseDouble(protein.getText().toString()));
            if (fat.length() != 0)
                m.setfat(Double.parseDouble(fat.getText().toString()));

            db.updateMeal(m);

            setResult(RESULT_OK, i);
            finish();
        }

    }

    // click handler for click of delete button
    public void onDeleteClick(View v){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setMessage("Are you sure you want to delete this meal? " +
                "The meal will be permanently deleted.");

        // Set up the buttons
        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // returning intent
                Intent i = new Intent();

                if (existing) {
                    db.deleteMeal(mealID);
                    setResult(RESULT_OK, i);
                    finish();
                } else {
                    finish();
                }
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();

    }
}

