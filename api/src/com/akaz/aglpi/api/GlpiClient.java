package com.akaz.aglpi.api;

import java.util.HashMap;

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
	 * @param callback @see {@link Callbacks.OnLogin}
	 */
	void doLogin(String user, String password, final Callbacks.OnLogin callback) {
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
