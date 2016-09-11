package com.smbtv.ui.components;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.Presenter;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ViewGroup;

import com.hitherejoe.leanbackcards.IconCardView;
import com.smbtv.R;


public class MenuItemPresenter extends Presenter {

    private static final String TAG = MenuItemPresenter.class.getName();

    private static final int IMAGE_WIDTH = 350;
    private static final int IMAGE_HEIGHT = 400;

    private Drawable mDefaultCardImage;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {

        Log.d(TAG, "onCreateViewHolder");

        final Context context = parent.getContext();
        final IconCardView iconCardView = new IconCardView(context, R.style.IconCardStyle);

        iconCardView.setFocusable(true);
        iconCardView.setFocusableInTouchMode(true);
        iconCardView.setMainImageDimensions(IMAGE_WIDTH, IMAGE_HEIGHT);

        return new ViewHolder(iconCardView);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {

        Log.d(TAG, "onBindViewHolder");

        if (!(item instanceof MenuItem)) {

            throw new IllegalArgumentException("item must be instance of MenuItem, not " + item.getClass().getName());
        }

        IconCardView iconCardView = (IconCardView) viewHolder.view;

        MenuItem menuItem = (MenuItem) item;
        iconCardView.setTitleText(menuItem.getTitle());
        iconCardView.setDetailText(menuItem.getDetail());

        final Context context = iconCardView.getContext();
        final Drawable icon = ContextCompat.getDrawable(context, menuItem.getIcon());
        iconCardView.setIcon(icon);
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {

        Log.d(TAG, "onUnbindViewHolder");
    }
}
