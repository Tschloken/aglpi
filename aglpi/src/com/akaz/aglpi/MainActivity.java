package com.akaz.aglpi;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.akaz.aglpi.api.Callbacks;
import com.akaz.aglpi.api.GlpiClient;
import com.akaz.aglpi.api.GlpiClientConfig;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
	public static final int SECTION_COMPUTERS_LIST = 0;
	public static final int SECTION_TICKETS_LIST = 1;
	static GlpiClient	glpi = null;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        glpi = new GlpiClient(new GlpiClientConfig().setURI("http://192.168.1.3/glpi/plugins/webservices/xmlrpc.php"));
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

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
    	if (position == SECTION_COMPUTERS_LIST) {
	        fragmentManager.beginTransaction()
	                .replace(R.id.container, new ComputerListFragment())
	                .commit();
    	} else if (position == SECTION_TICKETS_LIST) {
	        fragmentManager.beginTransaction()
	                .replace(R.id.container, new TicketListFragment())
	                .commit();
    	}
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case SECTION_COMPUTERS_LIST:
                mTitle = getString(R.string.section_computers);
                break;
            case SECTION_TICKETS_LIST:
                mTitle = getString(R.string.section_tickets);
                break;
                /*
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
                */
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            //getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
