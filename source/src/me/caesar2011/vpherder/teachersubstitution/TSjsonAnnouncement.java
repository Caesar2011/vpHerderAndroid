package me.caesar2011.vpherder.teachersubstitution;

import me.caesar2011.vpherder.teachersubstitution.TSAdapter.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;

public class TSjsonAnnouncement {
	
	public final String type;
	
	/**
	 * @param jsonObject
	 * @throws JSONException
	 */
	public TSjsonAnnouncement(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated constructor stub
		this.type = jsonObject.getString("type");
	}
	
	public void refillView(ViewHolder holder) {
		
	}
	
	public ViewHolder createHolder(View convertView) {
		return null;
	}

}
