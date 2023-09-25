package com.example.LearnChildrenSecurityApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void GoLoginParent(View v) {
        Intent intent = new Intent(MainActivity.this, LoginFristActivity.class);
        PublicData.UserType = "Parent";
        startActivity(intent);

    }

    public void GoLoginChild(View v) {

        Intent intent = new Intent(MainActivity.this, LoginFristActivity.class);
        PublicData.UserType = "Children";

        startActivity(intent);

    }

    public void GoLoginAdmin(View v) {
        Intent intent = new Intent(MainActivity.this, LoginFristActivity.class);
        PublicData.UserType = "Administrator";

        startActivity(intent);

    }

    public void GoRegister(View v) {
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);

    }
}
