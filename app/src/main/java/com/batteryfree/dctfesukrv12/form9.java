package com.batteryfree.dctfesukrv12;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class form9 extends AppCompatActivity {
    public JSONObject jsonOutput;
    public String URL_1C;
    public EditText f9_editText1;
    public TextView f9_l2_1;
    public TextView f9_l3_1;
    public TextView f9_l4_1;
    private ProgressDialog progressDialog;
    private boolean isRequestCancelled = false;
    private final AtomicBoolean isRequestInProgress = new AtomicBoolean(false);
    private ScrollView scrollView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form9);
        scrollView = findViewById(R.id.scrollView);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        URL_1C = intent.getStringExtra("URL");
        String _jsonOutput = intent.getStringExtra("jsonOutput");

        try {
            jsonOutput = new JSONObject(_jsonOutput);
            jsonOutput.put("form", 9);
            jsonOutput.put("nextForm", 9);
        } catch (Exception e) {
            showInfo("Помилка ініціалізації форми: " + e.getMessage());
        }

        f9_editText1 = findViewById(R.id.f9_editText1);
        f9_l2_1 = findViewById(R.id.f9_l2_1);
        f9_l3_1 = findViewById(R.id.f9_l3_1);
        f9_l4_1 = findViewById(R.id.f9_l4_1);

        f9_editText1.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                scrollView.post(() -> scrollView.scrollTo(0, 0));
                f9_editText1.selectAll();
            }
        });

        f9_editText1.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP && !v.hasFocus()) {
                f9_editText1.requestFocus();
                f9_editText1.selectAll();
                v.performClick();
                return true;
            }
            return false;
        });

        f9_editText1.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (keyEvent == null || (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                if (isRequestInProgress.get()) return true;

                try {
                    jsonOutput.put("operation", "Query");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                isRequestInProgress.set(true);
                isRequestCancelled = false;
                showProgressDialogWithCancelOption();
                sendPostRequest(() -> isRequestInProgress.set(false));
                return true;
            }
            return false;
        });
    }

    private void sendPostRequest(Runnable onComplete) {
        new Thread(() -> {
            try {
                URL url = new URL(URL_1C);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);

                jsonOutput.put("p1", f9_editText1.getText().toString().trim());

                if (!"Query".equals(jsonOutput.optString("operation"))) {
                    jsonOutput.put("p2", f9_l2_1.getText().toString().trim());
                    jsonOutput.put("p3", f9_l3_1.getText().toString().trim());
                    jsonOutput.put("p4", f9_l4_1.getText().toString().trim());
                }

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonOutput.toString().getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int code = connection.getResponseCode();

                if (code == HttpURLConnection.HTTP_OK) {
                    Scanner scanner = new Scanner(connection.getInputStream());
                    StringBuilder response = new StringBuilder();
                    while (scanner.hasNext()) {
                        response.append(scanner.nextLine());
                    }
                    scanner.close();

                    JSONObject jsonResponse = new JSONObject(response.toString());
                    String msg = jsonResponse.optString("msg", "");
                    int nextForm = jsonResponse.optInt("nextForm");

                    runOnUiThread(() -> {
                        dismissLoader();
                        if (msg.isEmpty()) {
                            if ("Query".equals(jsonOutput.optString("operation"))) {
                                f9_editText1.setText(jsonResponse.optString("p1"));
                                f9_l2_1.setText(jsonResponse.optString("p2"));
                                f9_l3_1.setText(jsonResponse.optString("p3"));
                                f9_l4_1.setText(jsonResponse.optString("p4"));

                                findViewById(R.id.submit).requestFocus();
                            } else {
                                clearForm();
                            }
                        } else {
                            showInfo(msg);
                            clearForm();
                        }
                        onComplete.run();
                    });
                } else {
                    runOnUiThread(() -> {
                        dismissLoader();
                        showInfo("Помилка код відповіді " + code);
                        onComplete.run();
                    });
                }
                connection.disconnect();
            } catch (Exception e) {
                runOnUiThread(() -> {
                    dismissLoader();
                    showInfo("Помилка: " + e.getMessage());
                    onComplete.run();
                });
            }
        }).start();
    }

    public boolean Submit(View v) {
        if (isRequestInProgress.get()) {
            return true;
        }

        try {
            jsonOutput.put("operation", "Update");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        isRequestInProgress.set(true);
        isRequestCancelled = false;
        showProgressDialogWithCancelOption();
        sendPostRequest(() -> isRequestInProgress.set(false));
        return true;
    }

    private void showProgressDialogWithCancelOption() {
        runOnUiThread(() -> {
            progressDialog = new ProgressDialog(form9.this);
            progressDialog.setMessage("Відправка данних...");
            progressDialog.setCancelable(false);
            progressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "Відміна", (dialog, which) -> cancelRequest());
            progressDialog.show();

            new Handler().postDelayed(() -> {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.getButton(ProgressDialog.BUTTON_NEGATIVE).setEnabled(true);
                }
            }, 5000);
        });
    }

    private void cancelRequest() {
        isRequestCancelled = true;
        dismissLoader();
        showInfo("Запит було скасовано користувачем.");
    }

    public void startMenu1(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void showInfo(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Сповіщення")
                .setMessage(text)
                .setPositiveButton("Ok", (dialogInterface, i) -> {})
                .setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void dismissLoader() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void clearForm() {
        f9_editText1.setText("");
        f9_l2_1.setText("");
        f9_l3_1.setText("");
        f9_l4_1.setText("");
        f9_editText1.requestFocus();
        f9_editText1.selectAll();
    }

    @Override
    public void onBackPressed() {
        clearForm();
        super.onBackPressed();
    }
}
