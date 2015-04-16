package com.example.chatappdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.util.StringUtils;


public class SendFriendActivity extends Activity implements View.OnClickListener {

    private EditText etNameOfFriend;
    private Button btnSend;
    private ListView lvPendingRequest;
    private String mFriendId = "";
    private ChatApp chatApp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_friend);

        etNameOfFriend = (EditText) findViewById(R.id.etNameOfFriend);
        btnSend = (Button) findViewById(R.id.btnSend);
        lvPendingRequest = (ListView) findViewById(R.id.lvPendingRequest);

        btnSend.setOnClickListener(this);

        etNameOfFriend.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    etNameOfFriend.setError(null);
                }
                return false;
            }
        });
        chatApp = ChatApp.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSend:

                if (etNameOfFriend.getText().toString().trim().equals("")) {
                    etNameOfFriend.setError("Please Enter Id");
                    return;
                }

                if (sendRequest()) {
                    Toast.makeText(getApplicationContext(), "Send", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Not Send try again", Toast.LENGTH_LONG).show();
                }

                break;
            default:
                break;
        }
    }


    private boolean sendRequest() {

        mFriendId = etNameOfFriend.getText().toString().trim() + "@" + ChatApp.SERVICE;
        return chatApp.sendFriendRequest(mFriendId);

    }
}