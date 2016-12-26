package com.unicornindonesia.wonderfulindonesia.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.unicornindonesia.wonderfulindonesia.R;
import ibmmobileappbuilder.ds.Datasource;
import ibmmobileappbuilder.ui.ListGridFragment;
import ibmmobileappbuilder.util.image.ImageLoader;
import ibmmobileappbuilder.util.image.PicassoImageLoader;
import ibmmobileappbuilder.util.StringUtils;
import ibmmobileappbuilder.util.ViewHolder;
import java.net.URL;
import static ibmmobileappbuilder.util.image.ImageLoaderRequest.Builder.imageLoaderRequest;
import ibmmobileappbuilder.ds.SearchOptions;
import ibmmobileappbuilder.ds.filter.Filter;
import java.util.Arrays;
import com.unicornindonesia.wonderfulindonesia.ds.DatanotesDSSchemaItem;
import ibmmobileappbuilder.ds.CloudantDatasource;
import ibmmobileappbuilder.cloudant.factory.CloudantDatastoresFactory;
import java.net.URI;
import android.content.Intent;
import ibmmobileappbuilder.util.Constants;
import static ibmmobileappbuilder.util.NavigationUtils.generateIntentToAddOrUpdateItem;

/**
 * "WonderfulIndonesiaFragment" listing
 */
public class WonderfulIndonesiaFragment extends ListGridFragment<DatanotesDSSchemaItem>  {

    private Datasource<DatanotesDSSchemaItem> datasource;


    public static WonderfulIndonesiaFragment newInstance(Bundle args) {
        WonderfulIndonesiaFragment fr = new WonderfulIndonesiaFragment();

        fr.setArguments(args);
        return fr;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }

    protected SearchOptions getSearchOptions() {
        SearchOptions.Builder searchOptionsBuilder = SearchOptions.Builder.searchOptions();
        return searchOptionsBuilder.build();
    }


    /**
    * Layout for the list itselft
    */
    @Override
    protected int getLayout() {
        return R.layout.fragment_list;
    }

    /**
    * Layout for each element in the list
    */
    @Override
    protected int getItemLayout() {
        return R.layout.wonderfulindonesia_item;
    }

    @Override
    protected Datasource<DatanotesDSSchemaItem> getDatasource() {
        if (datasource != null) {
            return datasource;
        }
       datasource = CloudantDatasource.cloudantDatasource(
              CloudantDatastoresFactory.create("data_notes"),
              URI.create("https://d474c95f-a46f-4ee2-8388-ed0e6c2a0462-bluemix:cd451352bbaa74569fc773726e43193b37bde09e1ed48cf4be4d1badd53396ea@d474c95f-a46f-4ee2-8388-ed0e6c2a0462-bluemix.cloudant.com/data_notes/"),
              DatanotesDSSchemaItem.class,
              getSearchOptions(),
              "judul", "notes", "sampul");
        return datasource;
    }

    @Override
    protected void bindView(DatanotesDSSchemaItem item, View view, int position) {
        
        ImageLoader imageLoader = new PicassoImageLoader(view.getContext());
        ImageView image = ViewHolder.get(view, R.id.image);
        URL imageMedia = StringUtils.parseUrl(item.sampul);
        if(imageMedia != null){
            imageLoader.load(imageLoaderRequest()
                          .withPath(imageMedia.toExternalForm())
                          .withTargetView(image)
                          .fit()
                          .build()
            );
        	
        }
        else {
            imageLoader.load(imageLoaderRequest()
                          .withResourceToLoad(R.drawable.ic_ibm_placeholder)
                          .withTargetView(image)
                          .build()
            );
        }
        
        
        TextView title = ViewHolder.get(view, R.id.title);
        
        if (item.judul != null){
            title.setText(item.judul);
            
        }
    }


    @Override
    public void showDetail(DatanotesDSSchemaItem item, int position) {
        // If we have forms, then we have to refresh when an item has been edited
        // Also with this we support list without details
        Bundle args = new Bundle();
        args.putInt(Constants.ITEMPOS, position);
        args.putParcelable(Constants.CONTENT, item);
        Intent intent = new Intent(getActivity(), WonderfulIndonesiaDetailActivity.class);
        intent.putExtras(args);

        if (!getResources().getBoolean(R.bool.tabletLayout)) {
            startActivityForResult(intent, Constants.DETAIL);
        } else {
            startActivity(intent);
        }
    }

}
