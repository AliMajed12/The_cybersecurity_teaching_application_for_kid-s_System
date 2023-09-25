package com.example.LearnChildrenSecurityApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ParentControlScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_control_screen);
    }
    public void goManageMyChildrenActivity(View v)
    {
        Intent intent = new Intent( ParentControlScreenActivity.this, ParentManageChildActivity.class );
        startActivity ( intent );


    }
    public void goViewReportActivity(View v)
    {
        Intent intent = new Intent( ParentControlScreenActivity.this, ChildViewLessonsActivity.class );
        startActivity ( intent );


    }
    public void goManageEvaluate(View v)
    {
        Intent intent = new Intent( ParentControlScreenActivity.this, ParentEvaluateLessonsActivity.class );
        startActivity ( intent );
    }

    public void goConactForHelp(View v)
    {
        Intent intent = new Intent( ParentControlScreenActivity.this, ConactForHelpActivity.class );
        startActivity ( intent );
    }
    public void gologout(View v)
    {
        finish();
        moveTaskToBack ( true );
    }
}