package com.batteryfree.dctfesukrv12;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
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
    public Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form9);
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
            jsonOutput.put("form", 9);
            jsonOutput.put("nextForm", 9);
        } catch (Exception e){}

        f9_editText1 = findViewById(R.id.f9_editText1);
        f9_editText1.requestFocus();
        f9_l2_1 = findViewById(R.id.f9_l2_1);
        f9_l3_1 = findViewById(R.id.f9_l3_1);
        f9_l4_1 = findViewById(R.id.f9_l4_1);
        submit = (Button)findViewById(R.id.submit);

//        f9_editText1.setInputType(InputType.TYPE_NULL);

        f9_editText1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

        f9_editText1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    f9_editText1.selectAll();
                }
            }
        });


    }

    private void showProgressDialogWithCancelOption() {
        runOnUiThread(() -> {
            progressDialog = new ProgressDialog(form9.this);
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

    public void startSubmit(View v) {
        AtomicBoolean isRequestInProgress = new AtomicBoolean(false);

        try {
            jsonOutput.put("operation", "Update");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        isRequestInProgress.set(true);
        isRequestCancelled = false;
        showProgressDialogWithCancelOption();
        sendPostRequest(() -> isRequestInProgress.set(false));
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

                jsonOutput.put("p1", f9_editText1.getText().toString().trim());

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

                            if (nextForm != 9) {
                                try {
                                    jsonOutput.put("nextForm", nextForm);
                                } catch (Exception e) {}
                                changeForm();

                            } else {
                                f9_editText1.setText(jsonResponse.optString("p1"));
                                f9_l2_1.setText(jsonResponse.optString("p2"));
                                f9_l3_1.setText(jsonResponse.optString("p3"));
                                f9_l4_1.setText(jsonResponse.optString("p4"));
                                submit.requestFocus();
                            }
                        } else if (nextForm != 9) {
                            showInfo(msg);
                            changeForm();
                        } else {
                            showInfo(msg);
                            f9_editText1.setText("");
                            f9_l2_1.setText("");
                            f9_l3_1.setText("");
                            f9_l4_1.setText("");
                            f9_editText1.requestFocus();
                            f9_editText1.selectAll();
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
        } else if (nextForm == 6) {
            intent = new Intent(this, form6.class);
        } else if (nextForm == 7) {
            intent = new Intent(this, form7.class);
        } else if (nextForm == 8) {
            intent = new Intent(this, form8.class);
        } else if (nextForm == 9) {
            intent = new Intent(this, form9.class);
        } else if (nextForm == 10) {
            intent = new Intent(this, form10.class);
        } else if (nextForm == 11) {
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