package com.brightdairy.personal.brightdairy.view.Behavior;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by shuangmusuihua on 2016/9/13.
 */
public class HomeTopSearchBg extends CoordinatorLayout.Behavior<LinearLayout>
{
    public HomeTopSearchBg(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, LinearLayout child, View directTargetChild, View target, int nestedScrollAxes)
    {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, LinearLayout child, View target, float velocityX, float velocityY)
    {
        if(velocityY > 0 && alphaOffset < 255)
        {
            alphaOffset += 50;

            if(alphaOffset > 255)
                alphaOffset = 255;

        } else if (velocityY < 0 && alphaOffset > 0)
        {
            alphaOffset -= 50;

            if(alphaOffset < 0)
                alphaOffset = 0;
        }
        child.setBackgroundColor(Color.argb(alphaOffset, 31 ,155 , 212));

        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    private int alphaOffset = 0;
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, LinearLayout child, View target, int dx, int dy, int[] consumed)
    {
        if(dy > 20 && alphaOffset < 255)
        {
            alphaOffset += 10;

            if(alphaOffset > 255)
                alphaOffset = 255;

        } else if (dy < -20 && alphaOffset > 0)
        {
            alphaOffset -= 10;

            if(alphaOffset < 0)
                alphaOffset = 0;
        }
        child.setBackgroundColor(Color.argb(alphaOffset, 31 ,155 , 212));

        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
    }

}
