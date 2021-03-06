package com.sqing.www.mytxtread.view;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.sqing.www.mytxtread.R;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Created by sqing on 2016/12/8.
 */

public class ReFlashListView extends ListView implements OnScrollListener {
    View header;// 顶部布局文件；
    View footer;// 底部布局文件；
    int headerHeight;// 顶部布局文件的高度；
    int firstVisibleItem;// 当前第一个可见的item的位置；
    int scrollState;// listview 当前滚动状态；
    boolean isRemark;// 标记，当前是在listview最顶端摁下的；
    int startY, startX;// 摁下时的Y值；

    int state;// 当前的状态；
    final int NONE = 0;// 正常状态；
    final int PULL = 1;// 提示下拉状态；
    final int RELESE = 2;// 提示释放状态；
    final int REFLASHING = 3;// 刷新状态；
    IReflashListener iReflashListener;//刷新数据的接口
    ILoadMoreDataListener iLoadMoreDataListener;//加载更多数据接口
    private int mTouchSlop, downX, spaceMove, tempCount;
    private boolean isFirshMeasure = true;

    private int totalItemCount;//总的显示数量
    private int lastVisibleItem; //最后一个可见的item
    private boolean isLoading; //是否正在加载更多数据;
    private final Runnable smoothToRefreshStag = new Runnable() {
        @Override
        public void run() {
            if (spaceMove > 0) {
                spaceMove -= 10;
                topPadding(spaceMove);
                postDelayed(this, 10);
            }
        }
    };
    private final Runnable smoothToOrigin = new Runnable() {
        @Override
        public void run() {
			/*if (tempCount < headerHeight) {
				tempCount += 10;
				topPadding(-tempCount);
				postDelayed(this, 10);
			}else {
				topPadding(-headerHeight);
			}*/
            if (spaceMove > -headerHeight) {
                spaceMove -= 15;
                topPadding(spaceMove);
                postDelayed(this, 10);
            }
        }
    };
    private final Runnable showRefresh = new Runnable() {
        @Override
        public void run() {
            if (tempCount < headerHeight) {
                tempCount += 10;
                topPadding(-headerHeight + tempCount);
                postDelayed(this, 10);
            }
        }
    };

    public ReFlashListView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        initView(context);
    }

    public ReFlashListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        initView(context);
    }

    public ReFlashListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initView(context);
    }

    /**
     * 初始化界面，添加顶部布局文件到 listview
     *
     * @param context
     */
    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        header = inflater.inflate(R.layout.header_layout, null);
        footer = inflater.inflate(R.layout.footer_layout, null);
        footer.findViewById(R.id.footer_layout).setVisibility(View.GONE);
        measureView(header);
        headerHeight = header.getMeasuredHeight();
//		Log.i("tag", "headerHeight = " + headerHeight);
        topPadding(-headerHeight);
        this.addHeaderView(header);
        this.addFooterView(footer);
        this.setOnScrollListener(this);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }
    /**
     * 通知父布局，占用的宽，高；
     *
     * @param view
     */
    private void measureView(View view) {
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int height;
        int tempHeight = p.height;
        if (tempHeight > 0) {
            height = MeasureSpec.makeMeasureSpec(tempHeight,
                    MeasureSpec.EXACTLY);
        } else {
            height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        view.measure(width, height);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
//		super.onMeasure(widthMeasureSpec, expandSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isFirshMeasure && header.getMeasuredHeight()!= 0) {
            isFirshMeasure = false;
            headerHeight = header.getMeasuredHeight();
            Log.i("debug", "header height kk " + header.getMeasuredHeight());
            topPadding(-headerHeight);
        }
    }

    /**
     * 设置header 布局 上边距；
     *
     * @param topPadding
     */
    private void topPadding(int topPadding) {
        header.setPadding(header.getPaddingLeft(), topPadding,
                header.getPaddingRight(), header.getPaddingBottom());
        header.invalidate();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // TODO Auto-generated method stub
        this.firstVisibleItem = firstVisibleItem;
        this.lastVisibleItem = firstVisibleItem + visibleItemCount;
        this.totalItemCount = totalItemCount;

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // TODO Auto-generated method stub
        this.scrollState = scrollState;
        if(lastVisibleItem == totalItemCount && scrollState == SCROLL_STATE_IDLE) {
            //加载更多的数据
            if(!isLoading) {
                isLoading = true;
                footer.findViewById(R.id.footer_layout).setVisibility(View.VISIBLE);
                iLoadMoreDataListener.onLoadMoreData();
            }
        }
    }
//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent ev) {
//
//		if (firstVisibleItem != 0) {
//			return super.onInterceptTouchEvent(ev);
//		}
//		switch (ev.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			downX = (int) ev.getRawX();
//			startY = (int) ev.getRawY();
//			Log.i("debug", "downX " + downX);
//			break;
//		case MotionEvent.ACTION_MOVE:
//			Log.i("debug", "ACTION_MOVE ");
//			int moveX = (int) ev.getRawX();
//			int moveY = (int) ev.getRawY();
//			Log.i("debug", "moveY " + moveX + " startY " + startY);
//			// 满足此条件屏蔽SildingFinishLayout里面子类的touch事件
//			if (moveY - startY > mTouchSlop
//					&& Math.abs((int) moveX - downX) < mTouchSlop) {
//				isRemark = true;
//				return true;
//			}
//			break;
//		}
//		return super.onInterceptTouchEvent(ev);
//	}

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (firstVisibleItem == 0) {
                    isRemark = true;
                    startY = (int) ev.getY();
                    startX = (int) ev.getX();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                onMove(ev);
                break;
            case MotionEvent.ACTION_UP:
                if (state == RELESE) {
                    state = REFLASHING;
                    // 加载最新数据；
                    tempCount = 0;
                    reflashViewByState();
                    iReflashListener.onReflash();
                } else if (state == PULL) {
                    state = NONE;
                    isRemark = false;
                    reflashViewByState();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }


    public void startRefreshing() {
        Log.i("debug", "startRefreshing");
//		topPadding(headerHeight);
        state = REFLASHING;
        // 加载最新数据；
        tempCount = 0;
        post(showRefresh);
        reflashViewByState();
        iReflashListener.onReflash();
    }

    /**
     * 判断移动过程操作；
     *
     * @param ev
     */
    private void onMove(MotionEvent ev) {
        if (!isRemark) {
            return;
        }
        int tempY = (int) ev.getY();
        int space = tempY - startY;
        int spaceX = (int) (startX - ev.getX());
//		Log.i("debug", "space " + space + " mTouchSlop " + mTouchSlop);
        if (space < mTouchSlop) {
            return;
        }

        int topPadding = space - headerHeight;
        spaceMove = topPadding;
        switch (state) {
            case NONE:
                if (space > 0) {
                    state = PULL;
                    reflashViewByState();
                }
                break;
            case PULL:
                topPadding(topPadding);
                if (space > headerHeight + 30
                        && scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    state = RELESE;
                    reflashViewByState();
                }
                break;
            case RELESE:
                topPadding(topPadding);
                if (space < headerHeight + 30) {
                    state = PULL;
                    reflashViewByState();
                } else if (space <= 0) {
                    state = NONE;
                    isRemark = false;
                    reflashViewByState();
                }
                break;
        }
    }

    /**
     * 根据当前状态，改变界面显示；
     */
    private void reflashViewByState() {
        TextView tip = (TextView) header.findViewById(R.id.tip);
        ImageView arrow = (ImageView) header.findViewById(R.id.arrow);
        ProgressBar progress = (ProgressBar) header.findViewById(R.id.progress);
        RotateAnimation anim = new RotateAnimation(0, 180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(500);
        anim.setFillAfter(true);
        RotateAnimation anim1 = new RotateAnimation(180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        anim1.setDuration(500);
        anim1.setFillAfter(true);
        switch (state) {
            case NONE:
                arrow.clearAnimation();
//			topPadding(-headerHeight);
                post(smoothToOrigin);
                break;

            case PULL:
                arrow.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                tip.setText("下拉可以刷新！");
                arrow.clearAnimation();
                arrow.setAnimation(anim1);
                break;
            case RELESE:
                arrow.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                tip.setText("松开可以刷新！");
                arrow.clearAnimation();
                arrow.setAnimation(anim);
                break;
            case REFLASHING:
//			topPadding(headerHeight);
                post(smoothToRefreshStag);
                arrow.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                tip.setText("正在刷新...");
                arrow.clearAnimation();
                break;
        }
    }

    /**
     * 获取完数据；
     */
    public void reflashComplete() {//取消提示
        state = NONE;
        isRemark = false;
        reflashViewByState();
        TextView lastupdatetime = (TextView) header
                .findViewById(R.id.lastupdate_time);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date = new Date(System.currentTimeMillis());
        String time = format.format(date);
        lastupdatetime.setText(time);
    }
    public void loadComplete() {
        footer.findViewById(R.id.footer_layout).setVisibility(View.GONE);
        isLoading = false;
    }

    public void setInterface(IReflashListener iReflashListener){
        this.iReflashListener = iReflashListener;
    }
    public void setLoadMoreInterface(ILoadMoreDataListener iLoadMoreDataListener){
        this.iLoadMoreDataListener = iLoadMoreDataListener;
    }

    /**
     * 刷新数据接口
     * @author Administrator
     */
    public interface IReflashListener{
        void onReflash();
    }
    public interface ILoadMoreDataListener{
        void onLoadMoreData();
    }

}

