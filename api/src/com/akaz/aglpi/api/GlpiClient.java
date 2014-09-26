package com.akaz.aglpi.api;

import java.util.HashMap;
import java.util.List;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;
import org.xmlrpc.android.XMLRPCFault;

import android.os.Handler;
import android.util.Log;

/**
 * Core class for the API
 */
public class GlpiClient {
	private XMLRPCClient		client;
	private GlpiClientConfig	config;
	private String				session;
	
	public GlpiClient(GlpiClientConfig config) {
		this.config = config;
		client = new XMLRPCClient(config.getWebserviceURI());
	}
	
	/**
	 * Calls the glpi.doLogin function. 
	 * @param user The user login
	 * @param password The user password
	 * @param callback {@link Callbacks.OnLogin}
	 */
	void doLogin(String user, String password, final Callbacks.OnLogin callback) {
		assert (callback != null);
		XMLRPCMethod method = new XMLRPCMethod("glpi.doLogin", new XMLRPCMethodCallback() {
			public void callFinished(Object result) {
			HashMap<String, String> data = (HashMap<String, String>)(result);
				session = data.get("session");
				if (session != null) {
					callback.ok(Integer.parseInt(data.get("id")), data.get("name"), data.get("realname"), data.get("firstname"), session);
				} else {
					callback.error("Session not received");
				}
			}
        });
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("login_name", user);
		params.put("login_password", password);
		method.call(new Object[]{(Object)params});
	}
	
	/**
	 * Log out of GLPI
	 */
	void doLogout() {
		XMLRPCMethod method = new XMLRPCMethod("glpi.doLogout", new XMLRPCMethodCallback() {
			public void callFinished(Object result) {
			HashMap<String, String> data = (HashMap<String, String>)(result);
				String message = data.get("message");
				if (message.startsWith("Bye"))
					; // OK
			}
        });
	}
	
	/**
	 * Retrieve a ticket
	 * @param ticketId The glpi id for the ticket
	 * @param callback {@link Callbacks.OnGetTicket}
	 */
	void getTicket(final int ticketId, final Callbacks.OnGetTicket callback) {
		assert (callback != null);
		XMLRPCMethod method = new XMLRPCMethod("glpi.getTicket", new XMLRPCMethodCallback() {
			public void callFinished(Object result) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> data = (HashMap<String, Object>)(result);
				if (data.get("id") != null) {
					try {
						Integer id = Integer.valueOf((String)data.get("id"));
						if (id.intValue() != -1)
							callback.ok(new Ticket(data));
						else
							callback.error("Could not get ticket with id " + ticketId);
					} catch (NumberFormatException e) {
						callback.error("Could not get ticket with id " + ticketId);
					}
				}
			}
        });
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("ticket", String.valueOf(ticketId));
		params.put("session", session);
		method.call(new Object[]{(Object)params});
	}
	
	/**
	 * Retrieve a computer
	 * @param computerId The glpi id of the computer to retrieve
	 * @param callback {@link Callbacks.OnGetComputer}
	 */
	void getComputer(final int computerId, final Callbacks.OnGetComputer callback) {
		assert (callback != null);
		XMLRPCMethod method = new XMLRPCMethod("glpi.getObject", new XMLRPCMethodCallback() {
			public void callFinished(Object result) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> data = (HashMap<String, Object>)(result);
				if (data.get("id") != null) {
					try {
						Integer id = Integer.valueOf((String)data.get("id"));
						if (id.intValue() != -1)
							callback.ok(new Computer(data));
						else
							callback.error("Could not get computer with id " + computerId);
					} catch (NumberFormatException e) {
						callback.error("Could not get computer with id " + computerId);
					}
				}
			}
        });
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", String.valueOf(computerId));
		params.put("itemtype", "Computer");
		params.put("session", session);
		method.call(new Object[]{(Object)params});
	}
	
	
	
	interface XMLRPCMethodCallback {
		void callFinished(Object result);
	}
	
	class XMLRPCMethod extends Thread {
		private String method;
		private Object[] params;
		private Handler handler;
		private XMLRPCMethodCallback callBack;
		public XMLRPCMethod(String method, XMLRPCMethodCallback callBack) {
			this.method = method;
			this.callBack = callBack;
			handler = new Handler();
		}
		public void call() {
			call(null);
		}
		public void call(Object[] params) {
			this.params = params;
			start();
		}
		@Override
		public void run() {
    		try {
    			final Object result = client.callEx(method, params);
    			handler.post(new Runnable() {
					public void run() {
						callBack.callFinished(result);
					}
    			});
    		} catch (final XMLRPCFault e) {
    			handler.post(new Runnable() {
					public void run() {
						Log.d("Test", "error", e);
					}
    			});
    		} catch (final XMLRPCException e) {
    			handler.post(new Runnable() {
					public void run() {
						Log.d("Test", "error", e);
					}
    			});
    		}
		}
	}
}
