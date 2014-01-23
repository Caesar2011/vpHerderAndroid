/**
 * 
 */
package me.caesar2011.vpherder.teachersubstitution;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The hole json object witch handles version, date, creation/modified time, etc. ...
 * And it holds a list of all announcements.
 * 
 * @author BastiLapTop
 *
 */
public class TSjsonObject {

	public final String version;
	public final Date date;
	public final Date created;
	public final Date modified;
	public final ArrayList<TSjsonAnnouncement> announcements;
	
	
	/**
	 * @param jsonObject
	 * @throws JSONException
	 */
	public TSjsonObject(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated constructor stub
		this.version = jsonObject.getString("version");
		
		try {
			this.date = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY).parse(jsonObject.getString("date"));
		} catch (ParseException e) {
			throw new JSONException("Date could not be converted. ParseExeption.");
		}
		
		try {
			this.created = new Date(Long.parseLong(jsonObject.getString("created")) * 1000);
		} catch (NumberFormatException e) {
			throw new JSONException("Created could not be converted. NumberFormatException.");
		}
		
		try {
			this.modified = new Date(Long.parseLong(jsonObject.getString("modified")) * 1000);
		} catch (NumberFormatException e) {
			throw new JSONException("Modified could not be converted. NumberFormatException.");
		}
		
		JSONArray jArray = jsonObject.getJSONArray("announcements");
		JSONObject jObject = null;
		this.announcements = new ArrayList<TSjsonAnnouncement>();
		for (int i = 0; i < jArray.length(); i++) {
			jObject = jArray.getJSONObject(i);
			if (jObject.getString("type").equals("alteration")) {
				this.announcements.add(new TSjsonAnnouncementAlteration(jObject));
			} else {
				this.announcements.add(new TSjsonAnnouncementRemark(jObject));
			}
		}
	}
	
	public TSjsonObject(String jsonString) throws JSONException {
		this(new JSONObject(jsonString));
	}
	
	

}
