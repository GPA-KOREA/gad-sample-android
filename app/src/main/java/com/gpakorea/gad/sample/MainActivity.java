package com.gpakorea.gad.sample;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.Editable;
import android.text.TextWatcher;

import com.gad.sdk.Gad;
import com.gad.sdk.GadEnvironment;
import com.gpakorea.gad.sample.databinding.ActivityMainBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mBinding;
    private boolean initialized;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // A new sample screen requires fresh input and explicit initialization.
        // Do not restore an SDK fragment with credentials from a previous screen.
        super.onCreate(null);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        TextWatcher credentialWatcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                invalidateSession();
            }
            @Override public void afterTextChanged(Editable s) { }
        };
        mBinding.mediaKey.addTextChangedListener(credentialWatcher);
        mBinding.userId.addTextChangedListener(credentialWatcher);
        mBinding.buttonInitialize.setOnClickListener(v -> initializeGad());

        mBinding.buttonActivity.setOnClickListener(v -> {
            if (initialized) {
                Gad.showAdList(this);
            }
        });

        mBinding.buttonFragment.setOnClickListener(v -> {
            if (initialized) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, Gad.getAdListFragment(this))
                        .commit();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // This integration sample intentionally does not restore a session or view state.
        outState.clear();
    }

    private String inputText(CharSequence text) {
        return text == null ? "" : text.toString().trim();
    }

    private void initializeGad() {
        String mediaKey = inputText(mBinding.mediaKey.getText());
        String appUid = inputText(mBinding.userId.getText());
        boolean missingMediaKey = isMissingInput(mediaKey);
        boolean missingAppUid = isMissingInput(appUid);
        boolean invalidAppUidLength = appUid.length() > 48;
        mBinding.mediaKeyLayout.setError(missingMediaKey ? getString(R.string.media_key_required) : null);
        mBinding.userIdLayout.setError(missingAppUid ? getString(R.string.app_uid_required)
                : invalidAppUidLength ? getString(R.string.app_uid_too_long) : null);
        if (missingMediaKey || missingAppUid || invalidAppUidLength) {
            return;
        }

        // This sample always selects staging explicitly; see README for production opt-in.
        Gad.init(this, mediaKey, appUid, GadEnvironment.STAGING);
        initialized = true;
        mBinding.buttonInitialize.setEnabled(false);
        mBinding.buttonActivity.setEnabled(true);
        mBinding.buttonFragment.setEnabled(true);
    }

    private void invalidateSession() {
        initialized = false;
        mBinding.buttonInitialize.setEnabled(true);
        mBinding.buttonActivity.setEnabled(false);
        mBinding.buttonFragment.setEnabled(false);
        androidx.fragment.app.Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commitNow();
        }
    }

    private boolean isMissingInput(String value) {
        return TextUtils.isEmpty(value) || (value.startsWith("{") && value.endsWith("}"));
    }
}
