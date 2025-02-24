package com.gocanvas.sdksample;

import static com.gocanvas.sdk.api.CanvasSdkKey.ERROR_MESSAGE_KEY;
import static com.gocanvas.sdk.api.CanvasSdkKey.ERROR_NUMBER_KEY;
import static com.gocanvas.sdk.api.CanvasSdkKey.RESPONSE_KEY;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.gocanvas.sdk.api.CanvasSdk;
import com.gocanvas.sdk.api.CanvasSdkKey;
import com.gocanvas.sdksample.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    String inputJSON = "";
    String submissionJson = "";

    ActivityResultLauncher<Intent> formLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                handleResult(result.getResultCode(), result.getData());
                showResult(result.getResultCode(), result.getData());
            });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(this.getLayoutInflater());
        setContentView(binding.getRoot());

        binding.inputJson.setText(inputJSON);
        binding.submissionJson.setText(submissionJson);

        binding.showForm.setOnClickListener(view -> {
            inputJSON = binding.inputJson.getText().toString();
            showForm(inputJSON);
        });
    }

    @Override
    protected void onPause() {
        binding.inputJson.setText("");
        binding.submissionJson.setText("");
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.inputJson.setText(inputJSON);
        binding.submissionJson.setText(submissionJson);
    }

    private void showForm(String formJson) {
        CanvasSdk.INSTANCE.showForm(formJson, this, formLauncher);
    }

    private void handleResult(int resultCode, Intent data) {
        switch (resultCode) {
            case Activity.RESULT_OK:
                // form completed successfully
                if (data != null && data.hasExtra(CanvasSdkKey.RESPONSE_KEY)) {
                    Log.i("ActivityResult", "form has response: " + data.getExtras().get(CanvasSdkKey.RESPONSE_KEY));
                    Log.i("ActivityResult", "form response: " + CanvasSdk.INSTANCE.getResponse());
                }
                break;
            case Activity.RESULT_CANCELED:
                // user canceled or an error occurred
                if (data != null && data.hasExtra(CanvasSdkKey.ERROR_NUMBER_KEY) && data.hasExtra(CanvasSdkKey.ERROR_MESSAGE_KEY)) {
                    Log.i("ActivityResult", "error code: " + data.getExtras().get(CanvasSdkKey.ERROR_NUMBER_KEY));
                    Log.i("ActivityResult", "error message: " + data.getExtras().get(CanvasSdkKey.ERROR_MESSAGE_KEY));
                }
                break;
        }
    }

    private void showResult(int resultCode, Intent intent) {
        String errorCode = "None";
        String errorMessage = "None";
        String responseText = "";

        if ((intent != null) && (intent.getExtras() != null)) {
            if (intent.getExtras().containsKey(ERROR_NUMBER_KEY) && intent.getExtras().getString(ERROR_NUMBER_KEY).length() > 0) {
                errorCode = intent.getExtras().getString(ERROR_NUMBER_KEY);
            }
            if (intent.getExtras().containsKey(ERROR_MESSAGE_KEY) && intent.getExtras().getString(ERROR_MESSAGE_KEY).length() > 0) {
                errorMessage = intent.getExtras().getString(ERROR_MESSAGE_KEY);
            }
            if (intent.getExtras().containsKey(RESPONSE_KEY)) {
                responseText = CanvasSdk.INSTANCE.getResponse();
            }
        }

        submissionJson = responseText;
        binding.submissionJson.setText(submissionJson);

        String messageTitle = "Result";
        String resultCodeString = String.format("Result Code: \n%d\n", resultCode);
        String errorCodeString = String.format("Error Number: \n%s\n", errorCode);
        String errorMessageString = String.format("Error Message: \n%s\n", errorMessage);
        String messageText = String.format("%s\n%s\n%s", resultCodeString, errorCodeString, errorMessageString);
        showMessage(this, messageTitle, messageText, "OK");
    }

    private void showMessage(Context context, String title, String message, String buttonText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setTitle(title)
                .setCancelable(false)
                .setNeutralButton(buttonText, null);
        AlertDialog alert = builder.create();
        alert.show();
    }
}
