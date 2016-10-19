package com.softtech.stevekamau.buyathome.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.activites.Details;
import com.softtech.stevekamau.buyathome.app.AppController;
import com.softtech.stevekamau.buyathome.model.NewModel;

import java.util.List;

/**
 * Created by Steve Kamau on 11-Mar-15.
 */
public class CustomGridAdapter extends BaseAdapter {
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private Activity activity;
    private LayoutInflater inflater;
    private List<NewModel> modelItems;
    private int mDefaultImageId;

    public CustomGridAdapter(Activity activity, List<NewModel> modelItems) {
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
        final TextView title = (TextView) convertView.findViewById(R.id.title);
        final TextView amount = (TextView) convertView.findViewById(R.id.amount);
        final NewModel m = modelItems.get(position);
        // thumbnail image
        thumbNail.setImageUrl(m.getImage_url(), imageLoader);
        thumbNail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getApplicationContext(), Details.class);
                intent.putExtra("p_id", m.getId());
                intent.putExtra("title", m.getName());
                intent.putExtra("amount", m.getAmount());
                intent.putExtra("description", m.getDetails());
                intent.putExtra("image1_url", m.getImage_url());
                activity.startActivity(intent);
            }
        });
        // title
        title.setText(m.getName());
        amount.setText(m.getAmount());
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
