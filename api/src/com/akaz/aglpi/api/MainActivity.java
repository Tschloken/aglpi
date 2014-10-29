package com.akaz.aglpi.api;

import java.util.List;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.akaz.aglpi.api.Callbacks.OnListComputers;

public class MainActivity extends ActionBarActivity {
    GlpiClient		glpi;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        glpi = new GlpiClient(new GlpiClientConfig().setURI("http://192.168.1.2/glpi/plugins/webservices/xmlrpc.php"));
        glpi.doLogin("glpi", "glpi", new Callbacks.OnLogin() {
			@Override
			public void ok(int id, String name, String realname, String firstname, String session) {
				Log.d(GlpiClient.class.getName(), "Logged in as : " + firstname + " " + realname);
				glpi.listComputers(new Pagination(9999), new OnListComputers() {
					@Override
					public void ok(List<Computer> computers) {
						for (Computer computer : computers) {
							Log.d(GlpiClient.class.getName(), computer.get("name") != null ? (String) computer.get("name") : "");
						}
					}
					
					@Override
					public void error(String what) {
						Log.d(GlpiClient.class.getName(), "error");
					}
				});
			}
			
			@Override
			public void error(String what) {
			}
		});
        /*
        glpi.listComputers(new Pagination(10), new OnListComputers() {
			@Override
			public void ok(List<Computer> computers) {
				for (Computer computer : computers) {
					Log.d(GlpiClient.class.getName(), (String) computer.get("name"));
				}
			}
			
			@Override
			public void error(String what) {
				Log.d(GlpiClient.class.getName(), "error");
			}
		});
		*/
    }
}
