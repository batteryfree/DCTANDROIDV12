package com.batteryfree.dctfesukrv12;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

public class form8 extends AppCompatActivity {
    public JSONObject jsonOutput;
    public String URL_1C;
    public EditText f8_editText1;
    public EditText f8_editText2;
    private ProgressDialog progressDialog;
    private boolean isRequestCancelled = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form8);
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
            jsonOutput.put("form", 8);
            jsonOutput.put("nextForm", 8);
        } catch (Exception e){}

        f8_editText1 = findViewById(R.id.f8_editText1);
        f8_editText2 = findViewById(R.id.f8_editText2);
        f8_editText1.requestFocus();
//        f8_editText1.setInputType(InputType.TYPE_NULL);
//        f8_editText2.setInputType(InputType.TYPE_NULL);

        f8_editText2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            private boolean isRequestInProgress = false; // Флаг для предотвращения повторного запроса

            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (keyEvent == null || keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (isRequestInProgress) {
                        return true; // Если запрос уже отправляется, игнорируем повторный вызов
                    }
                    isRequestInProgress = true;
                    isRequestCancelled = false; // Сбрасываем флаг отмены
                    showProgressDialogWithCancelOption(); // Показываем прогресс-диалог
                    sendPostRequest(() -> isRequestInProgress = false); // Сбрасываем флаг после выполнения
                    return true;
                }
                return false;
            }
        });

        f8_editText1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    f8_editText1.selectAll();
                }
            }
        });

        f8_editText2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    f8_editText2.selectAll();
                }
            }
        });

        f8_editText1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && !v.hasFocus()) {
                    f8_editText1.requestFocus();
                    f8_editText1.selectAll();
                    return true;
                }
                return false;
            }
        });

        f8_editText2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && !v.hasFocus()) {
                    f8_editText2.requestFocus();
                    f8_editText2.selectAll();
                    return true;
                }
                return false;
            }
        });

    }

    public void startMenu1(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void showProgressDialogWithCancelOption() {
        runOnUiThread(() -> {
            progressDialog = new ProgressDialog(form8.this);
            progressDialog.setMessage("Відправка данних...");
            progressDialog.setCancelable(false);
            progressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "Відміна", (dialog, which) -> cancelRequest());
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
        showInfo("Запрос біл відмінен користувачем.");
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

                jsonOutput.put("p1", f8_editText1.getText().toString().trim());
                jsonOutput.put("p2", f8_editText2.getText().toString().trim());
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
                    int nextForm = jsonResponse.optInt("nextForm");
                    // обрабатываем ответ
                    runOnUiThread(() -> {
                        dismissLoader();
                        if (msg.isEmpty()) {
                            if (nextForm != 8) {
                                try {
                                    jsonOutput.put("nextForm", nextForm);
                                } catch (Exception e) {}
                                changeForm();
                            }
                        } else {
                            showInfo(msg);
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

    public void changeForm() {
        int nextForm = jsonOutput.optInt("nextForm");
        Intent intent = new Intent();

        if (nextForm == 4) {
            intent = new Intent(this, form4.class);
        }

        else if (nextForm == 6) {
            try {
                jsonOutput.put("p8", jsonOutput.optString("p4"));
                jsonOutput.put("d1", jsonOutput.optString("p1"));
                jsonOutput.put("p4", "");
                jsonOutput.put("p1", "");
            } catch (Exception e) {}
            intent = new Intent(this, form6.class);
        }
        else if (nextForm == 7) {
            intent = new Intent(this, form7.class);
        } else if (nextForm == 8) {
            intent = new Intent(this, form8.class);
        } else if (nextForm == 9) {
            intent = new Intent(this, form9.class);
        } else if (nextForm == 10) {
            intent = new Intent(this, form10.class);
        } else if (nextForm == 11) {
            try {
                jsonOutput.put("d", jsonOutput.optString("p1"));
                jsonOutput.put("d1", jsonOutput.optString("d1"));
                jsonOutput.put("b",  jsonOutput.optString("p2"));
            } catch (Exception e) {}
            intent = new Intent(this, form11.class);
        } else if (nextForm == 12) {
            intent = new Intent(this, form12.class);
        } else if (nextForm == 13) {
            intent = new Intent(this, form13.class);
        } else if (nextForm == 2) {
            intent = new Intent(this, form2.class);
        }

        intent.putExtra("URL", URL_1C);
        intent.putExtra("jsonOutput", jsonOutput.toString());
        startActivity(intent);
    }
}