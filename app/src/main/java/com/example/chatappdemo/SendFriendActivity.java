package com.example.chatappdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


public class SendFriendActivity extends Activity implements View.OnClickListener {

    private EditText etNameOfFriend;
    private Button btnSend;
    private ListView lvPendingRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_friend);

        etNameOfFriend=(EditText)findViewById(R.id.etNameOfFriend);
        btnSend=(Button)findViewById(R.id.btnSend);
        lvPendingRequest=(ListView)findViewById(R.id.lvPendingRequest);

        btnSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSend:
                if(sendFriendRequest()){
                    Toast.makeText(getApplicationContext(),"Send",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(),"Not Send try again",Toast.LENGTH_LONG).show();
                }

                break;
            default:
                break;
        }
    }

    private boolean  sendFriendRequest() {

        return false;
    }
}
