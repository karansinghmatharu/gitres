package com.dev.practice;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.dev.practice.NetworkRunnableTaskGetSearchResults.GetResultListener;

public class AutoCompleteAdapter extends ArrayAdapter<String> implements GetResultListener,Filterable {

	protected static final String TAG = "AutoCompleteAdapter";
    private List<String> autoList;
    Context context;
    String filterText;
    public AutoCompleteAdapter(Activity context, String nameFilter) {
        super(context, android.R.layout.simple_dropdown_item_1line);
        this.context=context;
        this.filterText=nameFilter;
        autoList = new ArrayList<String>();
    }
    @Override
    public int getCount() {
    	// TODO Auto-generated method stub
    	return autoList.size();
    }
    @Override
    public String getItem(int position) {
    	// TODO Auto-generated method stub
    	return autoList.get(position);
    }
    @Override
    public Filter getFilter() {
    	 Filter myFilter = new Filter() {
             @Override
             protected FilterResults performFiltering(CharSequence constraint) {
                 FilterResults filterResults = new FilterResults();

              
                 if (constraint != null) {
                     // A class that queries a web API, parses the data and
                     // returns an ArrayList<GoEuroGetSet>
                	/*   NetworkRunnableTaskGetSearchResults networkRunnableTaskGetSearchResults  = new NetworkRunnableTaskGetSearchResults(context,filterText);
                		networkRunnableTaskGetSearchResults.setGetSearchResultListener(this);
                     List<SuggestGetSet> new_suggestions =jp.getParseJsonWCF(constraint.toString());
                     suggestions.clear();
                     for (int i=0;i<new_suggestions.size();i++) {
                         suggestions.add(new_suggestions.get(i).getName());
                     }
  
                     // Now assign the values and count to the FilterResults
                     // object
                     filterResults.values = suggestions;
                     filterResults.count = suggestions.size();*/
                 }
                 return filterResults;
             }
  
             @Override
             protected void publishResults(CharSequence contraint,
                     FilterResults results) {
                 if (results != null && results.count > 0) {
                     notifyDataSetChanged();
                 } else {
                     notifyDataSetInvalidated();
                 }
             }
         };
         return myFilter;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	// TODO Auto-generated method stub
    	 TextView originalView = (TextView) super.getView(position, convertView, parent); // Get the original view

         final LayoutInflater inflater = LayoutInflater.from(getContext());
         final TextView view = (TextView) inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);

    	return super.getView(position, convertView, parent);
    }
	@Override
	public void onGetGetSearchResultResponse(SearchResults response) {
		// TODO Auto-generated method stub
		
	}
}
