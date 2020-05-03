package com.shlsoft.fooddeliveryserver.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.shlsoft.fooddeliveryserver.R;
import com.shlsoft.fooddeliveryserver.app.BaseActivity;
import com.shlsoft.fooddeliveryserver.common.Common;
import com.shlsoft.fooddeliveryserver.model.User;

import info.hoang8f.widget.FButton;

public class SignInActivity extends BaseActivity {

    private MaterialEditText edt_phone, edt_password;
    private TextView tv_error;
    private FButton btn_signIn;

    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference table_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initViews();
        phoneEditTextSetup();

        //InitFirebase
        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("User"); //Table name is User

        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(edt_phone.getText().toString(), edt_password.getText().toString());
            }
        });
    }

    private void initViews() {
        edt_phone = findViewById(R.id.edt_phone);
        edt_password = findViewById(R.id.edt_password);
        btn_signIn = findViewById(R.id.btnSignIn);
        tv_error = findViewById(R.id.tv_error);
    }

    private void phoneEditTextSetup() {
        edt_phone.setText("+998");
        edt_phone.requestFocus();

        edt_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith("+998")) {
                    edt_phone.setText("+998");
                    Selection.setSelection(edt_phone.getText(), edt_phone.getText().toString().length());
                }
            }
        });
    }

    private void showError(String error_msg) {
        tv_error.setText(error_msg);
        tv_error.setVisibility(View.VISIBLE);
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                tv_error.setVisibility(View.INVISIBLE);
            }
        }.start();
    }


    private void signIn(final String phone, final String password) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.iltimos_kuting));
        progressDialog.setCancelable(false);
        progressDialog.show();

        final String phoneWithoutPlus = phone.replace("+", "");

        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();

                //Check if user not exists in database
                if (dataSnapshot.child(phoneWithoutPlus).exists()) {
                    //Get user information
                    User user = dataSnapshot.child(phoneWithoutPlus).getValue(User.class);
                    user.setPhone(edt_phone.getText().toString()); //set Phone
                    if (Boolean.parseBoolean(user.getIsStaff())) {//if is staff true
                        if (user.getPassword().equals(password)) {
                            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                            SignInActivity.this.finish();
                        } else {
                            //Password is wrong
                            edt_password.setText("");
                            edt_password.requestFocus();
                            showError(getString(R.string.parol_xato));
                        }
                    }
                } else {
                    //if the user does not exists
                    progressDialog.dismiss();
                    showError(getString(R.string.xodim_topilmadi));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
