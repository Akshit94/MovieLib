package com.example.jainsaab.movielib;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.jainsaab.movielib.utility.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignInActivity extends AppCompatActivity {

    SharedPreferences getPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Button logInButton = (Button) findViewById(R.id.log_in);
        Button registerBtn = (Button) findViewById(R.id.register);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LogInTask().execute(1);
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LogInTask().execute(0);
            }
        });

        getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
    }

    public class LogInTask extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... voids) {

            HttpURLConnection urlConnection;
            BufferedReader reader;
            InputStream inputStream;
            StringBuffer buffer = new StringBuffer();
            String line;

            try {

                if(voids[0] == 0){

                    Uri builtUri = Uri.parse(Constants.AUTHENTICATION_TOKEN_BASE_URL).buildUpon()
                            .appendQueryParameter(Constants.API_KEY_PARAM, Constants.API_KEY)
                            .build();
                    URL url = new URL(builtUri.toString());

                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod(Constants.HTTPS_GET);
                    urlConnection.connect();

                    inputStream = urlConnection.getInputStream();
                    if (inputStream == null) {
                        // Nothing to do.
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }

                    String tokenJsonStr = buffer.toString();
                    JSONObject tokenJsonObject = new JSONObject(tokenJsonStr);
                    String token = tokenJsonObject.getString(Constants.TOKEN_JSON_STR);
                    SharedPreferences.Editor e = getPrefs.edit();
                    e.putString(Constants.TOKEN_SHARED_PREFS, token);
                    e.apply();
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(Constants.AUTHENTICATE_URL + token));
                    startActivity(intent);

                } else if(voids[0] == 1){

                    String token = getPrefs.getString(Constants.TOKEN_SHARED_PREFS, null);

                    if(token != null){

                        Uri builtUri = Uri.parse(Constants.AUTHENTICATION_SESSION_BASE_URL).buildUpon()
                                .appendQueryParameter(Constants.API_KEY_PARAM, Constants.API_KEY)
                                .appendQueryParameter(Constants.REQUEST_TOKEN_PARAM, token)
                                .build();
                        URL url1 = new URL(builtUri.toString());

                        urlConnection = (HttpURLConnection) url1.openConnection();

                        if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                            inputStream = urlConnection.getInputStream();
                            if (inputStream == null) {
                                // Nothing to do.
                                return null;
                            }
                            reader = new BufferedReader(new InputStreamReader(inputStream));

                            while ((line = reader.readLine()) != null) {
                                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                                // But it does make debugging a *lot* easier if you print out the completed
                                // buffer for debugging.
                                buffer.append(line + "\n");
                            }

                            String sessionJsonStr = buffer.toString();
                            JSONObject tokenJsonObject = new JSONObject(sessionJsonStr);
                            String session = tokenJsonObject.getString(Constants.SESSION_JSON_STR);
                            SharedPreferences.Editor e = getPrefs.edit();
                            e.putString(Constants.SESSION_SHARED_PREFS, session);
                            e.apply();
                            Intent intent = new Intent(getApplicationContext(), MoviesActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            return 1;
                        }
                    } else {
                        return 0;
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(integer != null){
                if(integer == 0){
                    Toast.makeText(getApplicationContext(), R.string.please_register_toast, Toast.LENGTH_SHORT).show();
                } else if(integer == 1){
                    Toast.makeText(getApplicationContext(), R.string.not_allowed_tmdb_toast, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
