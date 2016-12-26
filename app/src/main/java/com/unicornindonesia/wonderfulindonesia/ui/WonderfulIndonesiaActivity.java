

package com.unicornindonesia.wonderfulindonesia.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.unicornindonesia.wonderfulindonesia.R;

import ibmmobileappbuilder.ui.BaseListingActivity;
/**
 * WonderfulIndonesiaActivity list activity
 */
public class WonderfulIndonesiaActivity extends BaseListingActivity {

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        if(isTaskRoot()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        setTitle(getString(R.string.wonderfulIndonesiaActivity));
    }

    @Override
    protected Class<? extends Fragment> getFragmentClass() {
        return WonderfulIndonesiaFragment.class;
    }

}
