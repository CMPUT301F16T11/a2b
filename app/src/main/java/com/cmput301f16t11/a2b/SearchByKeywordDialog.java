package com.cmput301f16t11.a2b;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

/**
 * Created by john on 22/11/16.
 */

@Deprecated
public class SearchByKeywordDialog extends Dialog {

    private String searchString;

    SearchByKeywordDialog(Context context){
        super(context);
        setContentView(R.layout.search_by_keyword);
    }

    public String getSearchString(){
        return searchString;
    }

}
