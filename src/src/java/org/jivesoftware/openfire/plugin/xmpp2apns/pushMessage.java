package org.jivesoftware.openfire.plugin;

import javapns.*;

class pushMessage extends Thread {

	String message;
	int badge;
	String sound;
	Object keystore;
	String password;
	boolean production;
	String token;
	
	pushMessage(String message, int badge, String sound, Object keystore, String password, boolean production, String token ) {

	this.message = message;
	this.badge = badge;
	this.sound = sound;
	this.keystore = keystore;
	this.password = password;
	this.production = production;
	this.token = token;
	
	}

	public void run() {
		Push.combined(message, badge, sound, keystore, password, production, token);
	}

}