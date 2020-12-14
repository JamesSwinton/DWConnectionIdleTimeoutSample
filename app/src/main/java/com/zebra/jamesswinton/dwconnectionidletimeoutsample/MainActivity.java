package com.zebra.jamesswinton.dwconnectionidletimeoutsample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    // Profile Settings
    private static final String PROFILE_NAME = "connection_idle_timeout_test_profile";

    // Actions
    public static final String DW_ACTION = "com.symbol.datawedge.api.ACTION";

    // Extras
    private static final String EXTRA_CREATE_PROFILE = "com.symbol.datawedge.api.CREATE_PROFILE";
    private static final String EXTRA_SET_CONFIG = "com.symbol.datawedge.api.SET_CONFIG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set Config
        setProfileConfig();

    }

    public void setProfileConfig() {
        // Create Base Profile Config Bundle
        Bundle profileConfig = new Bundle();
        profileConfig.putString("PROFILE_NAME", PROFILE_NAME);
        profileConfig.putString("PROFILE_ENABLED", "true");
        profileConfig.putString("CONFIG_MODE", "CREATE_IF_NOT_EXIST");

        // Create Associated App Bundle
        Bundle appConfig = new Bundle();
        appConfig.putString("PACKAGE_NAME", getPackageName());
        appConfig.putStringArray("ACTIVITY_LIST", new String[]{getPackageName() + "." + getClass().getName()});

        // Store AppConfig in ProfileConfig
        profileConfig.putParcelableArray("APP_LIST", new Bundle[]{appConfig});

        // Create Barcode Config Bundle
        Bundle barcodeConfig = new Bundle();
        barcodeConfig.putString("PLUGIN_NAME", "BARCODE");
        barcodeConfig.putString("RESET_CONFIG", "true");

        // Create BarcodeProperties Bundle
        Bundle barcodeProps = new Bundle();
        barcodeProps.putString("scanner_selection", "auto");
        barcodeProps.putString("scanner_input_enabled", "true");

        /**
         * These are the two key lines here
         *
         * configure_all_scanners sets the value for all scanners
         *
         * connection_idle_time sets the timeout for the connection
         *
         */

        barcodeProps.putString("configure_all_scanners", "true");
        barcodeProps.putString("connection_idle_time", "1800");

        // Store BarcodeProperties in BarcodeConfig
        barcodeConfig.putBundle("PARAM_LIST", barcodeProps);

        // Store BarcodeConfig in Profile Config
        profileConfig.putBundle("PLUGIN_CONFIG", barcodeConfig);

        // Send Intent
        sendDataWedgeIntentWithExtra(DW_ACTION, EXTRA_SET_CONFIG, profileConfig);
    }

    private void sendDataWedgeIntentWithExtra(String action, String extraKey, Bundle extras) {
        Intent dwIntent = new Intent();
        dwIntent.setAction(action);
        dwIntent.putExtra(extraKey, extras);
        sendBroadcast(dwIntent);
    }
}