package com.example.narenph.utahousing;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import android.app.Activity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;
import android.content.Intent;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import android.util.Log;

public class MainActivity extends Activity  implements OnClickListener {
    private Button btnLogin,btnLinkToRegister;
    EditText ed1,ed2;
    String email, password, result;

    TextView tx1;
    int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        ed1=(EditText)findViewById(R.id.editText);
        ed2=(EditText)findViewById(R.id.editText2);


        btnLogin.setOnClickListener(this);

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
                email = ed1.getText().toString().trim();
                password = ed2.getText().toString();

                if (email.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter Username!", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter the password!", Toast.LENGTH_SHORT).show();
                } else {

                    UserValidation validates = new UserValidation();
                    validates.execute(email, password);
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
            //String username = params[0];
            //String password1 = params[1];
            try {
				String email = URLEncoder.encode(params[0].trim(), "UTF-8");
				String password = URLEncoder.encode(params[1].trim(), "UTF-8");

                String toVerify = "email=" + email + "&password=" + password;

                String fullURL = "http://omega.uta.edu/~gxr7481/verify_password.php?" + toVerify;

                httpClient = new DefaultHttpClient();
                httpPost = new HttpPost(fullURL);
                System.out.println("httpPost is done");

                httpResponse = httpClient.execute(httpPost);
                System.out.println(httpResponse);

                entity = httpResponse.getEntity();
                System.out.println(entity);
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

                Toast.makeText(getApplicationContext(), "Logging in...",
                        Toast.LENGTH_SHORT).show();
				/*System.out.println("Exeucuting shared preferences");
				SharedPreferences sharedpreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedpreferences.edit();
				editor.putString("name", inputEmail.getText().toString().trim());
				editor.putString("pass", inputPassword.getText().toString());
				editor.commit();*/
                System.out.println(result);

                String fullname = result.split(",")[0].trim();
                System.out.println(result);
                String username = result.split(",")[0].trim();
                String password = result.split(",")[1].trim();


                Log.i("Login Activity", "on Post " + username);
                Log.i("Login Activity", "on Post " + password);

                Intent openActivity;
                    System.out.println("Exeucuting shared preferences");
                    openActivity = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(openActivity);
                    finish();
            }
        }

    }
}

