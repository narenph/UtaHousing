package com.example.narenph.utahousing;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


@SuppressWarnings("deprecation")
public class LoginActivity extends Activity implements OnClickListener {

    private Button btnLogin;
    private Button btnLinkToRegister;
    EditText inputNetId = null;
    EditText inputPassword = null;
    String netid, password, result;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputNetId = (EditText) findViewById(R.id.editText);
        inputPassword = (EditText) findViewById(R.id.editText2);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        btnLogin.setOnClickListener(this);

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        System.out.print("clickrd");
        switch (v.getId()) {

            case R.id.btnLogin:
                System.out.println("Entered OnClick");
			/*
			 * Intent openActivity; openActivity = new
			 * Intent(LoginActivity.this, MainActivity_Refugee.class); //Log.i(
			 * "LOGIN ACTIVIY", "ref 2"); startActivity(openActivity); //Log.i(
			 * "LOGIN ACTIVIY", "ref 3"); finish();
			 */
                netid = inputNetId.getText().toString().trim();
                password = inputPassword.getText().toString();

                if (netid.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter Username!", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter the password!", Toast.LENGTH_SHORT).show();
                } else {

                    UserValidation validates = new UserValidation();
                    validates.execute(netid, password);
                }
                break;

            case R.id.btnLinkToRegisterScreen:
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
                finish();
                break;
        }

    }

    private class UserValidation extends AsyncTask<String, String, String> {

        HttpClient httpClient;
        HttpResponse httpResponse;
        HttpPost httpPost;
        HttpEntity entity;
        InputStream isr;
        BufferedReader bReader;
        String line;
        String data;

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String password1 = params[1];
            try {
//				String email = URLEncoder.encode(params[0].trim(), "UTF-8");
//				String password = URLEncoder.encode(params[1].trim(), "UTF-8");

                String toVerify = "netId=" + username + "&password=" + password1;

                String fullURL = "http://omega.uta.edu/~gxr7481/login_val.php?" + toVerify;

                httpClient = new DefaultHttpClient();
                httpPost = new HttpPost(fullURL);
                System.out.println("httpPost is done");
                System.out.println(fullURL);

                httpResponse = httpClient.execute(httpPost);
                System.out.println(httpResponse);

                entity = httpResponse.getEntity();
                if (entity != null) {
                    isr = entity.getContent();
                    System.out.println("byte - " + isr.available());
                }
            } catch (UnsupportedEncodingException e) {
                Log.e("log_tag", " Error in UnsupportedEncodingException - " + e.toString());
            } catch (ClientProtocolException e) {
                Log.e("log_tag", " Error in ClientProtocolException - " + e.toString());
            } catch (IOException e) {
                Log.e("log_tag", " Error in IOException - " + e.toString());
            } catch (Exception e) {
                Log.e("log_tag", " Error in Connection" + e.toString());
            }

            try {
                bReader = new BufferedReader(new InputStreamReader(isr), 8);
                line = null;
                while ((line = bReader.readLine()) != null) {
                    data = line;
                    Log.i("LoginActivity - ", "Data from omega " + line);
                }
                return data;
            } catch (IOException e) {
                Log.e("LoginActivity - ", "Error in ISR to String conversion - " + e.toString());
                return null;
            }
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("Printing the result " + result);

            Log.i("Login Activity", "on Post " + result);

            if (result.equalsIgnoreCase("0000")) {
                Toast.makeText(getApplicationContext(), "Username or Password does not exist!", Toast.LENGTH_LONG)
                        .show();
            } else {
                String DEFAULT = "N/A";

                Toast.makeText(getApplicationContext(), "Logging in...",
                        Toast.LENGTH_SHORT).show();
				System.out.println("Exeucuting shared preferences");
				SharedPreferences sharedpreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedpreferences.edit();
				editor.putString("name", inputNetId.getText().toString().trim());
				editor.putString("pass", inputPassword.getText().toString());
				editor.commit();

                String name = sharedpreferences.getString("name", DEFAULT);


                /*String fullname = result.split(",")[0].trim();
                System.out.println(result);
                String username = result.split(",")[0].trim();
                String password = result.split(",")[1].trim();


                Log.i("Login Activity", "on Post " + username);
                Log.i("Login Activity", "on Post " + password);*/

                if(name.equalsIgnoreCase("admin")) {
                    Intent openActivity;
                    openActivity = new Intent(LoginActivity.this, AdminOptionPage.class);
                    startActivity(openActivity);
                    finish();
                }
                else{
                    Intent openActivity;
                    openActivity = new Intent(LoginActivity.this, AppOptionPage.class);
                    startActivity(openActivity);
                    finish();
                }
            }
        }

    }

}
