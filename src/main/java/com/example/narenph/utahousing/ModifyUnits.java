package com.example.narenph.utahousing;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

//import com.parse.ParseInstallation;

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ModifyUnits extends Activity{

    private Spinner spinner1, spinner2;
    private EditText editAptUnits;
    private Button addButton;
    HttpClient httpClient;
    HttpPost httppost;
    HttpResponse response;
    HttpEntity entity;
    InputStream isr;
    String name="";
    String[] aptname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_units);
        editAptUnits = (EditText) findViewById(R.id.editAptUnits);
        addButton = (Button) findViewById(R.id.btnChange);

        //addButton.setOnClickListener(this);

        addItemsOnSpinner2();
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();

        UserValidation validates = new UserValidation();
        validates.execute();
    }

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
        addButton = (Button) findViewById(R.id.btnChange);

        addButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(ModifyUnits.this,
                        "OnClickListener : " +
                                "\nSpinner 1 : " + String.valueOf(spinner1.getSelectedItem()) +
                                "\nName: " + name +
                                "\nSpinner 2 : " + String.valueOf(spinner2.getSelectedItem()),
                        Toast.LENGTH_SHORT).show();
                String aptunits = editAptUnits.getText().toString();

                EnterValues enter = new EnterValues();
                System.out.print("out");
                enter.execute(String.valueOf(spinner1.getSelectedItem()), String.valueOf(spinner2.getSelectedItem()),aptunits);
                System.out.print("in");
                Toast.makeText(
                        ModifyUnits.this,
                        "Units Modified Succesfully", Toast.LENGTH_SHORT)
                        .show();

                Intent i = new Intent(ModifyUnits.this, AdminOptionPage.class);
                startActivity(i);
                finish();
            }

        });
    }

   /*@Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        System.out.println("Entered onClick");
        String aptname = editAptName.getText().toString();
        String apttype = editAptType.getText().toString();
        String aptunits = editAptUnits.getText().toString();
        // int intPhone = Integer.parseInt(phoneNumber);

        //storing the email id of student into parse database.

        /*ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        System.out.println(installation);
        installation.put("User_id", email);
        installation.saveInBackground();
        System.out.println("saved into DB");

        //validation sequence for all the inputs
        if(aptname.isEmpty()){
            Toast.makeText(getApplicationContext(), "Enter values in Apartment Name field",
                    Toast.LENGTH_SHORT).show();
        }else if (apttype.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter values in Apartment type field",
                    Toast.LENGTH_SHORT).show();
        }else if (aptunits.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter values in Units field",
                    Toast.LENGTH_SHORT).show();
        } else {
            System.out.print("Inside 1");
            EnterValues enter = new EnterValues();
            System.out.print("out");
            enter.execute(aptname, apttype, aptunits);
            System.out.print("in");
            Toast.makeText(
                    ModifyUnits.this,
                    "Apartment Units Modified Succesfully", Toast.LENGTH_SHORT)
                    .show();

            Intent i = new Intent(ModifyUnits.this, AdminOptionPage.class);
            startActivity(i);
            finish();
        }
    }*/

    private class EnterValues extends AsyncTask<String, String, String> {
        HttpClient httpClient;
        @SuppressWarnings("unused")
        HttpResponse httpResponse;
        HttpPost httpPost;

        @Override
        protected String doInBackground(String... params) {

            System.out.print("Inside 13");
            // TODO Auto-generated method stub
            try {
                String aptname = URLEncoder.encode(params[0], "UTF-8").replace("+", "%20");
                String apttype = URLEncoder.encode(params[1], "UTF-8").replace("+", "%20");
                String aptunits = URLEncoder.encode(params[2], "UTF-8").replace("+", "%20");


                String toPostPHP = "aptname=" + aptname + "&apttype=" + apttype + "&aptunits=" + aptunits;
                System.out.println(toPostPHP);

                String fullURL = "http://omega.uta.edu/~gxr7481/modify_apartment_units.php?" + toPostPHP;
                httpClient = new DefaultHttpClient();

                Log.i("PostActvitiy - ", "Created httpClient " + fullURL);
                httpPost = new HttpPost(fullURL);
                httpResponse = httpClient.execute(httpPost);
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.e("PostActvitiy - ", "Error in ArrayIndexOutOfBoundsException - " + e.toString());
            } catch (ClientProtocolException e) {
                Log.e("PostActvitiy - ", "Error in ClientProtocolException - " + e.toString());
            } catch (IOException e) {
                Log.e("PostActivity IO - ", e.toString());
            }

            System.out.println("wooooooooo hooo");

            return null;
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
            try {
//
                String fullURL = "http://omega.uta.edu/~gxr7481/delete_apartment.php?";


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
                    aptname[i] = json.getString("name");
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
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(ModifyUnits.this,android.R.layout.simple_spinner_item, aptname);
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
