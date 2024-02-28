package com.abanoub.androidversionchecker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.abanoub.versionchecker.UpdateAvailableCallback;
import com.abanoub.versionchecker.VersionChecker;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VersionChecker checker = VersionChecker.getInstance(this);
        checker.check(updateAvailable -> {
            // your code if found update
        });
    }
}