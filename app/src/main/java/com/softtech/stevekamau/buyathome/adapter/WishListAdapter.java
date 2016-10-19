package com.softtech.stevekamau.buyathome.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.databaseHandlers.WishDB;
import com.softtech.stevekamau.buyathome.helper.ItemTouchHelperAdapter;
import com.softtech.stevekamau.buyathome.helper.ItemTouchHelperViewHolder;
import com.softtech.stevekamau.buyathome.interfaces.CartInterFace;
import com.softtech.stevekamau.buyathome.model.CartModel;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

/**
 * Created by steve on 8/16/16.
 */
public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ItemViewHolder> implements ItemTouchHelperAdapter {

    static List<CartModel> itemList;
    private final Context context;
    CartInterFace ci;
    private TextView tvNumber;

    public WishListAdapter(CartInterFace ci, Context context, List<CartModel> itemList) {
        this.context = context;
        this.itemList = itemList;
        this.ci = ci;

    }

    @Override
    public void onItemDismiss(final int position) {

        final CartModel item = new CartModel();
        item.setTitle(itemList.get(position).getTitle());

        notifyItemRemoved(position);
        itemList.remove(position);
        notifyItemRangeChanged(0, getItemCount());
        tvNumber.setText(String.valueOf(itemList.size()));

       /* final Snackbar snackbar = Snackbar

                .make(tvNumber, context.getResources().getString(R.string.item_deleted), Snackbar.LENGTH_LONG)
                .setActionTextColor(ContextCompat.getColor(context, R.color.white))
                .setAction(context.getResources().getString(R.string.item_undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemList.add(position, item);
                        notifyItemInserted(position);
                        tvNumber.setText(String.valueOf(itemList.size()));

                    }
                });


        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
        TextView tvSnack = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        TextView tvSnackAction = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_action);
        tvSnack.setTextColor(Color.WHITE);
        tvSnack.setTypeface(Typefaces.getGudeaMedium(context));
        tvSnackAction.setTypeface(Typefaces.getGudeaMedium(context));
        snackbar.show();


        Runnable runnableUndo = new Runnable() {

            @Override
            public void run() {
                tvNumber.setText(String.valueOf(itemList.size()));
                snackbar.dismiss();
            }
        };
        Handler handlerUndo = new Handler();
        handlerUndo.postDelayed(runnableUndo, 2500);*/
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

    public void addItem(int position, CartModel item) {

        itemList.add(position, item);
        notifyItemInserted(position);
        tvNumber.setText(String.valueOf(itemList.size()));

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder itemViewHolder, final int position) {

        final CartModel item = itemList.get(position);
        itemViewHolder.tvItemName.setText(item.getTitle());

        double amount = Double.parseDouble(String.valueOf(item.getAmount()));
        DecimalFormat formatter = new DecimalFormat("#,###.00");

        itemViewHolder.tvAmount.setText("kshs. " + formatter.format(amount));

        byte[] decodedString = Base64.decode(item.getImageFromPath(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        itemViewHolder.itemImage.setImageBitmap(decodedByte);
        itemViewHolder.imgButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem(position, item.getId());/*
                CartDB cartDB = new CartDB(context);
                cartDB.deleteSingleCartItem(item.getId());*/
                WishDB wishDB = new WishDB(context);
                wishDB.deleteSingleWishList(item.getId());
            }
        });
        itemViewHolder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });
    }

    public void deleteItem(int index, Integer id) {
        itemList.remove(index);
        notifyItemRemoved(index);
        notifyItemRangeChanged(index, itemList.size());
        (ci).onDeleted(id);
        /*Snackbar.make(findViewById(android.R.id.content), "empty", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();*/
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wishlist_item, viewGroup, false);
        return new ItemViewHolder(itemView);
    }

    private void showPopup(View v) {
        PopupMenu popup = new PopupMenu(context, v);
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

    public interface OnStartDragListener {

        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder, View.OnClickListener {

        protected TextView tvItemName;
        protected TextView tvAmount;
        protected ImageView itemImage;
        protected ImageView imgButon;
        protected ImageView options;
        protected EditText edQuantity;

        public ItemViewHolder(final View v) {
            super(v);
            tvItemName = (TextView) v.findViewById(R.id.txtTitle);
            tvAmount = (TextView) v.findViewById(R.id.amount);
            itemImage = (ImageView) v.findViewById(R.id.imageView);
            options = (ImageView) v.findViewById(R.id.options);
            imgButon = (ImageView) v.findViewById(R.id.cancel);
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
