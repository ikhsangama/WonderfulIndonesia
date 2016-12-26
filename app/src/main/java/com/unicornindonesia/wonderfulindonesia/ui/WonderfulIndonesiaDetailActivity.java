

package com.unicornindonesia.wonderfulindonesia.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import ibmmobileappbuilder.ui.BaseDetailActivity;

/**
 * WonderfulIndonesiaDetailActivity detail activity
 */
public class WonderfulIndonesiaDetailActivity extends BaseDetailActivity {

  	@Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        if(isTaskRoot()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected Class<? extends Fragment> getFragmentClass() {
        return WonderfulIndonesiaDetailFragment.class;
    }
}

