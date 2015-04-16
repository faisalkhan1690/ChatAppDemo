package com.example.chatappdemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.chatappdemo.Adapter.FriendRequestAdapter;

import org.jivesoftware.smack.roster.RosterEntry;

import java.util.List;

public class ReceiveFriendRequestActivity extends Activity {

    private ListView lvRequestList;
    private List<RosterEntry> mFriendRequestList;
    private ChatApp chatApp;
    private FriendRequestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);

        lvRequestList=(ListView)findViewById(R.id.lvRequestList);
        chatApp = ChatApp.getInstance();
        showAllFriendRequest();
    }

    private void showAllFriendRequest() {

        mFriendRequestList=chatApp.getAllFriendRequests();
        adapter = new FriendRequestAdapter(ReceiveFriendRequestActivity.this, mFriendRequestList);
        lvRequestList.setAdapter(adapter);
    }
}
