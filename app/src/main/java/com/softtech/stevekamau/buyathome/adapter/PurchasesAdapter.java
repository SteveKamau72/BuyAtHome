package com.softtech.stevekamau.buyathome.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.activites.PurchaseDetails;
import com.softtech.stevekamau.buyathome.helper.ItemTouchHelperAdapter;
import com.softtech.stevekamau.buyathome.helper.ItemTouchHelperViewHolder;
import com.softtech.stevekamau.buyathome.helper.TimeAgo;
import com.softtech.stevekamau.buyathome.model.PurchasesModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by steve on 12/18/16.
 */

public class PurchasesAdapter extends RecyclerView.Adapter<PurchasesAdapter.ItemViewHolder> implements ItemTouchHelperAdapter {

    static List<PurchasesModel> itemList;
    Activity activity;
    private TextView tvNumber;
    private DisplayImageOptions options;

    public PurchasesAdapter(Activity activity, List<PurchasesModel> itemList) {
        this.activity = activity;
        this.itemList = itemList;

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
    public void onItemDismiss(final int position) {

        final PurchasesModel item = new PurchasesModel();
        item.setUser_name(itemList.get(position).getUser_name());

        notifyItemRemoved(position);
        itemList.remove(position);
        notifyItemRangeChanged(0, getItemCount());
        tvNumber.setText(String.valueOf(itemList.size()));
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(itemList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(itemList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);

    }

    public void addItem(int position, PurchasesModel item) {

        itemList.add(position, item);
        notifyItemInserted(position);
        tvNumber.setText(String.valueOf(itemList.size()));

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public void onBindViewHolder(final PurchasesAdapter.ItemViewHolder itemViewHolder, final int position) {

        final PurchasesModel item = itemList.get(position);
        itemViewHolder.tvItemName.setText(item.getUser_name());
        itemViewHolder.tvDetails.setText(item.getQuantity() + " " + item.getP_name());
        // itemViewHolder.tvOrderedAT.setText(item.getOrder_at());
        itemViewHolder.row_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, PurchaseDetails.class);
                i.putExtra("model_class", item);
                i.putExtra("position", position);
                activity.startActivity(i);
            }
        });

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        try {
            Date date = formatter.parse(item.getOrder_at());
            assert date != null;
            long millis = date.getTime();
            Log.d("another time", String.valueOf(millis));
            TimeAgo timeAgo = new TimeAgo(getApplicationContext());
            itemViewHolder.tvOrderedAT.setText(String.valueOf(timeAgo.timeAgo(millis)));
        } catch (ParseException e) {

        }


        if (item.getViewedAt().length() < 5) {
            itemViewHolder.tvItemName.setTextColor(activity.getResources().getColor(R.color.black));
            itemViewHolder.tvOrderedAT.setTextColor(activity.getResources().getColor(R.color.black));
            itemViewHolder.tvDetails.setTextColor(activity.getResources().getColor(R.color.black));
        } else {
            itemViewHolder.tvItemName.setTextColor(activity.getResources().getColor(R.color.darker_gray));
            itemViewHolder.tvOrderedAT.setTextColor(activity.getResources().getColor(R.color.darker_gray));
            itemViewHolder.tvDetails.setTextColor(activity.getResources().getColor(R.color.darker_gray));
        }
        if (item.getCompletionStatus().equals("1")) {
            itemViewHolder.icon.setImageDrawable(activity.getResources().getDrawable(R.drawable.accent_icon));
        } else {
            itemViewHolder.icon.setImageDrawable(activity.getResources().getDrawable(R.drawable.green_circle));
        }
    }

    public void deleteItem(int index, Integer id) {
        //remove from list
        itemList.remove(index);
        notifyItemRemoved(index);
        notifyItemRangeChanged(index, itemList.size());
    }


    @Override
    public PurchasesAdapter.ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_row, viewGroup, false);
        return new PurchasesAdapter.ItemViewHolder(itemView, i);
    }

    public interface OnStartDragListener {

        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder, View.OnClickListener {

        protected TextView tvItemName;
        protected TextView tvDetails;
        protected TextView tvOrderedAT;
        protected RelativeLayout row_layout;
        protected ImageView icon;
        int position;

        public ItemViewHolder(final View v, int i) {
            super(v);
            tvItemName = (TextView) v.findViewById(R.id.title);
            tvDetails = (TextView) v.findViewById(R.id.details);
            tvOrderedAT = (TextView) v.findViewById(R.id.time);
            icon = (ImageView) v.findViewById(R.id.icon);
            row_layout = (RelativeLayout) v.findViewById(R.id.row_layout);
            position = i;
        }


        @Override
        public void onClick(View view) {
        }

        @Override
        public void onItemSelected(Context context) {
         /*   container.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
            tvItemName.setTextColor(ContextCompat.getColor(context, R.color.white));
            itemImage.setColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.SRC_IN);*/
        }

        @Override
        public void onItemClear(Context context) {
           /* container.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            itemImage.setColorFilter(ContextCompat.getColor(context, R.color.textlight), PorterDuff.Mode.SRC_IN);
            tvItemName.setTextColor(ContextCompat.getColor(context, R.color.textlight));*/
        }

    }

}
