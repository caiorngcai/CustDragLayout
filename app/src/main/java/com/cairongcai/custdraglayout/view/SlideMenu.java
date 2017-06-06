package com.cairongcai.custdraglayout.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by crc on 2017/6/6.
 */

public class SlideMenu extends ViewGroup {
    private Scroller scroller;
    private float downX;
    private float moveX;
    public static final int MAIN_STATE=0;
    public static final int MENU_STATE=1;
    public int currentstate=MAIN_STATE;
    private float dowmY;

    public SlideMenu(Context context) {
        super(context);
        init();
    }
    public SlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        //滚动事件的对象，可以获得关于滚动的一系列数据，比如滚动位移等。
        //view里有getcontext的方法
        scroller=new Scroller(getContext());
    }
    /**
     * 定义子控件摆放的位置
     * @param widthMeasureSpec 当前控件的高度
     * @param heightMeasureSpec 当前控件的宽度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //指定左面板的宽高
        View leftMenu=getChildAt(0);
        leftMenu.measure(leftMenu.getLayoutParams().width,heightMeasureSpec);
        //指定主面板的宽高
        final View mainCntent = getChildAt(1);
        mainCntent.measure(widthMeasureSpec,heightMeasureSpec);
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View leftMenu=getChildAt(0);
        leftMenu.layout(-leftMenu.getMeasuredWidth(),0,0,b);
        getChildAt(1).layout(l,t,r,b);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                System.out.println("当前位置："+downX);
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = event.getX();
                System.out.println("移动按下位置:"+moveX);
                int scrollX= (int) (downX-moveX);
                System.out.println("将要移动位置:"+scrollX);
                int newScrollXPosition= (int) (getScrollX()+scrollX);
                if(newScrollXPosition<-getChildAt(0).getMeasuredWidth())
                {
                    scrollTo(-getChildAt(0).getMeasuredWidth(),0);
                }else if(newScrollXPosition>0) {
                    scrollTo(0, 0);
                } else{
                    scrollBy(scrollX,0);
                }
                downX=moveX;//更新X的位置
                break;
            case MotionEvent.ACTION_UP:
                int leftCenter= (int) (-getChildAt(0).getMeasuredWidth()/2.0f);
                if(getScrollX()<leftCenter)
                {
                    currentstate=MENU_STATE;
                    updateCurrentContent();
                }else {
                    currentstate=MAIN_STATE;
                    updateCurrentContent();
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void updateCurrentContent() {
        int startX=getScrollX();
        int dx=0;
        switch (currentstate)
        {
            case MAIN_STATE:
                dx=0-startX;
                break;
            case MENU_STATE:
                dx=-getChildAt(0).getMeasuredWidth()-startX;
                break;
        }
        int duration=Math.abs(dx*2);
        scroller.startScroll(startX,0,dx,0,duration);
        invalidate();//重绘页面-drawchild-computeScroll
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(scroller.computeScrollOffset())//动画结束前都返回TRUE
        {
            int currx=scroller.getCurrX();//得到偏移量
            System.out.println("currx"+currx);
           scrollTo(currx,0);
            invalidate();//循环
        }
    }
    public void open()
    {
        currentstate=MENU_STATE;
        updateCurrentContent();
    }
    public void close()
    {
        currentstate=MAIN_STATE;
        updateCurrentContent();
    }
    public void switchState()
    {
        if(currentstate==MAIN_STATE)
        {
            open();
        }else {
            close();
        }
    }
    public int getCurrentState()
    {
        return currentstate;
    }

    /**
     * 拦截判断
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                downX=ev.getX();
                dowmY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //x偏移量大于5，并且x偏移量大于y偏移量，才响应主页面的触摸事件。
                float xOffset=Math.abs(ev.getX()-downX);
                float yOffset=Math.abs(ev.getY()-dowmY);
                if(xOffset>yOffset&&xOffset>5)
                {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
