package com.example.LearnChildrenSecurityApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminControlScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_control_screen);
    }
    public void goManageAdminUser(View v)
    {
        Intent intent = new Intent( AdminControlScreenActivity.this, ManageAdminActivity.class );
        startActivity ( intent );


    }
    public void goManageParent(View v)
    {
        Intent intent = new Intent( AdminControlScreenActivity.this, ManageParentActivity.class );
        startActivity ( intent );


    }
    public void goManageChild(View v)
    {
        Intent intent = new Intent( AdminControlScreenActivity.this, ManageChildActivity.class );
        startActivity ( intent );


    }
    public void ContactUS(View v)
    {
        Intent intent = new Intent( AdminControlScreenActivity.this, ConactForHelpActivity.class );
        startActivity ( intent );


    }
    public void goAddLessons(View v)
    {
        Intent intent = new Intent( AdminControlScreenActivity.this, AddLessonsActivity.class );
        startActivity ( intent );


    }

    public void goManageLessons(View v)
    {
        Intent intent = new Intent( AdminControlScreenActivity.this, ManageLessonsActivity.class );
        startActivity ( intent );
    }
    public void gologout(View v)
    {
        finish();
        moveTaskToBack ( true );
    }
}