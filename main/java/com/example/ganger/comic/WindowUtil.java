package com.example.ganger.comic;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * 窗体操作工具
 * 界面背景颜色的修改，控件x,y坐标的获取，屏幕宽和高的获取
 * Created by Jelly on 2016/9/15.
 */
public class WindowUtil {
    /**
     * 屏幕宽度
     */
    private int screenWidth;
    /**
     * 屏幕宽度
     */
    private int screenHeight;

    private static class WindowUtilInstance{

        private static WindowUtil instance = new WindowUtil();

    }

    /**
     * 获得单例对象
     * @return
     */
    public static WindowUtil getInstance(){
        return WindowUtilInstance.instance;
    }


    private WindowUtil(){}

    /**
     * 获取屏幕的宽
     * @param context Context
     * @return 屏幕的宽
     */
    public int getScreenWidth(Activity context){
        if(context == null){
            return 0;
        }
        if(screenWidth != 0){
            return screenWidth;
        }
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        return screenWidth;
    }

    /**
     * 获取屏幕的高
     * @param context Context
     * @return 屏幕的高
     */
    public int getScreenHeight(Activity context){
        if(context == null){
            return 0;
        }
        if(screenHeight != 0){
            return  screenHeight;
        }
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenHeight = dm.heightPixels;
        return screenHeight;
    }

    /**
     * 获取控件的位置
     * @param view 控件View
     * @return int[] x,y
     */
    public int[] getViewLocation(View view){
        int[] location = new int[2]; //获取筛选按钮的x坐标
        view.getLocationOnScreen(location);
        return location;
    }


    public int getStateBarHeight(Context context){
        Rect rect= new Rect();
        Activity activity = (Activity) context;
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;
        return  statusBarHeight;
    }

}