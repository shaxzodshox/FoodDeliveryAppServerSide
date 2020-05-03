package com.shlsoft.fooddeliveryserver.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.shlsoft.fooddeliveryserver.R;
import com.shlsoft.fooddeliveryserver.app.BaseActivity;

import info.hoang8f.widget.FButton;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FButton btn_sign = findViewById(R.id.btnSignIn);
        TextView slogan = findViewById(R.id.textSlogan);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/bee_leave.ttf");
        slogan.setTypeface(typeface);

        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            }
        });
    }
}
