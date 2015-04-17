package com.example.chatappdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterGroup;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
import org.jivesoftware.smackx.xdata.packet.DataForm;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class GroupActivity extends Activity {
    private EditText etGroupName;
    private Button btnCreateGroup;
    private ChatApp chatApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        chatApp = ChatApp.getInstance();

        //Roster roster = Roster.getInstanceFor(chatApp.connection);
        //Collection<RosterGroup> groups = roster.getGroups();
        MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(chatApp.connection);
        Set<String> strings = manager.getJoinedRooms();
        try {
            List<HostedRoom> hostedRooms = manager.getHostedRooms(ChatApp.SERVICE);
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }


        etGroupName = (EditText) findViewById(R.id.etGroupName);
        btnCreateGroup = (Button) findViewById(R.id.btnCreateGroup);
        /**
         * Click create group button
         */
        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("Group Name",""+etGroupName.getText().toString());
                createGroup();
            }
        });
    }

    private void createGroup() {


        //http://www.igniterealtime.org/builds/smack/docs/latest/documentation/extensions/muc.html

        // Get the MultiUserChatManager
        MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(chatApp.connection);
        // Get a MultiUserChat using MultiUserChatManager
        MultiUserChat muc = manager.getMultiUserChat("myroom" + "@conference." + ChatApp.SERVICE);
        // Create the room
        try {
            muc.create("vinay");
            muc.join("vinay");
            muc.invite("vinay@xmpp.deepco.com.br/Good", "Meet me in this excellent room");
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException e) {
            e.printStackTrace();
        }
        // Send an empty room configuration form which indicates that we want
        // an instant room
        try {
            muc.sendConfigurationForm(new Form(DataForm.Type.submit));
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group, menu);
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
}
