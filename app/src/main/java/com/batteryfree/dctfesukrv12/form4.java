package com.batteryfree.dctfesukrv12;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
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

public class form4 extends AppCompatActivity {
    public JSONObject jsonOutput;
    public String URL_1C;

    public EditText f4_editText1;
    public EditText f4_editText2;
    public EditText f4_editText3;
    public EditText f4_editText4;
    public TextView f4_l2_1;
    public TextView f4_l3_1;
    private ProgressDialog progressDialog;
    private boolean isRequestCancelled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form4);
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
            jsonOutput.put("form", 4);
            jsonOutput.put("nextForm", 4);
        } catch (Exception e){}

        f4_editText1 = findViewById(R.id.f4_editText1);
        f4_editText2 = findViewById(R.id.f4_editText2);
        f4_editText3 = findViewById(R.id.f4_editText3);
        f4_editText4 = findViewById(R.id.f4_editText4);
//        f4_editText1.setInputType(InputType.TYPE_NULL);
//        f4_editText2.setInputType(InputType.TYPE_NULL);
//        f4_editText3.setInputType(InputType.TYPE_NULL);
//        f4_editText1.setInputType(InputType.TYPE_NULL);

        f4_editText1.requestFocus();
        f4_l2_1 = findViewById(R.id.f4_l2_1);
        f4_l3_1 = findViewById(R.id.f4_l3_1);

        f4_editText1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    f4_editText1.selectAll();
                }
            }
        });

        f4_editText2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    f4_editText2.selectAll();
                }
            }
        });

        f4_editText3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    f4_editText3.selectAll();
                }
            }
        });

        f4_editText4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    f4_editText4.selectAll();
                }
            }
        });

        f4_editText1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            private boolean isRequestInProgress = false; // Флаг для предотвращения повторного запроса

            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (keyEvent == null || (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (isRequestInProgress) {
                        return true; // Предотвращаем повторный запрос
                    }

                    try {
                        jsonOutput.put("operation", "Query");
                    } catch (JSONException e) {
                        e.printStackTrace();
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

        f4_editText4.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            private boolean isRequestInProgress = false; // Флаг для предотвращения повторного запроса

            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (keyEvent == null || (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (isRequestInProgress) {
                        return true; // Предотвращаем повторный запрос
                    }

                    try {
                        jsonOutput.put("operation", "Update");
                    } catch (JSONException e) {
                        e.printStackTrace();
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

    }

    private void showProgressDialogWithCancelOption() {
        runOnUiThread(() -> {
            progressDialog = new ProgressDialog(form4.this);
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
        showInfo("Запрос був відмінен користувачем.");
    }

    public void startMenu1(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
            progressDialog = null;
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

                if (jsonOutput.optString("operation") == "Query") {
                    jsonOutput.put("p1", f4_editText1.getText().toString().trim());
                    jsonOutput.put("p2", "");
                    jsonOutput.put("p3", "");
                    jsonOutput.put("p4", "");
                    jsonOutput.put("p5", "");
                    jsonOutput.put("p6", "");
                } else {
                    jsonOutput.put("p1", f4_editText1.getText().toString().trim());
                    jsonOutput.put("p2", f4_l2_1.getText().toString().trim());
                    jsonOutput.put("p3", f4_l3_1.getText().toString().trim());
                    jsonOutput.put("p4", f4_editText2.getText().toString().trim());
                    jsonOutput.put("p5", f4_editText3.getText().toString().trim());
                    jsonOutput.put("p6", f4_editText4.getText().toString().trim());
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
                    String result = jsonResponse.toString(4);
                    int nextForm = jsonResponse.optInt("nextForm");

                    // обрабатываем ответ
                    runOnUiThread(() -> {
                        dismissLoader();
                        if (msg.isEmpty()) {

                            if (nextForm != 4) {
                                try {
                                    jsonOutput.put("nextForm", nextForm);
                                } catch (Exception e) {}
                                changeForm();

                            } else {
                                f4_editText1.setText(jsonResponse.optString("p1"));
                                f4_editText2.setText(jsonResponse.optString("p4"));
                                f4_editText3.setText(jsonResponse.optString("p5"));
                                f4_editText4.setText(jsonResponse.optString("p6"));
                                f4_l2_1.setText(jsonResponse.optString("p2"));
                                f4_l3_1.setText(jsonResponse.optString("p3"));
                                if (jsonOutput.optString("operation") == "Query") {
                                    f4_editText2.requestFocus();
                                    f4_editText2.selectAll();
                                } else {
                                    f4_editText1.requestFocus();
                                    f4_editText1.selectAll();
                                }
                            }
                        } else if (nextForm != 4) {
                            showInfo(msg);
                            changeForm();
                        } else {
                            showInfo(msg);

                            f4_editText1.setText("");
                            f4_editText2.setText("");
                            f4_editText3.setText("");
                            f4_editText4.setText("");
                            f4_l2_1.setText("");
                            f4_l3_1.setText("");

                            f4_editText1.requestFocus();
                            f4_editText1.selectAll();
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

        try {
            jsonOutput.put("p8", jsonOutput.optString("p4"));
            jsonOutput.put("d1", jsonOutput.optString("p1"));
            jsonOutput.put("p4", "");
            jsonOutput.put("p1", "");
        } catch (Exception e) {}

        if (nextForm == 4) {
            intent = new Intent(this, form4.class);
        }
        else if (nextForm == 6) {
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
        }
        else if (nextForm == 11) {
            intent = new Intent(this, form11.class);
        }
        else if (nextForm == 12) {
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