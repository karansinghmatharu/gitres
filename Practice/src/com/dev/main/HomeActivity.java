package com.dev.main;

/*
 *  Author @ Karan Deep Singh
 */
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dev.bean.SearchResults;
import com.dev.network.NetworkRunnableTaskGetSearchResults;
import com.dev.network.NetworkRunnableTaskGetSearchResults.GetResultListener;
import com.dev.practice.R;

public class HomeActivity extends Activity implements GetResultListener {

	List<String> countryList = new ArrayList<String>();
	ImageButton searchButton, dateButton;
	AutoCompleteTextView textView, destTextView;
	TextView dateText;
	private int year;
	private int month;
	private int day;
	static final int DATE_PICKER_ID = 10;
	ArrayAdapter<String> adapter = null;
	ArrayAdapter<String> destAdapter = null;
	ProgressBar progressLoading, progressLoadingDest;
	SearchResults results;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_home);
		progressLoading = (ProgressBar) findViewById(R.id.progressLoading);

		if (adapter == null) {
			adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line, countryList);
		}
		if (destAdapter == null) {
			destAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line, countryList);
		}

		textView = (AutoCompleteTextView) findViewById(R.id.source_countries_list);
		textView.setThreshold(2);
		textView.setAdapter(adapter);
		textView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				if (!s.toString().isEmpty()) {

					hit(s.toString());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

		destTextView = (AutoCompleteTextView) findViewById(R.id.destinationPointText);
		destTextView.setThreshold(2);
		destTextView.setAdapter(destAdapter);
		destTextView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (!s.toString().isEmpty()) {
					hit(s.toString());
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				adapter.notifyDataSetChanged();

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

	protected void findLatLong(String autoText) {
		// TODO Auto-generated method stub
		double lat = 0, longi = 0;
		if (results != null) {
			int searchListLength = results.getResults().size();
			for (int i = 0; i < searchListLength; i++) {
				if (results.getResults().get(i).getName().contains(autoText)) {

					lat = results.getResults().get(i).getGeo_position()
							.getLatitude();
					longi = results.getResults().get(i).getGeo_position()
							.getLongitude();

				}
			}
		}
		System.out.println("" + lat + longi);

	}

	protected void hit(String string) {
		if (string.length() == 4) {
			progressLoading.setVisibility(View.VISIBLE);
			NetworkRunnableTaskGetSearchResults getSearchResults = new NetworkRunnableTaskGetSearchResults(
					getApplicationContext(), string);
			getSearchResults.setGetSearchResultListener(this);

			Thread thread = new Thread(getSearchResults);
			thread.start();
		}

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

	@Override
	public void onGetGetSearchResultResponse(SearchResults response) {

		this.results = response;
		if (response != null) {
			if (response.getResults().size() > 0) {
				countryList.clear();
				for (int i = 0; i < response.getResults().size(); i++) {
					countryList.add(response.getResults().get(i).getName());
				}
			}
		}
		adapter.addAll(countryList);
		adapter.notifyDataSetChanged();
		destAdapter.addAll(countryList);
		destAdapter.notifyDataSetChanged();

		progressLoading.setVisibility(View.INVISIBLE);

		// bug
		// setting this in order to refresh the dropdown list
		// but by doing so i am not able to stop the list from populating again

		String tempString = textView.getText().toString();
		textView.setText(tempString);
		String tempString1 = destTextView.getText().toString();
		destTextView.setText(tempString1);

	}

}
