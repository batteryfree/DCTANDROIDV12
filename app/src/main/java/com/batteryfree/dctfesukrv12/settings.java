package com.batteryfree.dctfesukrv12;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class settings extends AppCompatActivity {
    private EditText editServerUrl;
    public static final String PREFS_NAME = "AppPrefs";
    public static final String KEY_SERVER_URL = "server_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editServerUrl = findViewById(R.id.editServerUrl);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedUrl = prefs.getString(KEY_SERVER_URL, "http://192.168.23.206:8080/TSD/hs/terminal/api");
        editServerUrl.setText(savedUrl);
    }

    public void onSaveClicked(View view) {
        String newUrl = editServerUrl.getText().toString();
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_SERVER_URL, newUrl);
        editor.apply();
        finish();
    }

    public void startMenu1(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}


