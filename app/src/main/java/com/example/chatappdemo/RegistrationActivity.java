package com.example.chatappdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URL;


public class RegistrationActivity extends Activity {

    private EditText etUserName;
    private EditText etPassword;
    private Button btnCreateAccount;
    private ChatApp chatApp;
    private String mUserName = "";
    private String mPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etUserName=(EditText)findViewById(R.id.et_user_name);
        etPassword=(EditText)findViewById(R.id.et_password);
        btnCreateAccount=(Button)findViewById(R.id.btn_create_account);

        //get the chatapp class object
        chatApp = ChatApp.getInstance();

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserName = etUserName.getText().toString().trim();
                mPassword = etPassword.getText().toString().trim();
                if(checkValidation()){
                 new Registration().execute();
                }
            }
        });

        etUserName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP){
                    etUserName.setError(null);
                }
                return false;
            }
        });

        etPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP){
                    etPassword.setError(null);
                }
                return false;
            }
        });
    }

    /**
     *  Create user account on server
     */
    private class Registration extends AsyncTask<URL, Integer, Boolean> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(RegistrationActivity.this, "Creating Account", "Please wait...", false);
            dialog.show();
        }

        /**
         * @param urls
         * @return true if Account created successfully else return false
         */
        @Override
        protected Boolean doInBackground(URL... urls) {
            return chatApp.Registration(mUserName,mPassword );
        }

        @Override
        protected void onPostExecute(Boolean result) {
            dialog.dismiss();
            if (result) {
                 Toast.makeText(getApplicationContext(), "Reg Success", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Reg Failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean checkValidation(){

        if(mUserName.equals("")){
            etUserName.setError("Enter a UserName");
            return false;
        }

        if(mPassword.equals("")){
            etPassword.setError("Enter a Password");
            return false;
        }
        return true;
    }
}
