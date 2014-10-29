package com.akaz.aglpi;

import java.util.ArrayList;
import java.util.List;

import com.akaz.aglpi.api.Callbacks.OnListTickets;
import com.akaz.aglpi.api.Computer;
import com.akaz.aglpi.api.GlpiClient;
import com.akaz.aglpi.api.Pagination;
import com.akaz.aglpi.api.Callbacks.OnListComputers;
import com.akaz.aglpi.api.Ticket;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class TicketListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (MainActivity.glpi.isLoggedIn()) {
    		final View rootView = inflater.inflate(R.layout.fragment_tickets, container, false);
			MainActivity.glpi.listTickets(new Pagination(50), "date_mod", GlpiClient.TICKET_STATUS_ASSIGN, new OnListTickets() {
				@Override
				public void ok(final List<Ticket> tickets) {
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
			        	   // ArrayAdapter<String> adapter = new ArrayAdapter<String>(PlaceholderFragment.this.getActivity(), R.layout.computers_row, R.id.label, computerNames.toArray(new String[]{}));
			        	    TicketListAdapter adapter = new TicketListAdapter(TicketListFragment.this.getActivity(), tickets);
			        	    ((ListView)rootView.findViewById(R.id.ticketsListView)).setAdapter(adapter);
			        	    ((ListView)rootView.findViewById(R.id.ticketsListView)).invalidate();
			        	    ((ListView)rootView.findViewById(R.id.ticketsListView)).setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
									//launchComputerScreen(computers.get(position).get("id").toString());
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
        ((MainActivity) activity).onSectionAttached(MainActivity.SECTION_TICKETS_LIST);
    }
    
    /*
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
    */
}
