/**
 * 
 */
package me.caesar2011.vpherder.teachersubstitution;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import me.caesar2011.vpherder.R;
import me.caesar2011.vpherder.views.PullToRefreshListView;
import me.caesar2011.vpherder.views.PullToRefreshListView.OnRefreshListener;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Class to organize the drawings of the teacher substition.
 * 
 * @author BastiLapTop
 *
 */
public class TSCreate {

	private final Activity activity;
	private final LayoutInflater inflater;
	private final ViewGroup container;
	private static TSjsonObject JsonObject;
	private boolean downloadStarted;
	private TSAdapter adapter;
	private PullToRefreshListView listview;

	/**
	 * Constructor
	 */
	public TSCreate(Activity activity, LayoutInflater inflater, ViewGroup container) {
		this.activity = activity;
		this.inflater = inflater;
		this.container = container;
		this.downloadStarted = false;
		
		if(JsonObject == null){
			StartDownload();
		}
	}
	
	public View Draw() {
		View rootView = inflater.inflate(R.layout.fragment_teacher_substitution,
				container, false);
		
		listview = (PullToRefreshListView) rootView.findViewById(R.id.listview);
		listview.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				StartDownload();
			}
			
		});
		System.out.println(JsonObject);
	    adapter = new TSAdapter(activity,
	    		R.layout.teacher_substitution_row_remark, (JsonObject!=null)?JsonObject.announcements:(new ArrayList<TSjsonAnnouncement>()));
	    listview.setAdapter(adapter);
		return rootView;
	}

	public void StartDownload() {
		if (!downloadStarted) {
			downloadStarted = true;
			try {
				URL url = new URL("https://vpherder.canis.uberspace.de/example.json");
				DownloadFilesTask downloadSubstitutions = new DownloadFilesTask();
				downloadSubstitutions.execute(url);
			} catch (MalformedURLException e) {
				downloadStarted = false;
			}
		}
	}

	public void FinishDownload() {
		downloadStarted = false;
		adapter.clear();
		adapter.addAll(JsonObject.announcements);
		adapter.notifyDataSetChanged();
		listview.onRefreshComplete();
	}
	
	private class DownloadFilesTask extends AsyncTask<URL, Integer, String> {
		
		protected String doInBackground(URL... urls) {
			String file = "";
	        URL url = urls[0];
			URLConnection connection;
			try {
				connection = url.openConnection();
				InputStream inputStream = connection.getInputStream();

				StringWriter writer = new StringWriter();
				IOUtils.copy(inputStream, writer, "UTF-8");
				file = writer.toString();
			} catch (IOException e) {
			}
	        return file;
	    }

		protected void onProgressUpdate(Integer... progress) {
	        //setProgressPercent(progress[0]);
	    }
		
		protected void onPostExecute(String result) {
			try {
				JsonObject = new TSjsonObject(result);
			} catch (JSONException e) {
			}
			FinishDownload();
		}
	}
}
