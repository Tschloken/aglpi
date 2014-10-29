package com.akaz.aglpi;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.akaz.aglpi.api.Callbacks;
import com.akaz.aglpi.api.Computer;
import com.akaz.aglpi.api.GlpiClient;
import com.akaz.aglpi.api.Pagination;
import com.akaz.aglpi.api.Callbacks.OnListComputers;

/**
 * A placeholder fragment containing a simple view.
 */
public class ComputerListFragment extends Fragment {
    public ComputerListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	if (MainActivity.glpi.isLoggedIn()) {
    		final View rootView = inflater.inflate(R.layout.fragment_computers, container, false);
			MainActivity.glpi.listComputers(new Pagination(9999), new OnListComputers() {
				@Override
				public void ok(final List<Computer> computers) {
					final List<String> computerNames = new ArrayList<String>();
					for (Computer computer : computers) {
						computerNames.add(computer.get("name") != null ? (String) computer.get("name") : "");
					}
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
			        	   // ArrayAdapter<String> adapter = new ArrayAdapter<String>(PlaceholderFragment.this.getActivity(), R.layout.computers_row, R.id.label, computerNames.toArray(new String[]{}));
			        	    ComputerListAdapter cadapter = new ComputerListAdapter(ComputerListFragment.this.getActivity(), computers);
			        	    ((ListView)rootView.findViewById(R.id.computersListView)).setAdapter(cadapter);
			        	    ((ListView)rootView.findViewById(R.id.computersListView)).invalidate();
			        	    ((ListView)rootView.findViewById(R.id.computersListView)).setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
									launchComputerScreen(computers.get(position).get("id").toString());
								}
							});
						}
					});
				}
				
				@Override
				public void error(String what) {
					Log.d(GlpiClient.class.getName(), "error");
				}
			});
			return rootView;
    	}
    	return null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(MainActivity.SECTION_COMPUTERS_LIST);
    }
    
    public void launchComputerScreen(String computerId) {
    	MainActivity.glpi.getComputer(Integer.parseInt(computerId), new Callbacks.OnGetComputer() {
			@Override
			public void ok(Computer computer) {
				FragmentManager fmanager = getFragmentManager();
				android.support.v4.app.FragmentTransaction ft = fmanager.beginTransaction();
				ft.replace(R.id.container, new ComputerFragment(computer, MainActivity.glpi));
				ft.addToBackStack(null);
				ft.commit();
			}
			
			@Override
			public void error(String what) {
			}
		});
    }
}