package com.softtech.stevekamau.buyathome.adapter;

import android.app.Activity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.app.AppController;
import com.softtech.stevekamau.buyathome.model.ReviewModel;

import java.util.List;

/**
 * Created by steve on 3/24/16.
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    private List<ReviewModel> modelItems;
    private Activity activity;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    // Provide a suitable constructor (depends on the kind of dataset)
    public ReviewsAdapter(Activity activity, List<ReviewModel> modelItems) {
        this.modelItems = modelItems;
        this.activity = activity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_row, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ReviewModel m = modelItems.get(position);
        holder.mTextView.setText(m.getUserReviewName());
        holder.comment.setText(m.getcomment());
        holder.timestamp.setText(m.gettime_stamp());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return modelItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView, timestamp,comment;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.name);
            comment = (TextView) v.findViewById(R.id.review);
            timestamp = (TextView) v.findViewById(R.id.timestamp);
        }
    }
}

