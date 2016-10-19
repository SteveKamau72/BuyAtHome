package com.softtech.stevekamau.buyathome.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by Steve Kamau on 16-Jul-15.
 */


public class NewGridAdapter extends RecyclerView.Adapter<NewGridAdapter.ViewHolder> {
    public List<NewModel> modelItems;
    public Activity activity;
    //int position;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    // Provide a suitable constructor (depends on the kind of dataset)
    public NewGridAdapter(Activity activity, List<NewModel> modelItems) {
        this.modelItems = modelItems;
        this.activity = activity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NewGridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_cell, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        NewModel m = modelItems.get(position);
        holder.p_id.setText("" + m.getId());
        holder.title.setText(m.getName());
        holder.thumbNail.setImageUrl(m.getImage_url(), imageLoader);
        holder.amount.setText(m.getAmount());
        holder.description.setText(m.getDetails());
        holder.img1.setText(m.getImage_url());
        holder.img2.setText(m.getImage_url());
        holder.img3.setText(m.getImage_url());
        holder.img4.setText(m.getImage_url());
        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return modelItems.size();
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

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final Context context;
        // each data item is just a string in this case
        public TextView p_id, title, amount, description, img1, img2, img3, img4;
        public NetworkImageView thumbNail;
        public ImageView options;
        public CardView cardView;
        public List<NewModel> modelItems;

        public ViewHolder(View v) {
            super(v);
            p_id = (TextView) v.findViewById(R.id.p_id);
            title = (TextView) v.findViewById(R.id.title);
            thumbNail = (NetworkImageView) itemView.findViewById(R.id.thumbnail);
            amount = (TextView) itemView.findViewById(R.id.amount);
            description = (TextView) itemView.findViewById(R.id.description);
            options = (ImageView) itemView.findViewById(R.id.options);
            cardView = (CardView) itemView.findViewById(R.id.card);
            img1 = (TextView) v.findViewById(R.id.img1);
            img2 = (TextView) v.findViewById(R.id.img2);
            img3 = (TextView) v.findViewById(R.id.img3);
            img4 = (TextView) v.findViewById(R.id.img4);
            thumbNail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext(), Details.class);
                    intent.putExtra("p_id", p_id.getText().toString());
                    intent.putExtra("title", title.getText().toString());
                    intent.putExtra("amount", amount.getText().toString());
                    intent.putExtra("description", description.getText().toString());
                    intent.putExtra("image1_url", img1.getText().toString());
                    context.startActivity(intent);
                }
            });
            context = itemView.getContext();
        }

        @Override
        public void onClick(View v) {
            if (v instanceof CardView) {
               /* Bitmap b = ((BitmapDrawable) thumbNail.getDrawable()).getBitmap();
                if (b == null) {
                } else {
                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
                    b.compress(Bitmap.CompressFormat.PNG, 100, bs);
                }*/


            }
        }
    }

}