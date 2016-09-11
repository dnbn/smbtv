package com.smbtv.ui.components;

import java.util.ArrayList;
import java.util.List;

public class DialogConfig {

    private String title;
    private String description;
    private String beatcrumb;
    private List<DialogItem> items = new ArrayList<>();

    public DialogConfig() {
        super();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<DialogItem> getItems() {
        return items;
    }

    public String getBeatcrumb() {
        return beatcrumb;
    }

    public void setBeatcrumb(String beatcrumb) {
        this.beatcrumb = beatcrumb;
    }

    public void addItem(DialogItem item) {
        this.items.add(item);
    }


    public static class DialogItem {

        private int id;
        private String title;
        private String description;

        public DialogItem(int id, String title) {

            this.id = id;
            this.title = title;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    }
}
