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
        checker.check(new UpdateAvailableCallback() {
            @Override
            public void onUpdateAvailableListener(boolean updateAvailable) {
                // your code if found update
            }

            @Override
            public void onCheckFailureListener() {
                // your code to handle failure
            }
        });
    }
}