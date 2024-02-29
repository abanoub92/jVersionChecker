package com.abanoub.versionchecker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;

public class VersionChecker {

    @SuppressLint("StaticFieldLeak")
    private static VersionChecker instance = null;
    private final Activity activity;
    private int currentVersion = 0;
    private String packageName = "";

    private VersionChecker(Activity activity){
        this.activity = activity;

        try {
            PackageInfo packageInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            currentVersion = packageInfo.versionCode;
            packageName = packageInfo.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized VersionChecker getInstance(Activity activity){
        if (instance == null)
            instance = new VersionChecker(activity);

        return instance;
    }

    @SuppressLint("CheckResult")
    public void check(UpdateAvailableCallback callback){
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(activity);
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(task -> {
            int s = task.availableVersionCode();

            if (s == 0) {
                callback.onUpdateAvailableListener(false);
                return;
            }

            if (s > currentVersion) {
                callback.onUpdateAvailableListener(true);
                if (!activity.isFinishing()) //to avoid crashing when activity is not visible
                    showUpdate().create().show();
            }
            else
                callback.onUpdateAvailableListener(false);

        });

        appUpdateManager.getAppUpdateInfo().addOnFailureListener(e -> {
            callback.onCheckFailureListener();
        });

        appUpdateManager.getAppUpdateInfo().addOnCanceledListener(() -> {
            callback.onUpdateAvailableListener(false);
        });
    }

    private AlertDialog.Builder showUpdate(){
        return new AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.version_checker_title))
                .setMessage(activity.getString(R.string.version_checker_desc))
                .setCancelable(false)
                .setPositiveButton(activity.getString(R.string.version_checker_ok), (dialog, which) -> {
                    try {
                        activity.startActivity(
                                new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                    }catch (ActivityNotFoundException ex) {
                        activity.startActivity(
                                new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
                        ex.printStackTrace();
                    }

                });

    }

}
