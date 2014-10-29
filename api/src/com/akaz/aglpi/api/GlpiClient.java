package com.akaz.aglpi.api;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;
import org.xmlrpc.android.XMLRPCFault;

import android.graphics.pdf.PdfDocument.Page;
import android.os.Handler;
import android.util.Log;

/**
 * Core class for the API
 */
public class GlpiClient {
	private XMLRPCClient				client;
	private GlpiClientConfig			config;
	private String						session;
	private HashMap<Integer, String>	computerModels;
	private HashMap<Integer, String>	manufacturers;
	private HashMap<Integer, String>	locations;
	
	public static final String			TICKET_STATUS_NEW = "1";
	public static final String			TICKET_STATUS_ASSIGN = "2";
	
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
	public void doLogin(String user, String password, final Callbacks.OnLogin callback) {
		assert (callback != null);
		XMLRPCMethod method = new XMLRPCMethod("glpi.doLogin", new XMLRPCMethodCallback() {
			public void callFinished(Object result) {
			HashMap<String, String> data = (HashMap<String, String>)(result);
				session = data.get("session");
				if (session != null) {
					setupDropdowns();
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
	 * @return True if the client is logged in
	 */
	public boolean isLoggedIn() {
		return session != null;
	}
	
	/**
	 * Log out of GLPI
	 */
	public void doLogout() {
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
	public void getTicket(final int ticketId, final Callbacks.OnGetTicket callback) {
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
	 * Liste tous les tickets selon les paramètres définis
	 */
	public void listTickets(final Pagination pagination, final String orderBy, final String status, final Callbacks.OnListTickets callback) {
		final List<Ticket> tickets = new LinkedList<Ticket>();
		XMLRPCMethod method = new XMLRPCMethod("glpi.listTickets", new XMLRPCMethodCallback() {
			public void callFinished(Object result) {
				/*
				HashMap<String, Object>[] data = (HashMap<String, Object>[])(result);
				for (HashMap<String, Object> row : data) {
					tickets.add(new Ticket(row));
				}
				*/
				for (Object row : (Object[])result) {
					HashMap<String, Object> mapRow = (HashMap<String, Object>)row;
					tickets.add(new Ticket(mapRow));
				}
				if (tickets.size() != 0)
					callback.ok(tickets);
				else
					callback.error("No tickets received");
			}
        });
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("order", orderBy);
		params.put("status", status);
		params.put("id2name", "true");
		if (pagination.getLimit() != 1) {
			params.put("start", String.valueOf(pagination.getStart()));
			params.put("limit", String.valueOf(pagination.getLimit()));
		}
		params.put("session", session);
		method.call(new Object[]{(Object)params});
	}
	
	/**
	 * Retrieve a computer
	 * @param computerId The glpi id of the computer to retrieve
	 * @param callback {@link Callbacks.OnGetComputer}
	 */
	public void getComputer(final int computerId, final Callbacks.OnGetComputer callback) {
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

	/**
	 * Retrieve a list of computers
	 */
	public void listComputers(final Pagination pagination, final Callbacks.OnListComputers callback) {
		final List<Computer> computers = new LinkedList<Computer>();
		XMLRPCMethod method = new XMLRPCMethod("glpi.listObjects", new XMLRPCMethodCallback() {
			public void callFinished(Object result) {
				@SuppressWarnings("unchecked")
				Object[] data = (Object[])(result);
				for (Object row : data) {
					computers.add(new Computer((HashMap<String, Object>) row));
				}
				if (computers.size() != 0)
					callback.ok(computers);
				else
					callback.error("No computers received");
			}
        });
		HashMap<String, String> params = new HashMap<String, String>();
		if (pagination.getLimit() != 1) {
			params.put("start", String.valueOf(pagination.getStart()));
			params.put("limit", String.valueOf(pagination.getLimit()));
		}
		params.put("itemtype", "Computer");
		params.put("session", session);
		method.call(new Object[]{(Object)params});
	}
	
	public void setupDropdowns() {
		setupComputerModelDropdown();
	}
	
	private void setupComputerModelDropdown() {
		computerModels = new HashMap<Integer, String>();
		XMLRPCMethod method = new XMLRPCMethod("glpi.listDropdownValues", new XMLRPCMethodCallback() {
			public void callFinished(Object result) {
				@SuppressWarnings("unchecked")
				Object[] data = (Object[])(result);
				for (Object row : data) {
					HashMap<String, String> r = (HashMap<String, String>) row;
					computerModels.put(Integer.valueOf(r.get("id")), r.get("name"));
				}
				if (computerModels.size() != 0) 
					Log.d("OK", "Downdown ComputerModels OK");
				else
					Log.d("NOK", "No ComputerModels Downdown received");
				setupComputerManufacturerDropdown();
			}
        });
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("dropdown", "ComputerModel");
		params.put("session", session);
		method.call(new Object[]{(Object)params});
	}
	
	public HashMap<Integer, String> getComputerModelDropdown() {
		return this.computerModels;
	}
	
	private void setupComputerManufacturerDropdown() {
		manufacturers = new HashMap<Integer, String>();
		XMLRPCMethod method = new XMLRPCMethod("glpi.listDropdownValues", new XMLRPCMethodCallback() {
			public void callFinished(Object result) {
				@SuppressWarnings("unchecked")
				Object[] data = (Object[])(result);
				for (Object row : data) {
					HashMap<String, String> r = (HashMap<String, String>) row;
					manufacturers.put(Integer.valueOf(r.get("id")), r.get("name"));
				}
				if (manufacturers.size() != 0)
					Log.d("OK", "Downdown Manufacturer OK");
				else
					Log.d("NOK", "No Manufacturer Downdown received");
				setupLocationDropdown();
			}
        });
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("dropdown", "Manufacturer");
		params.put("session", session);
		method.call(new Object[]{(Object)params});
	}
	
	public HashMap<Integer, String> getManufacturerDropdown() {
		return this.manufacturers;
	}
	
	private void setupLocationDropdown() {
		locations = new HashMap<Integer, String>();
		XMLRPCMethod method = new XMLRPCMethod("glpi.listDropdownValues", new XMLRPCMethodCallback() {
			public void callFinished(Object result) {
				@SuppressWarnings("unchecked")
				Object[] data = (Object[])(result);
				for (Object row : data) {
					HashMap<String, String> r = (HashMap<String, String>) row;
					try {
						locations.put(Integer.valueOf(r.get("id")), new String(r.get("name").getBytes("iso8859-1"), "UTF-8"));
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (locations.size() != 0)
					Log.d("OK", "Downdown Locations OK");
				else
					Log.d("NOK", "No Locations Downdown received");
			}
        });
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("dropdown", "Location");
		params.put("session", session);
		method.call(new Object[]{(Object)params});
	}
	
	public HashMap<Integer, String> getLocationDropdown() {
		return this.locations;
	}
	
	public interface XMLRPCMethodCallback {
		void callFinished(Object result);
	}
	
	public class XMLRPCMethod extends Thread {
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
