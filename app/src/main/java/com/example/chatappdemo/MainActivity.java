package com.example.chatappdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.net.URL;


public class MainActivity extends Activity implements View.OnClickListener {

    private EditText etUserName;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegister;
    private ChatApp chatApp;
    private String mUserName = "";
    private String mPassword = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialization();

        //get the chatapp class object
        chatApp = ChatApp.getInstance();

        //making connection
        new SetConnection().execute();
    }

    /**
     * initialise views
     */
    private void initialization() {
        etUserName = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPwd);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                mUserName = etUserName.getText().toString().trim();
                mPassword = etPassword.getText().toString().trim();

                if (checkValidation()) {
                    new Login().execute();
                }
                break;

            case R.id.btnRegister:

                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    /**
     * asyntask to connect with xmpp server
     */
    private class SetConnection extends AsyncTask<URL, Integer, Boolean> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(MainActivity.this, "Connecting...", "Please wait...", false);
            dialog.show();
        }

        /**
         * @param urls
         * @return true if connection success else return false
         */
        @Override
        protected Boolean doInBackground(URL... urls) {

            return chatApp.connect(getApplicationContext());
        }

        @Override
        protected void onPostExecute(Boolean result) {
            dialog.dismiss();
            if (result) {
                Toast.makeText(getApplicationContext(), "Connect", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Did't Connect", Toast.LENGTH_LONG).show();
            }
        }
    }


    private class Login extends AsyncTask<URL, Integer, Boolean> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(MainActivity.this, "Login...", "Please wait...", false);
            dialog.show();
        }

        /**
         * @param urls
         * @return true if connection success else return false
         */
        @Override
        protected Boolean doInBackground(URL... urls) {
            return chatApp.login(mUserName,mPassword );
        }

        @Override
        protected void onPostExecute(Boolean result) {
            dialog.dismiss();
            if (result) {

                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG).show();
            }
        }
    }


    private boolean checkValidation(){

        if(mUserName.equals("")){
            etUserName.setError("Enter UserName");
            return false;
        }

        if(mPassword.equals("")){
            etPassword.setError("Enter Password");
            return false;
        }
        return true;
    }
}
