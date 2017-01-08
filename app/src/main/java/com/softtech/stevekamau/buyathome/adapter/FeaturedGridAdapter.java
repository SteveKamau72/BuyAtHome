package com.softtech.stevekamau.buyathome.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.activites.FeaturedDetails;
import com.softtech.stevekamau.buyathome.app.AppController;
import com.softtech.stevekamau.buyathome.model.FeaturedModel;
import com.softtech.stevekamau.buyathome.model.FeaturedModel;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by steve on 3/21/16.
 */

public class FeaturedGridAdapter extends RecyclerView.Adapter<FeaturedGridAdapter.ViewHolder> {
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private List<FeaturedModel> modelItems;
    private Activity activity;
    private DisplayImageOptions options;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    // Provide a suitable constructor (depends on the kind of dataset)
    public FeaturedGridAdapter(Activity activity, List<FeaturedModel> modelItems) {
        this.modelItems = modelItems;
        this.activity = activity;
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

    // Create new views (invoked by the layout manager)
    @Override
    public FeaturedGridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.featured_grid_cell, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final FeaturedModel m = modelItems.get(position);
        holder.mTextView.setText(m.getName());
        //download from the url
        com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                .displayImage(m.getImage_url(), holder.thumbNail, options, new SimpleImageLoadingListener() {
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
        if (m.getAmount().equals("")) {
            holder.amount.setText("FREE");
        } else {
            double amount = Double.parseDouble(m.getAmount());
            DecimalFormat formatter = new DecimalFormat("#,###");

            holder.amount.setText("kshs. " + formatter.format(amount));
        }
        holder.description.setText(m.getDetails());
        holder.thumbNail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity.getApplicationContext(), FeaturedDetails.class);
                intent.putExtra("p_id", m.getId());
                intent.putExtra("title", m.getName());
                intent.putExtra("amount", m.getAmount());
                intent.putExtra("description", m.getDetails());
                intent.putExtra("image1_url", m.getImage_url());
                intent.putExtra("rating", m.getRatings());
                activity.startActivity(intent);
            }
        });
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity.getApplicationContext(), FeaturedDetails.class);
                intent.putExtra("p_id", m.getId());
                intent.putExtra("title", m.getName());
                intent.putExtra("amount", m.getAmount());
                intent.putExtra("description", m.getDetails());
                intent.putExtra("image1_url", m.getImage_url());
                intent.putExtra("rating", m.getRatings());
                activity.startActivity(intent);

            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return modelItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView, amount, description;
        public ImageView thumbNail;
        public Button more;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.title);
            thumbNail = (ImageView) itemView.findViewById(R.id.thumbnail);
            amount = (TextView) itemView.findViewById(R.id.amount);
            description = (TextView) itemView.findViewById(R.id.description);
            more = (Button) itemView.findViewById(R.id.see_more);

        }
    }

}