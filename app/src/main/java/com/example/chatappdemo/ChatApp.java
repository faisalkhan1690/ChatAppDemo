package com.example.chatappdemo;


import android.content.Context;
import android.util.Log;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smack.packet.Presence.Type;
import java.io.IOException;
import java.util.Collection;


public class ChatApp {

	private static ChatApp instance = null;
	public static final String HOST = "192.168.1.64";
	public static final int PORT = 5222;
	public static final String SERVICE = "xmpp.deepco.com.br";
    private Context context;
    public AbstractXMPPConnection connection;


    /*
    private constructor for making sin
     */
	private ChatApp() {

	}

	public static ChatApp getInstance() {
		if (instance == null) {
			instance = new ChatApp();
		}
		return instance;
	}


    /**
     * Making connection with xmpp server
     * @param context
     * @return true if connection established else return false
     */
    public boolean connect(Context context) {
        this.context = context;

        if(connection==null){
            XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
            configBuilder.setHost(HOST);
            configBuilder.setPort(PORT);
            configBuilder.setServiceName(SERVICE);
            configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
            configBuilder.setCompressionEnabled(false);
            connection = new XMPPTCPConnection(configBuilder.build());
            try {
                connection.connect();
                return true;
            } catch (SmackException | IOException | XMPPException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     *Login on server
     * @param username
     * @param password
     * @return true if login successfully else return false
     */
    public boolean login(String username,String password) {

        try {
            connection.login(username, password);
            return true;
        } catch (SmackException | IOException | XMPPException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *Registration on server
     * @param username
     * @param password
     * @return true if login successfully else return false
     */
    public boolean Registration(String username,String password) {
        AccountManager accountManager = AccountManager.getInstance(connection);
        try {
            accountManager.createAccount(username,password);
            return true;
        } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Return Friend list
     *
     * @return
     */
    public Collection<RosterEntry> getFriendList() {
        Roster roster = Roster.getInstanceFor(connection);
        Collection<RosterEntry> entries = roster.getEntries();
        for (RosterEntry entry : entries) {
            Log.d("XMPPChatDemoActivity",
                    "--------------------------------------");
            Log.d("XMPPChatDemoActivity", "RosterEntry " + entry);
            Log.d("XMPPChatDemoActivity", "User: " + entry.getUser());
            Log.d("XMPPChatDemoActivity", "Name: " + entry.getName());
            Log.d("XMPPChatDemoActivity", "Status: " + entry.getStatus());
            Log.d("XMPPChatDemoActivity", "Type: " + entry.getType());
            Presence entryPresence = roster.getPresence(entry.getUser());

            Log.d("XMPPChatDemoActivity",
                    "Presence Status: " + entryPresence.getStatus());
            Log.d("XMPPChatDemoActivity",
                    "Presence Type: " + entryPresence.getType());
            Presence.Type type = entryPresence.getType();
            if (type == Presence.Type.available)
                Log.d("XMPPChatDemoActivity", "Presence AVIALABLE");
            Log.d("XMPPChatDemoActivity", "Presence : " + entryPresence);

        }
        return entries;
    }

    /**
     *
     * @param userCompleteAddress
     * @return user online or offline.
     */

    public Type getStatusOfUser(String userCompleteAddress) {

        Roster roster = Roster.getInstanceFor(connection);
        Collection<RosterEntry> entries = roster.getEntries();
        for (RosterEntry entry : entries) {
            if (entry.getUser().equals(userCompleteAddress)) {
                Presence entryPresence = roster.getPresence(entry.getUser());
                Presence.Type type = entryPresence.getType();
                return type;
            }
        }
        return null;
    }

}
