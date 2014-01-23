/**
 * 
 */
package me.caesar2011.vpherder.teachersubstitution;

import me.caesar2011.vpherder.R;
import me.caesar2011.vpherder.teachersubstitution.TSAdapter.ViewHolder;
import me.caesar2011.vpherder.teachersubstitution.TSAdapter.ViewHolderREMARK;

import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.widget.TextView;

/**
 * @author BastiLapTop
 *
 */
public class TSjsonAnnouncementRemark extends TSjsonAnnouncement {

	public final String text;
	
	/**
	 * @param jsonObject
	 * @throws JSONException
	 */
	public TSjsonAnnouncementRemark(JSONObject jsonObject) throws JSONException {
		super(jsonObject);
		this.text = jsonObject.optString("text");
	}
	
	@Override
	public void refillView(ViewHolder holder) {
		ViewHolderREMARK holdRemark = (ViewHolderREMARK) holder;
		holdRemark.text.setText(text);
	}

	@Override
	public ViewHolder createHolder(View convertView) {
		ViewHolderREMARK holder = new ViewHolderREMARK();
        holder.text = (TextView)convertView.findViewById(R.id.text);
        return holder;
	}

}
