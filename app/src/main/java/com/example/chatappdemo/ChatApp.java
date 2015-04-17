package com.example.chatappdemo;


import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.packet.RosterPacket;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smack.packet.Presence.Type;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class ChatApp {

    private static ChatApp instance = null;
    public static final String HOST = "192.168.1.64";
    public static final int PORT = 5222;
    public static final String SERVICE = "xmpp.deepco.com.br";
    private Context context;
    public AbstractXMPPConnection connection;
    private String username;
    private MessageRcd msgrcd;
    private Handler mHandler = new Handler();


    public String getUsername() {
        return username;
    }

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
            this.username = connection.getUser();
            Presence presence = new Presence(Presence.Type.available);
            connection.sendPacket(presence);
            setConnection(connection);
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

    /**
     * Send message to the specified address
     *
     * @param to
     * @param message
     */
    public void sendMessage(String to, String message, String memberChat) {
        Message msg = null;
        msg = new Message(to, Message.Type.chat);
        msg.setBody(message);
        if (connection != null) {
            try {
                connection.sendStanza(msg);
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Called by Settings dialog when a connection is establised with the XMPP
     * server
     *
     * @param connection
     */
    @SuppressWarnings("deprecation")
    public void setConnection(AbstractXMPPConnection connection) {
        this.connection = connection;
        if (connection != null) {

            StanzaFilter filter = new StanzaTypeFilter(Message.class);
            connection.addPacketListener(new StanzaListener() {
                @Override
                public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                    final Message message = (Message) packet;
                    if (message.getBody() != null) {
                        String fromName = message.getFrom();
                        Log.i("XMPPChatDemoActivity", "Text Recieved "
                                + message.getBody() + " from " + fromName);
                        //messages.add(fromName + ":");
                        message.setStanzaId(fromName + ":");
                        //messages.add(message.getBody());
                        message.setStanzaId(message.getBody());
                        // Add the incoming message to the list view
                        playBeep();
                        mHandler.post(new Runnable() {
                            public void run() {
                                // TODO check split
                                msgrcd.onMessageReceived((message.getFrom()
                                        .split("@"))[0].toString()
                                        + ":"
                                        + message.getBody());
                            }
                        });
                    }

                }
            }, filter);

        }
    }

    /**
     * Plays device's default notification sound
     */
    public void playBeep() {

        try {
            Uri notification = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<RosterEntry> getAllFriendRequestsSend() {
        List<RosterEntry> mFriendRequests = new ArrayList<RosterEntry>();
        Roster roster = Roster.getInstanceFor(instance.connection);

        Collection<RosterEntry> entries = roster.getEntries();
        for (RosterEntry entry : entries) {
            if(entry.getType().toString().equals("to") && entry.getStatus()==null){
                mFriendRequests.add(entry);
            }
        }
        return mFriendRequests;
    }


    public interface MessageRcd {
        public void onMessageReceived(String message);
    }

    public void initializeListener(MessageRcd rcd) {
        this.msgrcd = rcd;
    }


    /**
     * For Friend Request send
     * @param friendId
     * @return true if success else failed
     */
    public boolean sendFriendRequest(String friendId){
        Roster roster = Roster.getInstanceFor(instance.connection);

        if (!roster.contains(friendId)) {

            try {
                roster.createEntry(friendId, friendId, null);
                Presence subscribe = new Presence(Presence.Type.subscribe);
                subscribe.setTo(friendId);
                subscribe.setFrom(friendId);
                instance.connection.sendStanza(subscribe);
                return true;

            } catch (XMPPException.XMPPErrorException | SmackException.NotLoggedInException |
                    SmackException.NoResponseException | SmackException.NotConnectedException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }


    public List<RosterEntry> getAllFriendRequestsReceive(){
        List<RosterEntry> mFriendRequests = new ArrayList<RosterEntry>();
        Roster roster = Roster.getInstanceFor(instance.connection);

        Collection<RosterEntry> entries = roster.getEntries();
        for (RosterEntry entry : entries) {
            if(entry.getType().toString().equals("from") && entry.getName()==null){
                mFriendRequests.add(entry);
            }
        }
        return mFriendRequests;
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
            Presence entryPresence = roster.getPresence(entry.getUser());
            Presence.Type type = entryPresence.getType();
        }
        return entries;
    }


    public boolean acceptFriendRequest(String mFriendName){

        Roster roster = Roster.getInstanceFor(instance.connection);

        try {
            roster.createEntry(mFriendName, mFriendName, null);
            Presence subscribed = new Presence(Type.subscribed);
            subscribed.setTo(mFriendName);
            instance.connection.sendStanza(subscribed);
            return true;
        } catch (SmackException.NotLoggedInException | SmackException.NoResponseException |
                XMPPException.XMPPErrorException | SmackException.NotConnectedException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean rejectFriendRequest(String mFriendName){
        Roster roster = Roster.getInstanceFor(instance.connection);
       try {
           roster.createEntry(mFriendName, mFriendName, null);
           Presence unsubscribe = new Presence(Type.unsubscribe);
           unsubscribe.setTo(mFriendName);
           unsubscribe.setFrom(mFriendName);
           instance.connection.sendStanza(unsubscribe);
           return true;

        }catch (SmackException.NotLoggedInException | SmackException.NoResponseException |
                XMPPException.XMPPErrorException | SmackException.NotConnectedException e) {
            e.printStackTrace();
            return false;
        }

    }


    public boolean cancelFriendRequest(String mFriendName) {
        Roster roster = Roster.getInstanceFor(instance.connection);
        try {
            roster.createEntry(mFriendName, mFriendName, null);
            Presence unsubscribe = new Presence(Type.unsubscribe);
            unsubscribe.setTo(mFriendName);
            unsubscribe.setFrom(mFriendName);
            instance.connection.sendStanza(unsubscribe);
            return true;

        }catch (SmackException.NotLoggedInException | SmackException.NoResponseException |
                XMPPException.XMPPErrorException | SmackException.NotConnectedException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<RosterEntry> getAllFriendList(){
        ArrayList<RosterEntry> friendList = new ArrayList<RosterEntry>();
        Roster roster = Roster.getInstanceFor(instance.connection);

        Collection<RosterEntry> entries = roster.getEntries();
        for (RosterEntry entry : entries) {
            if(entry.getType().toString().equals("both"))
            friendList.add(entry);
        }
        return friendList;
    }


}
