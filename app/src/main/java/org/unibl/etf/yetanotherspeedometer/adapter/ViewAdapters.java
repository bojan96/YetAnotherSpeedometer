package org.unibl.etf.yetanotherspeedometer.adapter;

import android.view.View;

import androidx.databinding.BindingAdapter;

public class ViewAdapters {

    @BindingAdapter("android:isVisible")
    public static void setIsVisible(View view, Boolean isVisible)
    {
        view.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }
}
