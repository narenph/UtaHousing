package com.example.narenph.utahousing;


import android.os.Bundle;
import android.view.View;

import android.app.Activity;

import android.content.Intent;


public class AdminOptionPage  extends Activity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }

    public void addAparment(View view)
    {
        Intent intent = new Intent(AdminOptionPage.this, AddApartment.class);
        startActivity(intent);
    }
    public void removeApartment(View view)
    {
        Intent intent = new Intent(AdminOptionPage.this, DeleteApartment.class);
        startActivity(intent);
    }
    public void modifyUnits(View view)
    {
        Intent intent = new Intent(AdminOptionPage.this, ModifyUnits.class);
        startActivity(intent);
    }

    public void allotedList(View view)
    {
        Intent intent = new Intent(AdminOptionPage.this, AllotedList.class);
        startActivity(intent);
    }


    public void logout(View view)
    {
        Intent intent = new Intent(AdminOptionPage.this, LoginActivity.class);
        startActivity(intent);
    }
}
