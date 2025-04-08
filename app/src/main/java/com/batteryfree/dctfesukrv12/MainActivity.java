package com.batteryfree.dctfesukrv12;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    public String URL_1C = "http://192.168.23.206:8080/TSD/hs/terminal/api";
    public String serialNumberDevice;
    public JSONObject jsonOutput = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        serialNumberDevice = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        creteJSONData();
    }

    public void startForm12(View v) {
        Intent intent = new Intent(this, form12.class);
        intent.putExtra("URL", URL_1C);
        intent.putExtra("jsonOutput", jsonOutput.toString());
        startActivity(intent);
    }

    public void startForm7(View v) {
        Intent intent = new Intent(this, form7.class);
        intent.putExtra("URL", URL_1C);
        intent.putExtra("jsonOutput", jsonOutput.toString());
        startActivity(intent);
    }

    public void startMenu2(View v) {
        Intent intent = new Intent(this, menu2.class);
        intent.putExtra("URL", URL_1C);
        intent.putExtra("jsonOutput", jsonOutput.toString());
        startActivity(intent);
    }

    public void creteJSONData() {
        try {
            jsonOutput.put("numTSD", serialNumberDevice);
            jsonOutput.put("operation", "Query");
            jsonOutput.put("base", 1);
            jsonOutput.put("form", 0);
            jsonOutput.put("p1", "");
            jsonOutput.put("p2", "");
            jsonOutput.put("p3", "");
            jsonOutput.put("p4", "");
            jsonOutput.put("p5", "");
            jsonOutput.put("p6", "");
            jsonOutput.put("p7", "");
            jsonOutput.put("p8", "");
            jsonOutput.put("d", "");
            jsonOutput.put("b", "");
            jsonOutput.put("d1", "");
            jsonOutput.put("nextForm", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        // Блокируем кнопку "Назад"
        super.onBackPressed();
    }
}
