
package com.unicornindonesia.wonderfulindonesia.ui;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;
import com.unicornindonesia.wonderfulindonesia.R;
import ibmmobileappbuilder.behaviors.ShareBehavior;
import ibmmobileappbuilder.util.image.ImageLoader;
import ibmmobileappbuilder.util.image.PicassoImageLoader;
import ibmmobileappbuilder.util.StringUtils;
import java.net.URL;
import static ibmmobileappbuilder.util.image.ImageLoaderRequest.Builder.imageLoaderRequest;
import ibmmobileappbuilder.ds.Datasource;
import ibmmobileappbuilder.ds.SearchOptions;
import ibmmobileappbuilder.ds.filter.Filter;
import java.util.Arrays;
import com.unicornindonesia.wonderfulindonesia.ds.DatanotesDSSchemaItem;
import ibmmobileappbuilder.ds.CloudantDatasource;
import ibmmobileappbuilder.cloudant.factory.CloudantDatastoresFactory;
import java.net.URI;

public class WonderfulIndonesiaDetailFragment extends ibmmobileappbuilder.ui.DetailFragment<DatanotesDSSchemaItem> implements ShareBehavior.ShareListener  {
//
    Button btnSpeech;
    StreamPlayer streamPlayer;
    String text_to_speech;

    private TextToSpeech initTextToSpeechService(){
        TextToSpeech service = new TextToSpeech();
        String username = "b79b07b3-3e60-41c4-99ac-8f81467222ee";
        String password = "DrMNemDDn5cO";
        service.setUsernameAndPassword(username, password);
        return service;
    }

    private class WatsonTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String...textToSpeak){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btnSpeech.setText("Listening...");
                }
            });

            TextToSpeech textToSpeech = initTextToSpeechService();
            streamPlayer = new StreamPlayer();
            streamPlayer.playStream(textToSpeech.synthesize(text_to_speech, Voice.EN_ALLISON).execute());
            return "text to speech done";
        }

        @Override
        protected void onPostExecute(String result){
            btnSpeech.setText("Speak Up!");
        }
    }
//
    private Datasource<DatanotesDSSchemaItem> datasource;
    public static WonderfulIndonesiaDetailFragment newInstance(Bundle args){
        WonderfulIndonesiaDetailFragment fr = new WonderfulIndonesiaDetailFragment();
        fr.setArguments(args);

        return fr;
    }

    public WonderfulIndonesiaDetailFragment(){
        super();
    }

    @Override
    public Datasource<DatanotesDSSchemaItem> getDatasource() {
        if (datasource != null) {
            return datasource;
    }
       datasource = CloudantDatasource.cloudantDatasource(
              CloudantDatastoresFactory.create("data_notes"),
              URI.create("https://d474c95f-a46f-4ee2-8388-ed0e6c2a0462-bluemix:cd451352bbaa74569fc773726e43193b37bde09e1ed48cf4be4d1badd53396ea@d474c95f-a46f-4ee2-8388-ed0e6c2a0462-bluemix.cloudant.com/data_notes/"),
              DatanotesDSSchemaItem.class,
              new SearchOptions(),
              "judul", "notes", "sampul");
        return datasource;
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        addBehavior(new ShareBehavior(getActivity(), this));

    }

    // Bindings

    @Override
    protected int getLayout() {
        return R.layout.wonderfulindonesiadetail_detail;
    }

    @Override
    @SuppressLint("WrongViewCast")
    public void bindView(final DatanotesDSSchemaItem item, View view) {
        if (item.judul != null){
            
            TextView view0 = (TextView) view.findViewById(R.id.view0);
            view0.setText(item.judul);
            
        }
        
        ImageView view1 = (ImageView) view.findViewById(R.id.view1);
        URL view1Media = StringUtils.parseUrl(item.sampul);
        if(view1Media != null){
            ImageLoader imageLoader = new PicassoImageLoader(view1.getContext());
            imageLoader.load(imageLoaderRequest()
                                   .withPath(view1Media.toExternalForm())
                                   .withTargetView(view1)
                                   .fit()
                                   .build()
                    );
            
        } else {
            view1.setImageDrawable(null);
        }
        if (item.notes != null){
            
            TextView view2 = (TextView) view.findViewById(R.id.view2);
            view2.setText(item.notes);
            //
            btnSpeech = (Button) view.findViewById(R.id.btnSpeech);
            btnSpeech.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick (View v){
                    try{
                        text_to_speech = item.notes;
                        WatsonTask task = new WatsonTask();
                        task.execute(new String[]{});
                    } catch (Exception e){
                        System.err.println(e);
                    }
                }
            });
            //
        }
    }

    @Override
    protected void onShow(DatanotesDSSchemaItem item) {
        // set the title for this fragment
        getActivity().setTitle("Wonderfull Indonesia");
    }
    @Override
    public void onShare() {
        DatanotesDSSchemaItem item = getItem();

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");

        intent.putExtra(Intent.EXTRA_TEXT, (item.judul != null ? item.judul : "" ) + "\n" +
                    (item.notes != null ? item.notes : "" ));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Wonderfull Indonesia");
        startActivityForResult(Intent.createChooser(intent, getString(R.string.share)), 1);
    }
}
