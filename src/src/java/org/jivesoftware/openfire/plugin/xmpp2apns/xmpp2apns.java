package org.jivesoftware.openfire.plugin;

import org.apache.commons.httpclient.*; 
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;

import org.jivesoftware.openfire.MessageRouter;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.session.Session;
import org.jivesoftware.openfire.handler.IQHandler;
import org.jivesoftware.openfire.IQRouter;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;

import org.jivesoftware.database.DbConnectionManager;
import org.jivesoftware.database.SequenceManager;
import org.jivesoftware.util.NotFoundException;

import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;
import org.xmpp.packet.Presence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;


public class xmpp2apns implements Plugin, PacketInterceptor {
	
	private static final Logger Log = LoggerFactory.getLogger(xmpp2apns.class);
	
    private InterceptorManager interceptorManager;
    
    private xmpp2apnsDBHandler dbManager;
    
    public xmpp2apns() {
        interceptorManager = InterceptorManager.getInstance();
        dbManager = new xmpp2apnsDBHandler();       
    }
    
	public void initializePlugin(PluginManager pManager, File pluginDirectory) {		
        // 메시지를 가로채기 위한 인터셉터 매니저 등록
        interceptorManager.addInterceptor(this);
        
        IQHandler myHandler = new xmpp2apnsIQHandler();
        IQRouter iqRouter = XMPPServer.getInstance().getIQRouter();       
        iqRouter.addHandler(myHandler);
    }
	
	public void destroyPlugin() {
        // 인터셉터 매니저 해제
        interceptorManager.removeInterceptor(this);
    }
	
	public void interceptPacket(Packet packet, Session session, boolean read, boolean processed) throws PacketRejectedException {
		
		if(isValidTargetPacket(packet,read,processed)) {
			Packet original = packet;			
						
			if(original instanceof Message) {
				Message receivedMessage = (Message)original;
				
				JID targetJID = receivedMessage.getTo();
				
				String targetJID_Bare = targetJID.toBareJID();				
				String body = receivedMessage.getBody();
			
				String[] userID = targetJID_Bare.split("@");
				if( userID[0] == null ) userID[0] = "누군가";
				
				String payloadString = userID[0];
				payloadString = payloadString.concat(": ");
				payloadString = payloadString.concat(body);
				
				String deviceToken = dbManager.getDeviceToken(targetJID);
				if(deviceToken == null) return;
				
				pushMessage message = new pushMessage(payloadString, 1, "Default.caf", "/usr/local/openfire/directxmpp.p12", "123789", false, deviceToken);
				message.start();
				
			}			
			
		}	
		
	
	}
	
	private boolean isValidTargetPacket(Packet packet, boolean read, boolean processed) {
        return  !processed && read && packet instanceof Message;
    }
}