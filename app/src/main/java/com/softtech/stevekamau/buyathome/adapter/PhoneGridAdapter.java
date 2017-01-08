package com.softtech.stevekamau.buyathome.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.PopupMenu;
import android.util.Base64;
import android.util.Log;
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
import com.softtech.stevekamau.buyathome.interfaces.OnOptionsSelectedInterface;
import com.softtech.stevekamau.buyathome.model.PhoneModel;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
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
    private OnOptionsSelectedInterface onOptionsSelected;
    private DisplayImageOptions options;

    public PhoneGridAdapter(Activity activity, List<PhoneModel> modelItems, OnOptionsSelectedInterface onOptionsSelected) {
        this.activity = activity;
        this.modelItems = modelItems;
        this.onOptionsSelected = onOptionsSelected;

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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.grid_two_cells, null);

        ImageView thumbNail = (ImageView) convertView
                .findViewById(R.id.thumbnail);
        final TextView title = (TextView) convertView.findViewById(R.id.title);
        final TextView amount = (TextView) convertView.findViewById(R.id.amount);
        final PhoneModel m = modelItems.get(position);
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
        ImageView m_options = (ImageView) convertView.findViewById(R.id.options);
        m_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("phones_frag", "reached");
                showPopup(view, position, m.getImage_url());

            }
        });
        thumbNail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("phones_frag1", "reached");
                Intent intent = new Intent(activity, Details.class);
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
        double d_amount = Double.parseDouble(m.getAmount());
        DecimalFormat formatter = new DecimalFormat("#,###");

        amount.setText(formatter.format(d_amount));
        return convertView;
    }

    private void showPopup(View v, final int position, final String image_url) {
        final PhoneModel m = modelItems.get(position);
        PopupMenu popup = new PopupMenu(activity, v);
        // Inflate the menu from xml
        popup.getMenuInflater().inflate(R.menu.popup_grid, popup.getMenu());
        // Setup menu item selection
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.share_this:
                        (onOptionsSelected).onShareButtonclicked(m.getName()
                                , m.getAmount());
                        return true;
                    case R.id.add_to_cart:
                        //get Image
                       /* thumbNail.buildDrawingCache();
                        Bitmap bmap = thumbNail.getDrawingCache();
                        String encodedImageData = getEncoded64ImageStringFromBitmap(bmap);*/

                        (onOptionsSelected).onAddToCartButtonClicked(m.getId(), m.getName(), m.getAmount(), m.getDetails(),
                                image_url, "1", m.getAmount());
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

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }


}
