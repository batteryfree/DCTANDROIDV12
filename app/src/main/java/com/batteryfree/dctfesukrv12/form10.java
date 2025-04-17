package com.batteryfree.dctfesukrv12;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class form10 extends AppCompatActivity {
    public JSONObject jsonOutput;
    public String URL_1C;
    public EditText f10_editText1;
    public EditText f10_editText2;
    public EditText f10_editText3;
    private ProgressDialog progressDialog;
    private boolean isRequestCancelled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form10);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        URL_1C =  intent.getStringExtra("URL");
        String _jsonOutput = intent.getStringExtra("jsonOutput");

        try {
            jsonOutput = new JSONObject(_jsonOutput);
            jsonOutput.put("form", 10);
            jsonOutput.put("nextForm", 10);
        } catch (Exception e){}

        f10_editText1 = findViewById(R.id.f10_editText1);
        f10_editText2 = findViewById(R.id.f10_editText2);
        f10_editText3 = findViewById(R.id.f10_editText3);
        f10_editText1.setShowSoftInputOnFocus(false);
        f10_editText2.setShowSoftInputOnFocus(false);
        f10_editText3.setShowSoftInputOnFocus(false);

        f10_editText1.requestFocus();

//        f10_editText1.setInputType(InputType.TYPE_NULL);
//        f10_editText2.setInputType(InputType.TYPE_NULL);
//        f10_editText3.setInputType(InputType.TYPE_NULL);


//Вот Это убрано перенесено на кнопку (НА САБМИТ)
//        f10_editText3.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            private boolean isRequestInProgress = false; // Флаг для предотвращения повторного запроса
//
//            @Override
//            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
//                if (keyEvent == null || keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
//                    if (isRequestInProgress) {
//                        return true; // Если запрос уже отправляется, игнорируем повторный вызов
//                    }
//                    isRequestInProgress = true;
//                    isRequestCancelled = false; // Сбрасываем флаг отмены
//                    showProgressDialogWithCancelOption(); // Показываем прогресс-диалог
//                    sendPostRequest(() -> isRequestInProgress = false); // Сбрасываем флаг после выполнения
//                    return true;
//                }
//                return false;
//            }
//        });

        f10_editText1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    f10_editText1.selectAll();
                }
            }
        });

        f10_editText2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    f10_editText2.selectAll();
                }
            }
        });

        f10_editText3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    f10_editText3.selectAll();
                }
            }
        });

        f10_editText1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && !v.hasFocus()) {
                    f10_editText1.requestFocus();
                    f10_editText1.selectAll();
                    v.performClick();
                    return true;
                }
                return false;
            }
        });

        f10_editText2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && !v.hasFocus()) {
                    f10_editText2.requestFocus();
                    f10_editText2.selectAll();
                    v.performClick();
                    return true;
                }
                return false;
            }
        });

        f10_editText3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && !v.hasFocus()) {
                    f10_editText3.requestFocus();
                    f10_editText3.selectAll();
                    v.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    public boolean Submit(View v) {
        AtomicBoolean isRequestInProgress = new AtomicBoolean(false); // Флаг для предотвращения повторного запроса
        if (isRequestInProgress.get()) {
            return true; // Если запрос уже отправляется, игнорируем повторный вызов
        }
        isRequestInProgress.set(true);
        isRequestCancelled = false; // Сбрасываем флаг отмены
        showProgressDialogWithCancelOption(); // Показываем прогресс-диалог
        sendPostRequest(() -> isRequestInProgress.set(false)); // Сбрасываем флаг после выполнения
        return true;
    }

    public void startMenu1(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void showProgressDialogWithCancelOption() {
        runOnUiThread(() -> {
            progressDialog = new ProgressDialog(form10.this);
            progressDialog.setMessage("Відправка данних...");
            progressDialog.setCancelable(false);
            progressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "Отменить", (dialog, which) -> cancelRequest());
            progressDialog.show();

            // Активируем кнопку "Отменить" через 5 секунд
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

    public void showInfo(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Сповіщення")
                .setMessage(text)
                .setPositiveButton("Ok",(dialogInterface, i)-> { })
                .setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void dismissLoader() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
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

                jsonOutput.put("p1", f10_editText1.getText().toString().trim());
                jsonOutput.put("p2", f10_editText2.getText().toString().trim());
                jsonOutput.put("p3", f10_editText3.getText().toString().trim());
                jsonOutput.put("operation", "Update");

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
                    String msg = jsonResponse.optString("msg");
                    String result = jsonResponse.toString(4);
                    // обрабатываем ответ
                    runOnUiThread(() -> {
                        dismissLoader();
                        if (msg.isEmpty()) {
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            showInfo(msg);
                        }
                        onComplete.run();
                    });
                } else {
                    runOnUiThread(() -> {
                        dismissLoader();
                        showInfo("Помилка: код відповіді " + code);
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
}