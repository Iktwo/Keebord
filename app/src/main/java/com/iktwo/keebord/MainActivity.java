package com.iktwo.keebord;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;

import com.iktwo.keebord.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        final String KeyboardId = this.getApplicationContext().getPackageName()
                + "/." + Keebord.class.getSimpleName();

        binding.settings.setOnClickListener(view -> {
            InputMethodManager imeManager = (InputMethodManager)
                    getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);

            boolean isEnabled = false;

            for (InputMethodInfo imeInfo : imeManager.getEnabledInputMethodList()) {
                if (imeInfo.getId().equals(KeyboardId)) {
                    isEnabled = true;
                    break;
                }
            }

            if (isEnabled) {
                imeManager.showInputMethodPicker();
            } else {
                MainActivity.this.startActivity(new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS));
            }
        });
    }
}
