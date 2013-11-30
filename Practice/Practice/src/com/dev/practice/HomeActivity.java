package com.dev.practice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dev.practice.NetworkRunnableTaskGetSearchResults.GetResultListener;

public class HomeActivity extends Activity implements GetResultListener{

	private static final String[] COUNTRIES = new String[] { "Belgium",
			"France", "Italy", "Germany", "Spain", "Berlin", "Potsdam Germany",
			"Potsdam USA", "London", "Paris", "Frankfurt", "Poland",
			"HongKong", "Beging", "Kula Lampur", "New Delhi", "Mumbai", "Goa",
			"Belgium", "Holland" };
	
	List<String> countryList = new ArrayList<String>();
	ImageButton searchButton, dateButton;
	AutoCompleteTextView textView, destTextView;
	TextView dateText;
	private int year;
	private int month;
	private int day;
	static final int DATE_PICKER_ID = 10;
	ArrayAdapter<String> adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_home);
	/*	for (int i = 0; i < COUNTRIES.length; ++i) {
			countryList.add(COUNTRIES[i]);
		}*/

		
		if (adapter == null) {
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, countryList);
		}
		textView = (AutoCompleteTextView) findViewById(R.id.source_countries_list);
		destTextView = (AutoCompleteTextView) findViewById(R.id.destinationPointText);
		textView.setThreshold(1);
		textView.setAdapter(adapter);

		textView.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if(!s.toString().isEmpty())
				hit(s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		textView.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_UP) {
					return check(textView.getText().toString());
				}
				return false;
			}
		});

		searchButton = (ImageButton) findViewById(R.id.searchButton);
		searchButton.setVisibility(View.GONE);
		dateButton = (ImageButton) findViewById(R.id.dateButton);
		dateText = (TextView) findViewById(R.id.dateText);
		dateText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s.toString().equals("")) {
					searchButton.setVisibility(View.GONE);
				} else {
					searchButton.setVisibility(View.VISIBLE);
				}

			}
		});

		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		dateButton.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				showDialog(DATE_PICKER_ID);
			}

		});

	}
	private String mSpecialSearchTextarr[]={" "};
	protected void hit(String string) {
		// TODO Auto-generated method stub
/*		for(int i=0;i<=mSpecialSearchTextarr.length;i++)
		{
			string=string.replace(mSpecialSearchTextarr[i], mSpecialSearchTextarr[i]+"%20");
		}*/
		NetworkRunnableTaskGetSearchResults getSearchResults=new NetworkRunnableTaskGetSearchResults(getApplicationContext(), string);
		getSearchResults.setGetSearchResultListener(this);
		
		Thread thread=new Thread(getSearchResults);
		thread.start();
		
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_PICKER_ID:
			return new DatePickerDialog(this, pickerListener, year, month, day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;
			dateText.setText(new StringBuilder().append(month + 1).append(".")
					.append(day).append(".").append(year).append(" "));

		}
	};

	@SuppressWarnings("deprecation")
	public void onClickButton(View view) {

		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

		alertDialog.setTitle("Alert!");
		alertDialog.setMessage("Search is not yet implemented");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
			}
		});
		alertDialog.show();
	}

	protected boolean check(String string) {
		// TODO Auto-generated method stub
		if (string != null) {
			searchAndRemove(string, countryList);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line, countryList);
			AutoCompleteTextView textView2 = (AutoCompleteTextView) findViewById(R.id.source_countries_list);
			textView2.setThreshold(1);
			textView2.setAdapter(adapter);
			return false;
		}
		return true;

	}

	private void searchAndRemove(String string, List<String> countryList) {
		if (countryList.contains(string) == true) {
			countryList.remove(countryList.indexOf(string));
		}

	}

	@Override
	public void onGetGetSearchResultResponse(SearchResults response) {
		// TODO Auto-generated method stub
		
		
	
		if(response!=null)
		{
			if(response.getResults().size()>0) {
				countryList.clear();
				for (int i = 0; i < response.getResults().size(); i++){
					countryList.add(response.getResults().get(i).getName());
				}
			}
		}
		/*if (adapter == null) {
			adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line, countryList);
			}*/
//		adapter.addAll(countryList);
		adapter.addAll(countryList);
		adapter.notifyDataSetChanged();
		
		String tempString = textView.getText().toString();
		textView.setText(tempString);
		
	}

}
