package com.abanoub.versionchecker;

public interface UpdateAvailableCallback {
    void onUpdateAvailableListener(boolean updateAvailable);
    void onCheckFailureListener();
}
