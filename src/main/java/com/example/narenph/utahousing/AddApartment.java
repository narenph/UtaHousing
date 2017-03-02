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

public class AddApartment extends Activity implements OnClickListener {

    private EditText editAptName;
    private EditText editAptType;
    private EditText editAptUnits;
    private Button addButton;
    HttpClient httpClient;
    HttpPost httppost;
    HttpResponse response;
    HttpEntity entity;
    InputStream isr;
    String name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_apartment);
        editAptName = (EditText) findViewById(R.id.editAptName);
        editAptType = (EditText) findViewById(R.id.editAptType);
        editAptUnits = (EditText) findViewById(R.id.editAptUnits);
        addButton = (Button) findViewById(R.id.btnadd);

        addButton.setOnClickListener(this);
    }

    @Override
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
        System.out.println("saved into DB");*/

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
                    AddApartment.this,
                    "Apartment Added Succesfully", Toast.LENGTH_SHORT)
                    .show();

            Intent i = new Intent(AddApartment.this, AdminOptionPage.class);
            startActivity(i);
            finish();
        }
    }

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

                String fullURL = "http://omega.uta.edu/~gxr7481/add_apartment.php?" + toPostPHP;
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

            System.out.println("Its Working");

            return null;
        }

    }
}
