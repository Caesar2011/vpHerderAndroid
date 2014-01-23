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
import java.util.concurrent.ExecutionException;

import me.caesar2011.vpherder.R;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

	/**
	 * Constructor
	 */
	public TSCreate(Activity activity, LayoutInflater inflater, ViewGroup container) {
		this.activity = activity;
		this.inflater = inflater;
		this.container = container;
		
		if(JsonObject == null){
			try {
				URL url = new URL("https://vpherder.canis.uberspace.de/example.json");
				
				DownloadFilesTask downloadSubstitutions = new DownloadFilesTask();
				downloadSubstitutions.execute(url);
				downloadSubstitutions.get();
				String theString = downloadSubstitutions.getOutput()[0];
				
				JsonObject = new TSjsonObject(theString);
			} catch (ExecutionException e) {
				System.out.println("ERRRRRRRRRRRRRRRRRRRRROR ExecutionException!");
			} catch (InterruptedException e) {
				System.out.println("ERRRRRRRRRRRRRRRRRRRRROR InterruptedException!");
			} catch (JSONException e) {
				System.out.println("ERRRRRRRRRRRRRRRRRRRRROR JSONException!");
			} catch (MalformedURLException e) {
				System.out.println("ERRRRRRRRRRRRRRRRRRRRROR MalformedURLException!");
			}
		}
	}
	
	public View Draw() {
		View rootView = inflater.inflate(R.layout.fragment_teacher_substitution,
				container, false);
		
		final ListView listview = (ListView) rootView.findViewById(R.id.listview);
		
	    final TSAdapter adapter = new TSAdapter(activity,
	    		R.layout.teacher_substitution_row_remark, JsonObject.announcements);
	    listview.setAdapter(adapter);
	    
		return rootView;
	}

	
	
	private class DownloadFilesTask extends AsyncTask<URL, Integer, String[]> {
		private String[] outputFiles;
		
		protected String[] doInBackground(URL... urls) {
	        int count = urls.length;
	        String[] files = new String[count];
	        for (int i = 0; i < count; i++) {
				URLConnection connection;
				try {
					connection = urls[i].openConnection();
					InputStream inputStream = connection.getInputStream();
	            	publishProgress((int) ((i / (float) count) * 100));

					StringWriter writer = new StringWriter();
					IOUtils.copy(inputStream, writer, "UTF-8");
					files[i] = writer.toString();
				} catch (IOException e) {
				}
	            
	            // Escape early if cancel() is called
	            if (isCancelled()) break;
	        }
	        outputFiles = files;
	        return files;
	    }

		protected void onProgressUpdate(Integer... progress) {
	        //setProgressPercent(progress[0]);
	    }
		
		protected String[] getOutput() {
			return outputFiles;
		}
	}
}
