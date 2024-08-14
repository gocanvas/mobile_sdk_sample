package com.gocanvas.sdksample;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.gocanvas.sdksample.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    String inputJSON = "";
    String submissionJson = "";

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
        /* SDK showForm call goes here */
    }

    private void handleResult(int resultCode, Intent data) {
        /* SDK handle result example goes here */
    }

    private void showResult(int resultCode, Intent data) {
        /* SDK show result on Ui goes here */
    }
}
