package com.example.LearnChildrenSecurityApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ChildControlScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_control_screen);
    }
    public void goViewLessonsActivity(View v)
    {
        Intent intent = new Intent( ChildControlScreenActivity.this, ChildViewLessonsActivity.class );

        startActivity ( intent );


    }
    public void goManageLessonsExamsActivity(View v)
    {
        Intent intent = new Intent( ChildControlScreenActivity.this, ChildManageExamsLessonsActivity.class );
        startActivity ( intent );


    }
    public void goEvaluateActivity(View v)
    {
        Intent intent = new Intent( ChildControlScreenActivity.this, ParentEvaluateLessonsActivity.class );
        startActivity ( intent );
    }
    public void goClientShowHisEvaluationActivity(View v)
    {

    }

    public void goConactForHelp(View v)
    {
        Intent intent = new Intent( ChildControlScreenActivity.this, ConactForHelpActivity.class );
        startActivity ( intent );
    }
    public void gologout(View v)
    {
        finish();
        moveTaskToBack ( true );
    }
}