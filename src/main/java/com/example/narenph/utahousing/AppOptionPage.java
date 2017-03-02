package com.example.narenph.utahousing;

import android.os.Bundle;
import android.view.View;

import android.app.Activity;

import android.widget.Button;
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

import android.util.Log;

public class AppOptionPage  extends Activity{


    private Button btnNewApplication,btnStsEnquiry,btnEditInfo,btnRenewLease,btnCancelApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_option);

        btnNewApplication=(Button)findViewById(R.id.btnNewApplication);
        btnStsEnquiry = (Button) findViewById(R.id.btnStsEnquiry);
        btnEditInfo = (Button) findViewById(R.id.btnEditInfo);
        btnRenewLease = (Button) findViewById(R.id.btnRenewLease);
        btnCancelApplication = (Button) findViewById(R.id.btnCancelApplication);

        //btnNewApplication.setOnClickListener(this);

    }

    public void newApplication(View view)
    {
        Intent intent = new Intent(AppOptionPage.this, AptList.class);
        startActivity(intent);
    }
    public void stsEnquiry(View view)
    {
        Intent intent = new Intent(AppOptionPage.this, StatusEnquiry.class);
        startActivity(intent);
    }
    public void editInfo(View view)
    {
        Intent intent = new Intent(AppOptionPage.this, PersonalInfo.class);
        startActivity(intent);
    }
    public void renewLease(View view)
    {
        Intent intent = new Intent(AppOptionPage.this, RegisterActivity.class);
        startActivity(intent);
    }
    public void cancelApplication(View view)
    {
        Intent intent = new Intent(AppOptionPage.this, CancelApplication.class);
        startActivity(intent);

        /*String DEFAULT = "N/A";
        SharedPreferences sharedpreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        final String name = sharedpreferences.getString("name", DEFAULT);*/

        //UserValidation validates = new UserValidation();
        //validates.execute(name);
    }
    public void logout(View view)
    {
        Intent intent = new Intent(AppOptionPage.this, LoginActivity.class);
        startActivity(intent);
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
            String netid = params[0];
            try {
//
                String toVerify = "netId=" + netid;

                String fullURL = "http://omega.uta.edu/~gxr7481/cancel_application.php?" + toVerify;


                httpClient = new DefaultHttpClient();
                httpPost = new HttpPost(fullURL);
                System.out.println("httpPost is done");
                System.out.println(fullURL);

                httpResponse = httpClient.execute(httpPost);
                System.out.println(httpResponse);



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
                Toast.makeText(getApplicationContext(), "Application Cancelled!", Toast.LENGTH_LONG)
                        .show();

                Intent openActivity;
                System.out.println("Exeucuting shared preferences");
                openActivity = new Intent(AppOptionPage.this, AppOptionPage.class);
                startActivity(openActivity);
                finish();
            }
        }

    }

}
