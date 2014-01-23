/**
 * 
 */
package me.caesar2011.vpherder.teachersubstitution;

import java.util.ArrayList;
import java.util.Iterator;

import me.caesar2011.vpherder.R;
import me.caesar2011.vpherder.teachersubstitution.TSAdapter.ViewHolder;
import me.caesar2011.vpherder.teachersubstitution.TSAdapter.ViewHolderALTERATION;

import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.widget.TextView;

/**
 * @author BastiLapTop
 *
 */
public class TSjsonAnnouncementAlteration extends TSjsonAnnouncement {

	public final int time;
	public final String form;
	public final String teacher;
	public final String subject;
	public final boolean cancelled;
	public final ArrayList<TSjsonChange> changes;
	public final String remark;
	
	/**
	 * @param jsonObject
	 * @throws JSONException
	 */
	public TSjsonAnnouncementAlteration(JSONObject jsonObject)
			throws JSONException {
		super(jsonObject);
		this.time = jsonObject.getInt("time");
		this.form = jsonObject.getString("form");
		this.teacher = jsonObject.getString("teacher");
		this.subject = jsonObject.getString("class");
		this.cancelled = jsonObject.getBoolean("cancelled");
		
		this.changes = new ArrayList<TSjsonChange>();
		JSONObject jObject = jsonObject.getJSONObject("changes");
		Iterator<?> i = jObject.keys();

        while(i.hasNext()){
            String key = (String)i.next();
            if(jObject.get(key) instanceof JSONObject){
            	jObject.getJSONObject(key);
            	TSjsonChange jChanges = new TSjsonChange(key, 
            			jObject.getJSONObject(key).getString("old"), 
            			jObject.getJSONObject(key).getString("new"));
            	this.changes.add(jChanges);
            }
        }
		this.remark = jsonObject.optString("remark");
	}
	
	@Override
	public void refillView(ViewHolder holder) {
		ViewHolderALTERATION holdAlteration = (ViewHolderALTERATION) holder;
		holdAlteration.teacher.setText(teacher);
		holdAlteration.subject.setText(subject);
		holdAlteration.subject_subinfo.setText(time + " - " + form);
		
		String sFrom = "";
		String sTo = "";
		Iterator<TSjsonChange> i = changes.iterator();
		while (i.hasNext()) {
			TSjsonChange jChange = i.next();
			if (!sFrom.equals("")) {
				sFrom += "\n";
				sTo += "\n";
			}
			sFrom += jChange.oldValue;
			sTo += jChange.newValue;
		}
		holdAlteration.changes_from.setText(sFrom);
		holdAlteration.changes_to.setText(sTo);
		
		if (remark.equals("false")) {
			holdAlteration.remark.setVisibility(View.GONE);
		} else {
			holdAlteration.remark.setText(remark);
		}
	}

	@Override
	public ViewHolder createHolder(View convertView) {
		ViewHolderALTERATION holder = new ViewHolderALTERATION();
        holder.teacher = (TextView)convertView.findViewById(R.id.teacher);
        holder.subject = (TextView)convertView.findViewById(R.id.subject);
        holder.subject_subinfo = (TextView)convertView.findViewById(R.id.subject_subinfo);
        holder.changes_from = (TextView)convertView.findViewById(R.id.textview_from);
        holder.changes_to = (TextView)convertView.findViewById(R.id.textview_to);
        holder.remark = (TextView)convertView.findViewById(R.id.remark);
        return holder;
	}

}
