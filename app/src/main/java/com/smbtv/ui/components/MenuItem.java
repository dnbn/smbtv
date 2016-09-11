package com.smbtv.ui.components;

public class MenuItem {

    private MenuAction mAction;
    private String mTitle;
    private String mDetail;
    private int mIcon;
    private Object mElement;

    public MenuItem() {

        super();
    }

    public MenuItem(MenuAction action, String title, int icon) {
        this.mAction = action;
        this.mTitle = title;
        this.mIcon = icon;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String text) {
        this.mTitle = text;
    }

    public MenuAction getAction() {
        return mAction;
    }

    public void setAction(MenuAction action) {
        this.mAction = action;
    }

    public String getDetail() {
        return mDetail;
    }

    public void setDetail(String mDetail) {
        this.mDetail = mDetail;
    }

    public int getIcon() {
        return mIcon;
    }

    public void setIcon(int icon) {
        this.mIcon = icon;
    }

    public Object getElement() {
        return mElement;
    }

    public void setElement(Object mElement) {
        this.mElement = mElement;
    }
}