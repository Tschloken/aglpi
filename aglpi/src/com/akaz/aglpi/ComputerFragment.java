package com.akaz.aglpi;

import com.akaz.aglpi.api.Computer;
import com.akaz.aglpi.api.GlpiClient;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ComputerFragment extends Fragment {
	private Computer computer;
	private GlpiClient		glpi;
	
	public ComputerFragment(Computer computer, GlpiClient glpi) {
		this.computer = computer;
		this.glpi = glpi;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_computer, container, false);
		if (computer.get("name") != null)
			((TextView)rootView.findViewById(R.id.name)).setText(computer.get("name").toString());
		if (computer.get("manufacturers_id") != null)
			((TextView)rootView.findViewById(R.id.manufacturer)).setText(glpi.getManufacturerDropdown().get(Integer.valueOf(computer.get("manufacturers_id").toString())));
		if (computer.get("computermodels_id") != null)
			((TextView)rootView.findViewById(R.id.model)).setText(glpi.getComputerModelDropdown().get(Integer.valueOf(computer.get("computermodels_id").toString())));
		if (computer.get("locations_id") != null)
		((TextView)rootView.findViewById(R.id.location)).setText(glpi.getLocationDropdown().get(Integer.valueOf(computer.get("locations_id").toString())).toString());
		if (computer.get("contact") != null)
			((TextView)rootView.findViewById(R.id.user)).setText(computer.get("contact").toString());
		if (computer.get("serial") != null)
			((TextView)rootView.findViewById(R.id.serial)).setText(computer.get("serial").toString());
		if (computer.get("uuid") != null)
			((TextView)rootView.findViewById(R.id.uuid)).setText(computer.get("uuid").toString());
		if (computer.get("os_license_number") != null)
			((TextView)rootView.findViewById(R.id.os_serial)).setText(computer.get("os_license_number").toString());
		if (computer.get("os_licenseid") != null)
			((TextView)rootView.findViewById(R.id.os_productid)).setText(computer.get("os_licenseid").toString());
		if (computer.get("date_mod") != null)
			((TextView)rootView.findViewById(R.id.last_modified)).setText(computer.get("date_mod").toString());
		return rootView;
	}
}
