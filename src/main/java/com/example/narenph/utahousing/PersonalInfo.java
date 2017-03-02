package com.example.narenph.utahousing;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

//import com.parse.ParseInstallation;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PersonalInfo extends Activity implements OnClickListener {

    private EditText editStuName;
    private EditText editStuDOB;
    private EditText editStuAdr;
    private EditText editStuPhone;
    private EditText editStuEmrName;
    private EditText editStuEmrRel;
    private EditText editStuEmrPh;
    private Button signUpButton;
    HttpClient httpClient;
    HttpPost httppost;
    HttpResponse response;
    HttpEntity entity;
    InputStream isr;
    String name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        editStuName = (EditText) findViewById(R.id.editStuName);
        editStuDOB = (EditText) findViewById(R.id.editStuDOB);
        editStuAdr = (EditText) findViewById(R.id.editStuAdr);
        editStuPhone = (EditText) findViewById(R.id.editStuPhone);
        editStuEmrName = (EditText) findViewById(R.id.editStuEmrName);
        editStuEmrRel = (EditText) findViewById(R.id.editStuEmrRel);
        editStuEmrPh = (EditText) findViewById(R.id.editStuEmrPh);
        signUpButton = (Button) findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        System.out.println("Entered onClick");
        String stuName = editStuName.getText().toString();
        System.out.println("Printing email" + stuName);
        String stuDob = editStuDOB.getText().toString();
        String stuAdr = editStuAdr.getText().toString();
        String stuPh = editStuPhone.getText().toString();
        String emrName = editStuEmrName.getText().toString();
        String emrRel = editStuEmrRel.getText().toString();
        String emrPh = editStuEmrPh.getText().toString();
        // int intPhone = Integer.parseInt(phoneNumber);

        //storing the email id of student into parse database.

        /*ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        System.out.println(installation);
        installation.put("User_id", email);
        installation.saveInBackground();
        System.out.println("saved into DB");*/

        //validation sequence for all the inputs
        if(stuName.isEmpty()){
            Toast.makeText(getApplicationContext(), "Enter values in name field",
                    Toast.LENGTH_SHORT).show();
        }else if (stuDob.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter values in DOB field",
                    Toast.LENGTH_SHORT).show();
        }else if (stuAdr.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter values in address field",
                    Toast.LENGTH_SHORT).show();
        }else if (stuPh.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter values in Phone number field",
                    Toast.LENGTH_SHORT).show();
        }else if (emrPh.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter values in Emergency Phone number field",
                    Toast.LENGTH_SHORT).show();
        }else if (emrName.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter values in Emergency Name field",
                    Toast.LENGTH_SHORT).show();
        }else if (emrRel.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter values in Emergency Relation field",
                    Toast.LENGTH_SHORT).show();
        }else if (stuPh.length() < 10 || emrPh.length() < 10) {
            Toast.makeText(getApplicationContext(),
                    "Phone number must contain 10 characters",
                    Toast.LENGTH_SHORT).show();
        } else {
            EnterValues enter = new EnterValues();
            System.out.print("out");
            enter.execute(stuName, stuDob, stuAdr, stuPh, emrName, emrRel, emrPh);
            System.out.print("in");
            Toast.makeText(
                    PersonalInfo.this,
                    "Please Register", Toast.LENGTH_SHORT)
                    .show();

            Intent i = new Intent(PersonalInfo.this, AppOptionPage.class);
            startActivity(i);
            finish();
        }
    }

    private class EnterValues extends AsyncTask<String, Void, String> {

        HttpClient httpClient;
        @SuppressWarnings("unused")
        HttpResponse httpResponse;
        HttpPost httpPost;
        public void execute(String name, String password, String email) {
            // TODO Auto-generated method stub
            //enter.execute(stuName, stuDob, stuAdr, stuPh, emrName, emrRel, emrPh);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                String name = URLEncoder.encode(params[0], "UTF-8").replace("+", "%20");
                String dob = URLEncoder.encode(params[1], "UTF-8").replace("+", "%20");
                String adr = URLEncoder.encode(params[2], "UTF-8").replace("+", "%20");
                String ph = URLEncoder.encode(params[3], "UTF-8").replace("+", "%20");
                String eName = URLEncoder.encode(params[4], "UTF-8").replace("+", "%20");
                String eRel = URLEncoder.encode(params[5], "UTF-8").replace("+", "%20");
                String ePh = URLEncoder.encode(params[6], "UTF-8").replace("+", "%20");


                String toPostPHP = "name=" + name + "&dob=" + dob + "&adr=" + adr + "&ph=" + ph
                        + "&ename=" + eName + "&erel=" + eRel+ "&ePh=" + ePh;
                System.out.println(toPostPHP);

                String fullURL = "http://omega.uta.edu/~gxr7481/update_user_profile.php?" + toPostPHP;
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



            return null;
        }

    }
}
