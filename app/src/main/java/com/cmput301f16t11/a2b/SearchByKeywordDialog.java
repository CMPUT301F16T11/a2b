package com.cmput301f16t11.a2b;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.RadioGroup;

/**
 * Created by john on 22/11/16.
 */

public class SearchByKeywordDialog extends Dialog {

    public enum searchCriteria{
        START,
        END,
        DESCRIPTION,
        NOT_SET
    }

    private searchCriteria selectedCriteria;

    SearchByKeywordDialog(Context context){
        super(context);
        setContentView(R.layout.search_by_keyword);
        selectedCriteria = searchCriteria.DESCRIPTION;

        setOnClickListeners(context);
    }

    private void setOnClickListeners(Context context){
        final RadioGroup selectorGroup = (RadioGroup)findViewById(R.id.searchGroup);
        final Button okButton = (Button) findViewById(R.id.okKeyword);
        final Button cancelButton= (Button) findViewById(R.id.cancelKeyword);

        selectorGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.descriptionSelect:{
                        selectedCriteria = searchCriteria.DESCRIPTION;
                       return;
                    }
                    case R.id.startSelect:{
                        selectedCriteria = searchCriteria.START;
                       return;
                    }
                    case R.id.endSelect:{
                        selectedCriteria = searchCriteria.END;
                       return;
                    }
                    default:{
                        selectedCriteria = searchCriteria.NOT_SET;
                       return;
                    }
                }
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If this is set the parent activity will know the user cancelled
                selectedCriteria = searchCriteria.NOT_SET;
                dismiss();
            }
        });
    }

    public searchCriteria getSelectedCriteria(){
        return selectedCriteria;
    }
}
