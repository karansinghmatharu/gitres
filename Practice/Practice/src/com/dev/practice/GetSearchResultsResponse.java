package com.dev.practice;

import java.util.ArrayList;
import java.util.List;

public class GetSearchResultsResponse {
	private List<SearchResults> searchResultsList;

	public GetSearchResultsResponse() {
		setSearchResultsList(new ArrayList<SearchResults>());
	}

	public List<SearchResults> getSearchResultsList() {
		return searchResultsList;
	}

	public void setSearchResultsList(List<SearchResults> searchResultsList) {
		this.searchResultsList = searchResultsList;
	}

}
