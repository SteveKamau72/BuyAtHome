package com.softtech.stevekamau.buyathome.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.app.AppController;
import com.softtech.stevekamau.buyathome.model.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steve Kamau on 16-Jul-15.
 */
public class SearchListAdapter extends BaseAdapter implements Filterable {
    List<Model> mStringFilterList;
    ValueFilter valueFilter;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private Activity activity;
    private LayoutInflater inflater;
    private List<Model> modelItems;
    private int mDefaultImageId;
    private DisplayImageOptions options;

    public SearchListAdapter(Activity activity, List<Model> modelItems) {
        this.activity = activity;
        this.modelItems = modelItems;
        mStringFilterList = modelItems;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.product_placeholder)
                .showImageForEmptyUri(R.drawable.product_placeholder)
//                .displayer(new RoundedBitmapDisplayer(50))
                .showImageOnFail(R.drawable.product_placeholder)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }


    public void setDefaultImageResId(int defaultImage) {
        mDefaultImageId = defaultImage;
    }

    @Override
    public int getCount() {
        return modelItems.size();
    }

    @Override
    public Object getItem(int location) {
        return modelItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.search_list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        ImageView thumbNail = (ImageView) convertView
                .findViewById(R.id.thumbnail);

        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView desc = (TextView) convertView.findViewById(R.id.desc);
        TextView amount = (TextView) convertView.findViewById(R.id.amount);
        Model m = modelItems.get(position);
        title.setText(m.getName());
        desc.setText(m.getDetails());
        amount.setText(m.getAmount());

        com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                .displayImage(m.getImage_url(), thumbNail, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                    }
                }, new ImageLoadingProgressListener() {
                    @Override
                    public void onProgressUpdate(String imageUri, View view, int current, int total) {

                    }
                });


        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<Model> filterList = new ArrayList<Model>();
                for (int i = 0; i < mStringFilterList.size(); i++)
                    if ((mStringFilterList.get(i).getName().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {


                        Model m = new Model(mStringFilterList.get(i)
                                .getId(), mStringFilterList.get(i)
                                .getName(), mStringFilterList.get(i)
                                .getImage_url(), mStringFilterList.get(i)
                                .getAmount(), mStringFilterList.get(i)
                                .getDetails(), mStringFilterList.get(i)
                                .getTags(), mStringFilterList.get(i)
                                .getRatings());

                        filterList.add(m);
                    }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            modelItems = (List<Model>) results.values;
            notifyDataSetChanged();
        }

    }

}

