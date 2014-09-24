package com.akaz.aglpi.api;

public class Callbacks {

	/**
	 * Callback for GlpiClient.doLogin
	 */
	public interface OnLogin {
		/**
		 * Called when the result is OK.
		 */
		public void ok(int id, String name, String realname, String firstname, String session);
		/**
		 * Called chen there is an error
		 */
		public void error(String what);
	}
}
