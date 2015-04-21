package com.example.chatappdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.iqlast.LastActivityManager;
import org.jivesoftware.smackx.iqlast.packet.LastActivity;

import java.io.IOException;

import static org.jivesoftware.smackx.iqlast.LastActivityManager.*;


public class HomeActivity extends Activity {
    private TextView tvFriend;
    private TextView tvGroup;
    private RelativeLayout rlLogout;
    private ChatApp chatApp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        chatApp = ChatApp.getInstance();


        tvFriend = (TextView)findViewById(R.id.tvFriend);
        tvFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FriendListActivity.class);
                startActivity(intent);
            }
        });
        tvGroup = (TextView)findViewById(R.id.tvGroup);
        tvGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, GroupActivity.class);
                startActivity(intent);
            }
        });
        /**
         * click on logout
         */
        rlLogout = (RelativeLayout)findViewById(R.id.rlLogout);
        rlLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Presence pr=new Presence(Presence.Type.unavailable);
                pr.setStanzaId(chatApp.getUsername());
                try {
                    chatApp.connection.sendStanza(pr);
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }

                chatApp.connection.disconnect();
                Intent intent = new Intent(HomeActivity.this, SplashScreenActvity.class);
                startActivity(intent);
            }
        });
    }
}
