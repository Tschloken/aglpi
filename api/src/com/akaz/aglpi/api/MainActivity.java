package com.akaz.aglpi.api;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {
    GlpiClient		glpi;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        glpi = new GlpiClient(new GlpiClientConfig().setURI("http://192.168.1.7/glpi/plugins/webservices/xmlrpc.php"));
        glpi.doLogin("glpi", "glpi", new Callbacks.OnLogin() {
			@Override
			public void ok(int id, String name, String realname, String firstname, String session) {
				Log.d(GlpiClient.class.getName(), "Logged in as : " + firstname + " " + realname);
			}
			
			@Override
			public void error(String what) {
			}
		});
    }
}
