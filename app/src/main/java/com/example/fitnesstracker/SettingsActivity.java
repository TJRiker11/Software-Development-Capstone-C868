package com.example.fitnesstracker;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ajts.androidmads.library.SQLiteToExcel;

import java.io.File;

public class SettingsActivity extends CalorieTrackerActivity {

    // class variables to define keys used to refer to respective nutritional values
    public static final String calorieKey = "calories";
    public static int caloricLimit;
    public static final int caloricDefault = 0;

    public static final String proteinKey = "protein";
    public static int proteinLimit;
    public static final int proteinDefault = 0;

    public static final String carbKey = "carbs";
    public static int carbLimit;
    public static final int carbDefault = 0;

    public static final String fatKey = "fat";
    public static int fatLimit;
    public static final int fatDefault = 0;

    public static final String weightKey = "weight";
    public static int weightLimit;
    public static final int weightDefault = 0;

    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        db = new DatabaseHandler(getApplicationContext());


        EditText setting = (EditText) findViewById(R.id.calorie_limit);
        caloricLimit = db.getSetting(calorieKey);
        if (caloricLimit == -1) {
            caloricLimit = caloricDefault;
            db.addSetting(calorieKey, caloricLimit);
        }
        setting.setText(Integer.toString(caloricLimit));

        EditText settingProt = (EditText) findViewById(R.id.protein_limit);
        proteinLimit = db.getSetting(proteinKey);
        if (proteinLimit == -1) {
            proteinLimit = proteinDefault;
            db.addSetting(proteinKey, proteinLimit);
        }
        settingProt.setText(Integer.toString(proteinLimit));

        EditText settingCarb = (EditText) findViewById(R.id.carb_limit);
        carbLimit = db.getSetting(carbKey);
        if (carbLimit == -1) {
            carbLimit = carbDefault;
            db.addSetting(carbKey, carbLimit);
        }
        settingCarb.setText(Integer.toString(carbLimit));

        EditText settingfat = (EditText) findViewById(R.id.fat_limit);
        fatLimit = db.getSetting(fatKey);
        if (fatLimit == -1) {
            fatLimit = fatDefault;
            db.addSetting(fatKey, fatLimit);
        }
        settingfat.setText(Integer.toString(fatLimit));
    }

    public void saveSettings(View v) {
        /*
        updates each of the given goal settings based on value in edittext
         */
        EditText setting = (EditText) findViewById(R.id.calorie_limit);
        if (setting.length() != 0) caloricLimit = Integer.parseInt(setting.getText().toString());
        setting.setText(Integer.toString(caloricLimit));
        db.updateSettings(calorieKey, caloricLimit);

        EditText settingProt = (EditText) findViewById(R.id.protein_limit);
        if (settingProt.length() != 0)
            proteinLimit = Integer.parseInt(settingProt.getText().toString());
        settingProt.setText(Integer.toString(proteinLimit));
        db.updateSettings(proteinKey, proteinLimit);

        EditText settingfat = (EditText) findViewById(R.id.fat_limit);
        if (settingfat.length() != 0) fatLimit = Integer.parseInt(settingfat.getText().toString());
        settingfat.setText(Integer.toString(fatLimit));
        db.updateSettings(fatKey, fatLimit);

        EditText settingCarb = (EditText) findViewById(R.id.carb_limit);
        if (settingCarb.length() != 0)
            carbLimit = Integer.parseInt(settingCarb.getText().toString());
        settingCarb.setText(Integer.toString(carbLimit));
        db.updateSettings(carbKey, carbLimit);

    }

    public void exportData(View v) {
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Backup/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
            SQLiteToExcel sqLiteToExcel = new SQLiteToExcel(getApplicationContext(), db.DATABASE_NAME, directory_path);
            sqLiteToExcel.exportAllTables("tables.xls", new SQLiteToExcel.ExportListener() {
                @Override
                public void onStart() {
                    Toast.makeText(SettingsActivity.this, "Export Started", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCompleted(String filePath) {
                    Toast.makeText(SettingsActivity.this, "Export Completed", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(SettingsActivity.this, "Export Error", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
