/*
 * Copyright 2018 Idealnaya rabota LLC
 * Licensed under Multy.io license.
 * See LICENSE for details
 */

package io.multy.storage;

import android.content.Context;
import android.util.Log;

import java.io.File;

import io.multy.Multy;
import io.realm.Realm;

public class RealmManager {

    private final static String TAG = RealmManager.class.getSimpleName();

    private static Realm realm;

    public static Realm open() {
        try {
            realm = Realm.getInstance(Multy.getRealmConfiguration());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return realm;
    }

    public static void close() {
        if (realm != null && realm.isClosed()) {
            try {
                realm.close();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                open(); //since we can't call realm from different threads, we need to reopen and close realm on the same thread
                realm.close();
            }
        }
    }

    public static AssetsDao getAssetsDao() {
//        isRealmAvailable();
        return new AssetsDao(isRealmNeedOpen() ? open() : realm);
    }

    public static SettingsDao getSettingsDao() {
//        isRealmAvailable();
        return new SettingsDao(isRealmNeedOpen() ? open() : realm);
    }

    public static void clear() {
//        if (realm == null || realm.isClosed()) {
//            open();
//        }
//
//        if (realm != null) {
//            realm.executeTransaction(realm -> realm.deleteAll());
//        }
    }

    public static boolean isRealmNeedOpen() {
        return realm == null || realm.isClosed();
    }

    private static void isRealmAvailable() {
        try {
            if (realm == null || realm.isClosed()) {
                Log.e(TAG, "ERROR DB IS CLOSED OR NULL");
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
            open();
        }
    }

    public static void removeDatabase(Context context) {
        for (File file : context.getFilesDir().listFiles()) {
            if (file.isHidden()) {
                continue;
            }
            if (file.getAbsolutePath().contains("realm")) {
                if (file.isDirectory()) {
                    removeFilesFromDirectory(file);
                }
                file.delete();
            }
        }
    }

    private static void removeFilesFromDirectory(File file) {
        for (File subFile : file.listFiles()) {
            if (file.isHidden()) {
                continue;
            }
            if (subFile.isDirectory()) {
                removeFilesFromDirectory(file);
            }
            subFile.delete();
        }
    }
}
