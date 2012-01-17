package com.fpassword.android;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;

public class OptionsActivity extends PreferenceActivity {

    public static final String KEY_SAVE_KEYS = "save_keys";

    public static final String KEY_CLEAR_KEYS = "clear_keys";

    private final Database database = new Database(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database.open();

        addPreferencesFromResource(R.xml.options);

        findPreference(KEY_CLEAR_KEYS).setOnPreferenceClickListener(new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                clearKeys();
                return true;
            }

        });
    }

    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }

    private void clearKeys() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                database.deleteKeys();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                Toast.makeText(OptionsActivity.this, R.string.toast_clear_keys_success, Toast.LENGTH_SHORT).show();
            }

        }.execute();
    }

}
