package com.gpakorea.gad.sample;

import android.widget.EditText;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.gad.sdk.Gad;
import com.google.android.material.textfield.TextInputLayout;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/** Exercises the real UI and public SDK without initializing it or making ad requests. */
@RunWith(AndroidJUnit4.class)
public class KeylessStartupTest {
    @Test
    public void startupAndIncompleteInputsNeverInitializeSdk() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                EditText mediaKey = activity.findViewById(R.id.media_key);
                EditText appUid = activity.findViewById(R.id.user_id);
                TextInputLayout mediaLayout = activity.findViewById(R.id.media_key_layout);
                TextInputLayout uidLayout = activity.findViewById(R.id.user_id_layout);
                assertEquals("", mediaKey.getText().toString());
                assertEquals("", appUid.getText().toString());
                assertTrue(((TextView) activity.findViewById(R.id.environment_notice))
                        .getText().toString().contains("STAGING"));
                assertSdkUninitialized(activity);

                activity.findViewById(R.id.button_initialize).performClick();
                assertNotNull(mediaLayout.getError());
                assertNotNull(uidLayout.getError());
                assertSdkUninitialized(activity);

                // A uid without a key, and whitespace in either field, must remain blocked.
                mediaKey.setText("   ");
                appUid.setText("sample-test-user");
                activity.findViewById(R.id.button_initialize).performClick();
                assertNotNull(mediaLayout.getError());
                assertNull(uidLayout.getError());
                assertSdkUninitialized(activity);

                mediaKey.setText("sample-test-key");
                appUid.setText("   ");
                activity.findViewById(R.id.button_initialize).performClick();
                assertNull(mediaLayout.getError());
                assertNotNull(uidLayout.getError());
                assertSdkUninitialized(activity);

                // Copied placeholders must not turn into a real SDK session.
                mediaKey.setText("{INSERT_MEDIA_KEY}");
                appUid.setText("{INSERT_USER_ID}");
                activity.findViewById(R.id.button_initialize).performClick();
                assertNotNull(mediaLayout.getError());
                assertNotNull(uidLayout.getError());
                assertSdkUninitialized(activity);

                mediaKey.setText("sample-test-key");
                appUid.setText("1234567890123456789012345678901234567890123456789");
                activity.findViewById(R.id.button_initialize).performClick();
                assertNotNull(uidLayout.getError());
                assertSdkUninitialized(activity);

                // Even a saved embedded screen must not return before a new initialization.
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, new androidx.fragment.app.Fragment())
                        .commitNow();
                assertNotNull(activity.getSupportFragmentManager().findFragmentById(R.id.fragment));
            });
            scenario.recreate();
            scenario.onActivity(activity -> {
                assertEquals("", ((EditText) activity.findViewById(R.id.media_key)).getText().toString());
                assertEquals("", ((EditText) activity.findViewById(R.id.user_id)).getText().toString());
                assertSdkUninitialized(activity);
            });
        }
    }

    private void assertSdkUninitialized(MainActivity activity) {
        assertFalse(Gad.isValid());
        assertTrue(activity.getSharedPreferences("com.gad.sdk", 0).getAll().isEmpty());
        assertFalse(activity.findViewById(R.id.button_activity).isEnabled());
        assertFalse(activity.findViewById(R.id.button_fragment).isEnabled());
        assertNull(activity.getSupportFragmentManager().findFragmentById(R.id.fragment));
    }
}
