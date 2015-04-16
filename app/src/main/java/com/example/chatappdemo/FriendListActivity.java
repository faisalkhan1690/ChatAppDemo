package com.example.chatappdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.chatappdemo.Adapter.FriendListAdapter;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.ArrayList;
import java.util.Collection;


public class FriendListActivity extends Activity implements View.OnClickListener {
    private ListView lvFriendList;
    private ChatApp chatApp;
    private ArrayList<RosterEntry> friendList = new ArrayList<RosterEntry>();
    private FriendListAdapter friendListAdapter;
    private TextView tvSendFriend;
    private  TextView tvPendingFriend;
    private TextView tvSentFriend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        tvSendFriend = (TextView) findViewById(R.id.tvSendFriend);
        tvPendingFriend = (TextView) findViewById(R.id.tvPendingFriend);
        tvSentFriend = (TextView) findViewById(R.id.tvSentFriend);

        chatApp = ChatApp.getInstance();
        Roster roster = Roster.getInstanceFor(chatApp.connection);

        Collection<RosterEntry> entries = roster.getEntries();
        for (RosterEntry entry : entries) {
            //System.out.println("Here: " + entry);
          /*  System.out.println("Here: " +  roster.getPresence(entry.getUser()));
            Log.i("userPresence", ""+roster.getPresence(entry.getUser()));
            Presence entryPresence = roster.getPresence(entry.getUser());
            Presence.Type type = entryPresence.getType();
            System.out.println("type: " +  type);
            Log.i("userPresence", ""+roster.getPresence(entry.getUser()));*/
            //roster.getPresence(entry.getUser());
            friendList.add(entry);

        }

        lvFriendList = (ListView) findViewById(R.id.lvFriendList);
        friendListAdapter = new FriendListAdapter(FriendListActivity.this, friendList, roster);
        lvFriendList.setAdapter(friendListAdapter);

        lvFriendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FriendListActivity.this, XMPPChatDemoActivity.class);
                intent.putExtra("email", friendList.get(position).getUser().toString());
                startActivity(intent);
            }
        });


        /**
         * click on tab button
         */

        tvSendFriend.setOnClickListener(this);
        tvPendingFriend.setOnClickListener(this);
        tvSentFriend.setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friend_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSendFriend: {
                Intent intent = new Intent(FriendListActivity.this, SendFriendActivity.class);
                startActivity(intent);
            }

            break;
            case R.id.tvPendingFriend: {

                Intent intent = new Intent(FriendListActivity.this, PendingFriendActivity.class);
                startActivity(intent);
            }
            case R.id.tvSentFriend: {

                Intent intent = new Intent(FriendListActivity.this, FriendRequestSentActivity.class);
                startActivity(intent);
            }
            break;

            default:
                break;
        }
    }
}