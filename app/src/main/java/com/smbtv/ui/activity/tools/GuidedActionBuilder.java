package com.smbtv.ui.activity.tools;

import android.support.v17.leanback.widget.GuidedAction;
import android.text.InputType;

public class GuidedActionBuilder {

    public static GuidedAction editText(long id, String title, String desc) {

        return new GuidedAction.Builder()
                .id(id)
                .title(title)
                .description(desc)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .descriptionEditable(true)
                .build();
    }

    public static GuidedAction editNumber(long id, String title, int desc) {

        return new GuidedAction.Builder()
                .id(id)
                .title(title)
                .description(Integer.toString(desc))
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .descriptionEditable(true)
                .build();
    }

    public static GuidedAction text(long id, String title, String desc) {

        return new GuidedAction.Builder()
                .id(id)
                .title(title)
                .description(desc)
                .descriptionEditable(false)
                .build();
    }

}
