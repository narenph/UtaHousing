package com.example.narenph.utahousing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.content.Context;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;


public class CancelApplication extends Activity{


    private Spinner spinner1, spinner2;
    private Button btnSubmit;
    final String DEFAULT = "N/A";
    String name;
    String[] aptname;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_application);

        SharedPreferences sharedpreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        name = sharedpreferences.getString("name", DEFAULT);

        addItemsOnSpinner2();
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();

        UserValidation validates = new UserValidation();
        validates.execute(name);
    }

    // add items into spinner dynamically
    public void addItemsOnSpinner2() {

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        List<String> list = new ArrayList<String>();
        list.add("1 BHK");
        list.add("2 BHK");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
    }

    public void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());

    }

    public void addListenerOnButton() {

        //spinner  = (Spinner) findViewById(R.id.spinner);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(CancelApplication.this,
                        "OnClickListener : " +
                                "\nSpinner 1 : " + String.valueOf(spinner1.getSelectedItem()) +
                                "\nName: " + name +
                                "\nSpinner 2 : " + String.valueOf(spinner2.getSelectedItem()),
                        Toast.LENGTH_SHORT).show();

                UserValidationClick validates = new UserValidationClick();
                validates.execute(name, String.valueOf(spinner1.getSelectedItem()), String.valueOf(spinner2.getSelectedItem()));
            }

        });
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

    }

    private class UserValidationClick extends AsyncTask<String, String, String> {

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
                String name = URLEncoder.encode(params[1], "UTF-8").replace("+", "%20");
                String type = URLEncoder.encode(params[2], "UTF-8").replace("+", "%20");

                String toVerify = "netId=" + netid + "&name=" + name+ "&type=" + type;

                String fullURL = "http://omega.uta.edu/~gxr7481/cancel_application.php?" + toVerify;


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

            if (result.equalsIgnoreCase("2 BHK")) {
                Toast.makeText(getApplicationContext(), "Select 2 BHK from drop down!!!!", Toast.LENGTH_LONG)
                        .show();
            }else if(result.equalsIgnoreCase("1 BHK")){
                Toast.makeText(getApplicationContext(), "Select 1 BHK from drop down!!!!", Toast.LENGTH_LONG)
                        .show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Selected Application has been cancelled!!!!", Toast.LENGTH_LONG)
                        .show();
                Intent openActivity;
                openActivity = new Intent(CancelApplication.this, AppOptionPage.class);
                startActivity(openActivity);
                finish();
            }
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

        String result = null;
        final List<String> list1 = new ArrayList<String>();

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
                bReader = new BufferedReader(new InputStreamReader(isr,"iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                while ((line = bReader.readLine()) != null) {
                    System.out.println("Inside1");
                    sb.append(line + "\n");
                }
                isr.close();
                result = sb.toString();
            } catch (IOException e) {
                Log.e("LoginActivity - ", "Error in ISR to String conversion - " + e.toString());

            }
            return result;


        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                System.out.println(result);
                System.out.println("Inside3");

                JSONArray JA=new JSONArray(result);
                System.out.println("Inside4");
                JSONObject json = null;
                System.out.println(JA.length());
                aptname = new String[JA.length()];

                for(int i=0;i<JA.length();i++)
                {
                    System.out.println("Inside5");
                    json = JA.getJSONObject(i);
                    System.out.println("Inside6");
                    aptname[i] = json.getString("aptname");
                }
                Toast.makeText(getApplicationContext(),"sss",Toast.LENGTH_LONG).show();

                for(int i =0;i<aptname.length;i++){
                    System.out.println("Inside7");
                    list1.add(aptname[i]);
                }
                spinner_fn();
            }
            catch(Exception e){
                Log.e("Fail 3",e.toString());
            }
        }



    }

    private void spinner_fn()
    {
        System.out.println("Inside8");
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(CancelApplication.this,android.R.layout.simple_spinner_item, aptname);
        spinner1.setAdapter(dataAdapter1);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                spinner1.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });
    }
}
