package com.softtech.stevekamau.buyathome.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.activites.Details;
import com.softtech.stevekamau.buyathome.app.AppController;
import com.softtech.stevekamau.buyathome.model.NewModel;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Steve Kamau on 11-Mar-15.
 */
public class CustomGridAdapter extends BaseAdapter {
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private Activity activity;
    private LayoutInflater inflater;
    private List<NewModel> modelItems;
    private DisplayImageOptions options;

    public CustomGridAdapter(Activity activity, List<NewModel> modelItems) {
        this.activity = activity;
        this.modelItems = modelItems;

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
            convertView = inflater.inflate(R.layout.grid_two_cells, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        ImageView thumbNail = (ImageView) convertView.findViewById(R.id.thumbnail);
        final TextView title = (TextView) convertView.findViewById(R.id.title);
        final TextView amount = (TextView) convertView.findViewById(R.id.amount);
        final NewModel m = modelItems.get(position);
        // thumbnail image
        //download from the url
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
        thumbNail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getApplicationContext(), Details.class);
                intent.putExtra("p_id", m.getId());
                intent.putExtra("title", m.getName());
                intent.putExtra("amount", m.getAmount());
                intent.putExtra("description", m.getDetails());
                intent.putExtra("image1_url", m.getImage_url());
                intent.putExtra("rating", m.getRatings());
                activity.startActivity(intent);
            }
        });
        // title
        title.setText(m.getName());
        //amount
        double d_amount = Double.parseDouble(m.getAmount());
        DecimalFormat formatter = new DecimalFormat("#,###");
        amount.setText(formatter.format(d_amount));

        ImageView options = (ImageView) convertView.findViewById(R.id.options);
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });

        return convertView;
    }

    private void showPopup(View v) {
        PopupMenu popup = new PopupMenu(activity, v);
        // Inflate the menu from xml
        popup.getMenuInflater().inflate(R.menu.popup_grid, popup.getMenu());
        // Setup menu item selection
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.share_this:

                        return true;
                    case R.id.add_to_cart:
                        return true;
                    case R.id.add_to_wishlist:
                        return true;
                    default:
                        return false;
                }
            }
        });
        // Handle dismissal with: popup.setOnDismissListener(...);
        // Show the menu
        popup.show();
    }

}
