package com.dudu.wearlauncher.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import com.dudu.wearlauncher.R;
import com.dudu.wearlauncher.ui.BaseActivity;

public class SettingsActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        findViewById(R.id.card_settings_about).setOnClickListener(v -> {
            startActivity(new Intent(this, AboutActivity.class));
        });
    }
    
}