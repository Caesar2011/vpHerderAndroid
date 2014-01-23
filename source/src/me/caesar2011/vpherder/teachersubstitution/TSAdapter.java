/**
 * 
 */
package me.caesar2011.vpherder.teachersubstitution;

import java.util.ArrayList;

import me.caesar2011.vpherder.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * @author BastiLapTop
 *
 */
public class TSAdapter extends ArrayAdapter<TSjsonAnnouncement> {

	private ArrayList<TSjsonAnnouncement> mData = new ArrayList<TSjsonAnnouncement>();
	private final LayoutInflater mInflater;

    public TSAdapter(Context context, int textViewResourceId,
    		ArrayList<TSjsonAnnouncement> objects) {
    	super(context, textViewResourceId, objects);
    	mData = objects;
    	mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public TSjsonAnnouncement getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
    @Override
    public int getItemViewType(int position) {
    	// TODO Optimize
    	if (mData.get(position) instanceof TSjsonAnnouncementRemark) {
    		return AnnounceType.REMARK;
    	} else {
    		return AnnounceType.ALTERATION;
    	}
    }

    @Override
    public int getViewTypeCount() {
        return AnnounceType.count;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case AnnounceType.REMARK:
                    convertView = mInflater.inflate(R.layout.teacher_substitution_row_remark, null);
                    break;
                case AnnounceType.ALTERATION:
                    convertView = mInflater.inflate(R.layout.teacher_substitution_row_alteration, null);
                    break;
            }
            holder = mData.get(position).createHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // (Re-)fill view with new data
        mData.get(position).refillView(holder);
        return convertView;
	}
	 
	
	// SUB CLASSES
    
	static class AnnounceType {
		public static final int REMARK = 0;
		public static final int ALTERATION = 1;
		public static final int count = 2;
		}
	
	public static class ViewHolder {}
	
	public static class ViewHolderREMARK extends ViewHolder {
    	public TextView text;
	}
	
	public static class ViewHolderALTERATION extends ViewHolder {
    	public TextView teacher;
		public TextView subject;
		public TextView subject_subinfo;
		public TextView changes_from;
		public TextView changes_to;
		public TextView remark;
	}
}
