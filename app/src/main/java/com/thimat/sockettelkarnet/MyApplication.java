package com.thimat.sockettelkarnet;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.getyourmap.glmap.GLMapManager;
import com.thimat.sockettelkarnet.geofencing.ReminderRepository;

import org.jetbrains.annotations.NotNull;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import kotlin.jvm.internal.Intrinsics;


public class MyApplication extends Application {
    private static Context context;
    private ReminderRepository repository;
    @Override
    public void onCreate() {
        super.onCreate();

        if (!GLMapManager.Initialize(this, this.getString(R.string.api_key), null)) { // Uncomment and insert your API key into api_key in res/values/strings.xml
            // Error caching resources. Check free space for world database (~25MB)
        }
        MyApplication.context = this;
        Realm.init(getApplicationContext());
        RealmConfiguration mConfig = new RealmConfiguration.Builder()
                .name("Telcarnet.realm")
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(mConfig);
        this.repository = new ReminderRepository((Context)this);
    }
    @NotNull
    public final ReminderRepository getRepository() {
        ReminderRepository var10000 = this.repository;
        if (this.repository == null) {
            Intrinsics.throwUninitializedPropertyAccessException("repository");
        }

        return var10000;
    }
    public static Context getContext() {
        return context;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
