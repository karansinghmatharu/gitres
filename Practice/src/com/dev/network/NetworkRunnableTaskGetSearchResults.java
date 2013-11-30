package com.dev.network;

/*
 *  Author @ Karan Deep Singh
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.dev.bean.SearchResults;
import com.google.gson.Gson;

public class NetworkRunnableTaskGetSearchResults implements Runnable {

	//private static final String GET_SEARCH_RESULTS = "http://pre.dev.goeuro.de:12345/api/v1/suggest/position/en/name/";
	
	private static final String GET_SEARCH_RESULTS ="https://api.goeuro.de/api/v1/suggest/position/en/name/";
	String searchText;

	public interface GetResultListener {
		void onGetGetSearchResultResponse(SearchResults response);
	}

	private final String TAG_STRING = NetworkRunnableTaskGetSearchResults.class
			.getSimpleName();
	Context mContext = null;
	String URL;

	public GetResultListener getResultListener;

	public GetResultListener getGetSearchResultListener() {
		return getResultListener;
	}

	public void setGetSearchResultListener(GetResultListener getResultListener) {
		this.getResultListener = getResultListener;
	}

	public NetworkRunnableTaskGetSearchResults(Context cx, String searchText) {
		this.searchText = searchText;
		mContext = cx;
		URL = GET_SEARCH_RESULTS;
	}

	
	public HttpClient getNewHttpClient() {
	    try {
	        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	        trustStore.load(null, null);

	        SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
	        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

	        HttpParams params = new BasicHttpParams();
	        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

	        SchemeRegistry registry = new SchemeRegistry();
	        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	        registry.register(new Scheme("https", sf, 443));

	        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

	        return new DefaultHttpClient(ccm, params);
	    } catch (Exception e) {
	        return new DefaultHttpClient();
	    }
	}
	
	@Override
	public void run() {
		Log.d(TAG_STRING, "Running on different thread " + URL);
		String jsonString = null;

		try {

			HttpGet httppost = new HttpGet(URL + searchText);
			Log.v(TAG_STRING, "searchText " + searchText);
			HttpClient httpClient = getNewHttpClient();
			if (httpClient != null) {
				HttpResponse response = httpClient.execute(httppost);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								response.getEntity().getContent(), "UTF-8"));
				jsonString = reader.readLine();
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.v(TAG_STRING, "jsonString " + jsonString);

		if (jsonString != null) {
			SearchResults obj=new Gson().fromJson(jsonString, SearchResults.class);
			Message msg = new Message();
			msg.obj = obj;
			responseHandler.sendMessage(msg);
			
			/*
			JSONObject jsonObject;
			GetSearchResultsResponse response = null;
			try {
				jsonObject = new JSONObject(jsonString);

				if (jsonObject != null) {
					response = new GetSearchResultsResponse();
					JSONObject resultObj = jsonObject.getJSONObject("results");
					if (resultObj != null) {

						SearchResults searchResultList = new SearchResults();
						searchResultList.set_type(resultObj.getString("_type"));
						searchResultList.set_id(resultObj.getLong("_id"));
						searchResultList.setName(resultObj.getString("name"));
						searchResultList.setType(resultObj.getString("type"));

						JSONArray geoPositionListArray = resultObj
								.getJSONArray("geo_position");
						ArrayList<GeoPosition> geoPositionArryList = new ArrayList<GeoPosition>();

						for (int j = 0; j < geoPositionListArray.length(); j++) {
							GeoPosition geoPositionList = new GeoPosition();

							JSONObject coordinates = geoPositionListArray
									.getJSONObject(j);
							if (coordinates != null) {
								geoPositionList.setLatitude(coordinates
										.getDouble("latitude"));

								geoPositionList.setLongitude(coordinates
										.getDouble("longitude"));

								geoPositionArryList.add(geoPositionList);
							}

						}
						searchResultList
								.setGeoPositionList(geoPositionArryList);

						response.getSearchResultsList().add(searchResultList);

					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		*/}
	}

	Handler responseHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (getResultListener != null) {
				SearchResults getSearchResultsResponse = (SearchResults) msg.obj;
				if (getSearchResultsResponse != null) {
					getResultListener
							.onGetGetSearchResultResponse(getSearchResultsResponse);
				} else {
					getResultListener.onGetGetSearchResultResponse(null);
				}
			}

			super.handleMessage(msg);
		}

	};
}
