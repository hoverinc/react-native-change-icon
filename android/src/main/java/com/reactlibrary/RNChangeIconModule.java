
package com.reactlibrary;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.ComponentName;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;

public class RNChangeIconModule extends ReactContextBaseJavaModule {

    private final String packageName;
    private String componentClass = null;

    RNChangeIconModule(ReactApplicationContext reactContext, String packageName) {
        super(reactContext);
        this.packageName = packageName;
    }

    @Override
    public String getName() {
        return "RNChangeIcon";
    }

    @ReactMethod
    public void changeIcon(String enableIcon, Promise promise) {
        final Activity activity = getCurrentActivity();
        String mainActivity = this.packageName + ".MainActivity";

        if (this.componentClass == null) this.componentClass = activity.getComponentName().getClassName();

        // reset to main activity if enableIcon is null or undefined
        if (activity == null || enableIcon == null || enableIcon.isEmpty()) {
            promise.resolve(true);
            if(this.componentClass != mainActivity) {
                activity.getPackageManager().setComponentEnabledSetting(
                        new ComponentName(this.packageName, mainActivity),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                );
                activity.getPackageManager().setComponentEnabledSetting(
                        new ComponentName(this.packageName, this.componentClass),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                );
                this.componentClass = mainActivity;
            }
            return;
        }

        String activeClass = this.packageName + ".MainActivity" + enableIcon;
        if (this.componentClass.equals(activeClass)) {
            promise.reject("Icon already in use.");
            return;
        }

        promise.resolve(true);
        activity.getPackageManager().setComponentEnabledSetting(
                new ComponentName(this.packageName, activeClass),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
        );
        activity.getPackageManager().setComponentEnabledSetting(
                new ComponentName(this.packageName, this.componentClass),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
        );
        this.componentClass = activeClass;
    }
}
