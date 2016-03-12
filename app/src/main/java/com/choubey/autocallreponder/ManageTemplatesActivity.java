package com.choubey.autocallreponder;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.choubey.autocallreponder.db.TemplatesDbDao;
import com.choubey.autocallreponder.db.UserTemplatesData;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ManageTemplatesActivity extends ActionBarActivity {
    private TemplateDetailsDisplayManager detailsDisplayManager = null;
    private static final String Y = "Y";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Activity context = this;
        detailsDisplayManager = TemplateDetailsDisplayManager.createAndGetNewInstance(context);
        List<UserTemplatesData> userTemplatesDataList = TemplatesDbDao.queryAndGetTemplatesData(context);
        Log.i(this.getLocalClassName(), "Queried the db to get all the user templates. Populating this data into the scrollview");
        final ScrollView manageTemplatesScrollView = new ScrollView(context);
        manageTemplatesScrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        manageTemplatesScrollView.setBackgroundColor(Color.parseColor("#d93d455b"));

        LinearLayout outerLinearLayout = new LinearLayout(context);
        outerLinearLayout.setOrientation(LinearLayout.VERTICAL);
        outerLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        Collections.sort(userTemplatesDataList, new CustomComparator());
        for(final UserTemplatesData userTemplatesData: userTemplatesDataList) {
            LinearLayout rowDataLinearLayout = new LinearLayout(context);
            String templateActive = userTemplatesData.getStatus().name();
            int textColor = Y.equals(templateActive) ? Color.WHITE : Color.GRAY;

            rowDataLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;
            rowDataLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT));
            rowDataLinearLayout.setWeightSum(1.0f);

            TextView nameTextView = new TextView(context);
            nameTextView.setGravity(Gravity.CENTER);
            nameTextView.setText(userTemplatesData.getContactName());
            nameTextView.setTextColor(textColor);
            nameTextView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.3f));

            TextView messageTextView = new TextView(context);
            messageTextView.setGravity(Gravity.CENTER);
            messageTextView.setText(userTemplatesData.getMessage());
            messageTextView.setTextColor(textColor);
            messageTextView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f));

            final Button detailsButton = new Button(context);
            detailsButton.setText("...");
            detailsButton.setTextColor(textColor);
            detailsButton.setGravity(Gravity.CENTER);
            detailsButton.setBackgroundColor(Color.TRANSPARENT);
            detailsButton.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f));
            detailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    detailsDisplayManager.showDetailsAsPopup(manageTemplatesScrollView, context, userTemplatesData);
                }
            });

            rowDataLinearLayout.addView(nameTextView);
            rowDataLinearLayout.addView(messageTextView);
            rowDataLinearLayout.addView(detailsButton);

            outerLinearLayout.addView(rowDataLinearLayout);
        }

        manageTemplatesScrollView.addView(outerLinearLayout);
        setContentView(manageTemplatesScrollView);
        Log.i(this.getLocalClassName(), "Read all data and displayed on UI");
    }

    private class CustomComparator implements Comparator<UserTemplatesData>
    {
        @Override
        public int compare(UserTemplatesData lhs, UserTemplatesData rhs) {
            if(lhs == null || rhs == null)
            {
                return 0;
            }

            if(lhs.getStatus() == rhs.getStatus())
            {
                return lhs.getTemplateId().compareTo(rhs.getTemplateId());
            }

            if(UserTemplatesData.ActiveStatus.Y == lhs.getStatus()
                    && UserTemplatesData.ActiveStatus.N == rhs.getStatus())
            {
                return -1;
            }

            return 1;
        }
    }
}
