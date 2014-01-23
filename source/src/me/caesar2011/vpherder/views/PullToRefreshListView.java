package me.caesar2011.vpherder.views;

import me.caesar2011.vpherder.R;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

public class PullToRefreshListView extends ListView implements OnScrollListener {

	private float mStartX;
	private float mStartY;
	private int mRefreshState;
	private LayoutInflater mInflater;
	private View mHeaderView;
	private HeaderViewHolder mHeaderViewHolder;
	private int mLastTopVisiblePos;
	
	private static final int PULL_DOWN_LIMIT = (int) Math.round(0.75 * DisplayMetrics.DENSITY_DEFAULT);
	
	public PullToRefreshListView(Context context) {
		super(context);
		init(context);
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PullToRefreshListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	public void init(Context context) {
        super.setOnScrollListener(this);

        mInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        mHeaderView = mInflater.inflate(R.layout.pull_to_refresh_header, null);
        mHeaderViewHolder = new HeaderViewHolder(mHeaderView);
        addHeaderView(mHeaderView);
        
        mRefreshState = RefreshState.PULL_TO_REFRESH;
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub
	}
	
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	boolean isHandled = false;
    	
    	if (event.getAction() == MotionEvent.ACTION_DOWN || mLastTopVisiblePos == 1) {
    		int refreshBoxHeight = (int) ((mRefreshState == RefreshState.REFRESHING)?PULL_DOWN_LIMIT+15:1);
    		mStartX = event.getRawX();
    		mStartY = event.getRawY()-refreshBoxHeight;
    		mHeaderViewHolder.setVisibility(View.VISIBLE);
    		mHeaderView.setLayoutParams(new LayoutParams(mHeaderView.getLayoutParams().width, refreshBoxHeight));
        }
    	if (event.getRawY()-mStartY > Math.abs(event.getRawX()-mStartX) && getFirstVisiblePosition() == 0) {
	    	int stretch = (int) Math.max(0, event.getRawY()-mStartY);
        	System.out.println(event.getRawY()-mStartY);
        	System.out.println(Math.abs(event.getRawX()-mStartX));
        	System.out.println("--");
	    	mHeaderView.setLayoutParams(new LayoutParams(mHeaderView.getLayoutParams().width, stretch));
	    	if (mRefreshState == RefreshState.PULL_TO_REFRESH && stretch > PULL_DOWN_LIMIT) {
	    		mRefreshState = RefreshState.RELEASE_TO_REFRESH;
	    		mHeaderViewHolder.setRefreshingState(mRefreshState);
	    	} else if (mRefreshState == RefreshState.RELEASE_TO_REFRESH && stretch <= PULL_DOWN_LIMIT) {
	    		mRefreshState = RefreshState.PULL_TO_REFRESH;
	    		mHeaderViewHolder.setRefreshingState(mRefreshState);
	    	} else if (mRefreshState == RefreshState.ABORTING && stretch > PULL_DOWN_LIMIT) {
	    		mRefreshState = RefreshState.RELEASE_TO_REFRESH;
	    		mHeaderViewHolder.setRefreshingState(mRefreshState);
	    	} else if (mRefreshState == RefreshState.REFRESHING && stretch <= PULL_DOWN_LIMIT) {
	    		mRefreshState = RefreshState.ABORTING;
	    		mHeaderViewHolder.setRefreshingState(mRefreshState);
	    	}
        	isHandled = true;
    	}
    	
        if (event.getAction() == MotionEvent.ACTION_UP) {
        	isHandled = false;
        	switch (mRefreshState) {
        	case RefreshState.PULL_TO_REFRESH:
        		mHeaderViewHolder.setVisibility(View.GONE);
        		mHeaderView.setLayoutParams(new LayoutParams(mHeaderView.getLayoutParams().width, 0));
        		break;
        	case RefreshState.RELEASE_TO_REFRESH:
        		mHeaderViewHolder.setVisibility(View.VISIBLE);
        		mRefreshState = RefreshState.REFRESHING;
        		mHeaderViewHolder.setRefreshingState(mRefreshState);
        		mHeaderView.setLayoutParams(new LayoutParams(mHeaderView.getLayoutParams().width, PULL_DOWN_LIMIT));
        		break;
        	case RefreshState.REFRESHING:
        		mHeaderView.setLayoutParams(new LayoutParams(mHeaderView.getLayoutParams().width, PULL_DOWN_LIMIT));
        		break;
        	case RefreshState.ABORTING:
        		mHeaderViewHolder.setVisibility(View.GONE);
        		mRefreshState = RefreshState.PULL_TO_REFRESH;
        		mHeaderViewHolder.setRefreshingState(mRefreshState);
        		mHeaderView.setLayoutParams(new LayoutParams(mHeaderView.getLayoutParams().width, 0));
        		break;
        	}
        }
        mLastTopVisiblePos = getFirstVisiblePosition();
        return isHandled?true:super.onTouchEvent(event);
    }
    
    private class HeaderViewHolder {
    	public final View mParent;
    	public final TextView mText;
    	
    	public HeaderViewHolder(View HeaderView) {
    		mParent = HeaderView;
    		mText = (TextView) HeaderView.findViewById(R.id.text);
    	}
    	
    	public void setVisibility(int visibility) {
    		mParent.setVisibility(visibility);
    		mText.setVisibility(visibility);
    	}
    	
    	public void setRefreshingState(int refreshState) {
    	    switch (refreshState){
	    	case RefreshState.PULL_TO_REFRESH:
	    		mText.setText(R.string.pull_to_refresh_pull_label);
	    		break;
	    	case RefreshState.RELEASE_TO_REFRESH:
	    		mText.setText(R.string.pull_to_refresh_release_label);
	    		break;
	    	case RefreshState.REFRESHING:
	    		mText.setText(R.string.pull_to_refresh_refreshing_label);
	    		break;
	    	case RefreshState.ABORTING:
	    		mText.setText(R.string.pull_to_refresh_aborting_label);
	    		break;
    	    }
    	}
    }
    
    private static class RefreshState {
    	public static final int PULL_TO_REFRESH = 1;
    	public static final int RELEASE_TO_REFRESH = 2;
    	public static final int REFRESHING = 3;
    	public static final int ABORTING = 4;
    }
}
