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

public class RegisterActivity extends Activity implements OnClickListener {

    private EditText editSignUpEmail;
    private EditText editSignUpPassword;
    private EditText editSignUpConfirmPassword;
    private EditText editSignUpName;
    private EditText editNetId;
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
        setContentView(R.layout.activity_register);
        editSignUpEmail = (EditText) findViewById(R.id.editSignUpEmail);
        editSignUpPassword = (EditText) findViewById(R.id.editSignUpPassword);
        editSignUpConfirmPassword = (EditText) findViewById(R.id.editSignUpConfirmPassword);
        editSignUpName = (EditText) findViewById(R.id.editSignUpName);
        editNetId = (EditText) findViewById(R.id.editNetId);
        signUpButton = (Button) findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        System.out.println("Entered onClick");
        String email = editSignUpEmail.getText().toString();
        System.out.println("Printing email" + email);
        String password = editSignUpPassword.getText().toString();
        String confirmPassword = editSignUpConfirmPassword.getText().toString();
        String name = editSignUpName.getText().toString();
        String netId = editNetId.getText().toString();
        // int intPhone = Integer.parseInt(phoneNumber);

        //storing the email id of student into parse database.

        /*ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        System.out.println(installation);
        installation.put("User_id", email);
        installation.saveInBackground();
        System.out.println("saved into DB");*/

        //validation sequence for all the inputs
        if(email.isEmpty()){
            Toast.makeText(getApplicationContext(), "Enter values in email field",
                    Toast.LENGTH_SHORT).show();
        }else if (password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter values in password field",
                    Toast.LENGTH_SHORT).show();
        }else if (confirmPassword.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter values in confirm password field",
                    Toast.LENGTH_SHORT).show();
        }else if (name.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter values in name field",
                    Toast.LENGTH_SHORT).show();
        }else if (netId.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter values in NetId field",
                    Toast.LENGTH_SHORT).show();
        }else if (!confirmPassword.equals(password)) {
            Toast.makeText(getApplicationContext(), "Passwords don't match",
                    Toast.LENGTH_SHORT).show();
        } else if (password.length() < 8) {
            Toast.makeText(getApplicationContext(),
                    "Password must contain atleast 8 characters",
                    Toast.LENGTH_SHORT).show();
        } else if (!email.contains("@mavs.uta.edu")) {
            Toast.makeText(getApplicationContext(), "Enter a valid Mavs ID",
                    Toast.LENGTH_SHORT).show();
        } else if (!(netId.length() == 7)) {
            Toast.makeText(getApplicationContext(),
                    "Enter a valid NetID", Toast.LENGTH_SHORT).show();
        } else {
            EnterValues enter = new EnterValues();
            System.out.print("out");
            enter.execute(name, email, password, netId);
            System.out.print("in");
            Toast.makeText(
                    RegisterActivity.this,
                    "Please Login", Toast.LENGTH_SHORT)
                    .show();

            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
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

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                String name = URLEncoder.encode(params[0], "UTF-8").replace("+", "%20");
                String email = URLEncoder.encode(params[1], "UTF-8").replace("+", "%20");
                String password = URLEncoder.encode(params[2], "UTF-8").replace("+", "%20");
                String netId = URLEncoder.encode(params[3], "UTF-8").replace("+", "%20");


                String toPostPHP = "name=" + name + "&email=" + email + "&password=" + password
                        + "&netId=" + netId;
                System.out.println(toPostPHP);

                String fullURL = "http://omega.uta.edu/~gxr7481/create_user_profile.php?" + toPostPHP;
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