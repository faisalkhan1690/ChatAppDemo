package com.example.chatappdemo;


import android.content.Context;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;

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
    public boolean login(final String username, final String password) {

        try {
            connection.login(username, password);
            return true;
        } catch (SmackException | IOException | XMPPException e) {
            e.printStackTrace();
            return false;
        }
    }

}
