package com.softtech.stevekamau.buyathome.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.softtech.stevekamau.buyathome.model.HotDealsModel;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by steve on 12/31/16.
 */

/**
 * Created by steve on 12/25/16.
 */

public class MoreHotDealsAdapter extends RecyclerView.Adapter<MoreHotDealsAdapter.ViewHolder> {
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private List<HotDealsModel> modelItems;
    private Activity activity;
    private OnOptionsSelectedInterface onOptionsSelected;
    private DisplayImageOptions options;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    // Provide a suitable constructor (depends on the kind of dataset)
    public MoreHotDealsAdapter(Activity activity, List<HotDealsModel> modelItems, OnOptionsSelectedInterface onOptionsSelected) {
        this.modelItems = modelItems;
        this.activity = activity;
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

    // Create new views (invoked by the layout manager)
    @Override
    public MoreHotDealsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_two_cells_deals, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final HotDealsModel m = modelItems.get(position);
        holder.title.setText(m.getName());
        // thumbnail image
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
            holder.amount.setText("");
            holder.percentTxt.setVisibility(View.GONE);
        } else {
            double amount = Double.parseDouble(m.getDiscountedAmount());
            DecimalFormat formatter = new DecimalFormat("#,###");

            double amount1 = Double.parseDouble(m.getAmount());
            DecimalFormat formatter1 = new DecimalFormat("#,###");

            holder.amount.setText(formatter.format(amount));
            holder.amount1.setText(formatter1.format(amount1));
            holder.amount1.setPaintFlags(holder.amount1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.ksh1.setPaintFlags(holder.ksh1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            double percentageValue = ((amount1 - amount) / amount1) * 100;
            holder.percentTxt.setText((int) percentageValue + "%");
        }

        holder.m_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, position, holder, m.getImage_url());
            }
        });

        holder.thumbNail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Details.class);
                intent.putExtra("p_id", m.getId());
                intent.putExtra("title", m.getName());
                intent.putExtra("amount", m.getAmount());
                intent.putExtra("description", m.getDetails());
                intent.putExtra("image1_url", m.getImage_url());
                // TODO: 12/6/16 this is totally dummy data
                intent.putExtra("rating", m.getRatings());
                activity.startActivity(intent);
            }
        });

    }

    // Return th
    // e size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return modelItems.size();
    }

    private void showPopup(View v, final int position, final ViewHolder holder, final String image_url) {
        final HotDealsModel m = modelItems.get(position);
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
                        (onOptionsSelected).onAddToCartButtonClicked(m.getId(), m.getName(), m.getAmount(), m.getDetails(),
                                image_url, "1", m.getDiscountedAmount());
                        return true;
                    case R.id.add_to_wishlist:
                        (onOptionsSelected).onAddToWishList(m.getId(), m.getName(), m.getAmount(), m.getDetails(),
                                m.getImage_url(), "1", m.getDiscountedAmount());
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Context context;
        // each data item is just a string in this case
        public TextView p_id, title, amount, description, img1, img2, img3, img4, amount1, ksh1, percentTxt;
        public ImageView thumbNail;
        public ImageView m_options;
        public CardView cardView;

        public ViewHolder(View v) {
            super(v);
            p_id = (TextView) v.findViewById(R.id.p_id);
            title = (TextView) v.findViewById(R.id.title);
            thumbNail = (ImageView) itemView.findViewById(R.id.thumbnail);
            amount = (TextView) itemView.findViewById(R.id.amount);
            amount1 = (TextView) itemView.findViewById(R.id.amount1);
            ksh1 = (TextView) itemView.findViewById(R.id.ksh1);
            percentTxt = (TextView) itemView.findViewById(R.id.percentTxt);
            // description = (TextView) itemView.findViewById(R.id.description);
            m_options = (ImageView) itemView.findViewById(R.id.options);
            cardView = (CardView) itemView.findViewById(R.id.card);
            context = itemView.getContext();

        }
    }
}