package com.softtech.stevekamau.buyathome.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.activites.Details;
import com.softtech.stevekamau.buyathome.activites.FeaturedDetails;
import com.softtech.stevekamau.buyathome.app.AppController;
import com.softtech.stevekamau.buyathome.model.NewModel;

import java.util.List;

/**
 * Created by steve on 3/21/16.
 */

public class FeaturedGridAdapter extends RecyclerView.Adapter<FeaturedGridAdapter.ViewHolder> {
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private List<NewModel> modelItems;
    private Activity activity;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    // Provide a suitable constructor (depends on the kind of dataset)
    public FeaturedGridAdapter(Activity activity, List<NewModel> modelItems) {
        this.modelItems = modelItems;
        this.activity = activity;
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
        final NewModel m = modelItems.get(position);
        holder.mTextView.setText(m.getName());
        holder.thumbNail.setImageUrl(m.getImage_url(), imageLoader);
        holder.amount.setText(m.getAmount());
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
        public NetworkImageView thumbNail;
        public Button more;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.title);
            thumbNail = (NetworkImageView) itemView.findViewById(R.id.thumbnail);
            amount = (TextView) itemView.findViewById(R.id.amount);
            description = (TextView) itemView.findViewById(R.id.description);
            more = (Button) itemView.findViewById(R.id.see_more);

        }
    }

}