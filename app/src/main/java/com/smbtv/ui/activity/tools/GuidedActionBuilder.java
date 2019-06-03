package com.smbtv.ui.activity.tools;

import android.support.v17.leanback.widget.GuidedAction;
import android.text.InputType;

import java.util.List;

public class GuidedActionBuilder {

    public static GuidedAction editPassword(long id, String title, String desc) {

        return new GuidedAction.Builder()
                .id(id)
                .title(title)
                .description(desc)
                .inputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .descriptionEditable(true)
                .build();
    }

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

    public static GuidedAction checkedSubAction(int id, String title, String desc, boolean checked) {

        GuidedAction guidedAction = new GuidedAction.Builder()
                .title(title)
                .description(desc)
                .checkSetId(0)
                .build();
        guidedAction.setChecked(checked);

        return guidedAction;
    }

    public static GuidedAction dropDownAction(long id, String title, String desc, List<GuidedAction> selectionActions) {

        return new GuidedAction.Builder()
                .id(id)
                .title(title)
                .description(desc)
                .subActions(selectionActions)
                .build();
    }
}
