package com.example.mango;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

public class SearchActivity extends Activity implements 
OnQueryTextListener  {
	
	private SearchView sv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);

		sv = (SearchView) findViewById(R.id.sv);
		sv.setOnQueryTextListener(this);
		sv.setSubmitButtonEnabled(true);
		sv.setIconifiedByDefault(false);;
	}
	
	@Override
	public boolean onQueryTextSubmit(String query) {
		Toast.makeText(SearchActivity.this, "search " + query, Toast.LENGTH_SHORT)
				.show();
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		if (newText != null && newText.length() > 0) {

		}
		return true;
	}

}
