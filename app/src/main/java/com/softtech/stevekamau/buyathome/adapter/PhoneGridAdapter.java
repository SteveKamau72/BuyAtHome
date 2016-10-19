package com.softtech.stevekamau.buyathome.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.app.AppController;
import com.softtech.stevekamau.buyathome.model.NewModel;
import com.softtech.stevekamau.buyathome.model.PhoneModel;

import java.util.List;

/**
 * Created by steve on 7/2/16.
 */

public class PhoneGridAdapter extends BaseAdapter {
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private Activity activity;
    private LayoutInflater inflater;
    private List<PhoneModel> modelItems;
    private int mDefaultImageId;

    public PhoneGridAdapter(Activity activity, List<PhoneModel> modelItems) {
        this.activity = activity;
        this.modelItems = modelItems;
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
            convertView = inflater.inflate(R.layout.grid_cell_wider, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView amount = (TextView) convertView.findViewById(R.id.amount);
        PhoneModel m = modelItems.get(position);
        // thumbnail image
        thumbNail.setImageUrl(m.getImage_url(), imageLoader);
        // title
        title.setText(m.getName());
        amount.setText(m.getAmount());
        return convertView;
    }

}
