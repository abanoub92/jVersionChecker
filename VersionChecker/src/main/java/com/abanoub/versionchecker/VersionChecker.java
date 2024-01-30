package com.abanoub.versionchecker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class VersionChecker {

    @SuppressLint("StaticFieldLeak")
    private static VersionChecker instance = null;
    private final Activity activity;
    private String currentVersion = "";
    private String packageName = "";

    private VersionChecker(Activity activity){
        this.activity = activity;

        try {
            PackageInfo packageInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            currentVersion = packageInfo.versionName;
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
    public void check(){
        Observable.fromCallable(() -> {
            try {
                Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id="+packageName).get();
                return doc.getElementsByAttributeValue("itemprop","softwareVersion");
            }catch (HttpStatusException e){
                return new Elements();
            }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> { return; })
                .subscribe(s -> {
                    if (s.text().isEmpty())
                        return;

                    if (!s.text().equals(currentVersion))
                        if (!activity.isFinishing()) //to avoid crashing when activiy is not visible
                            showUpdate().create().show();
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
                    }catch (android.content.ActivityNotFoundException ignored) {
                        activity.startActivity(
                                new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
                        ignored.printStackTrace();
                    }

                });

    }

}
