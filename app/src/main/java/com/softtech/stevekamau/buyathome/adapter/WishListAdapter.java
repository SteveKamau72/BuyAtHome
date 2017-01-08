package com.softtech.stevekamau.buyathome.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.databaseHandlers.WishDB;
import com.softtech.stevekamau.buyathome.helper.ItemTouchHelperAdapter;
import com.softtech.stevekamau.buyathome.helper.ItemTouchHelperViewHolder;
import com.softtech.stevekamau.buyathome.interfaces.CartInterFace;
import com.softtech.stevekamau.buyathome.interfaces.OnOptionsSelectedInterface;
import com.softtech.stevekamau.buyathome.model.CartModel;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

/**
 * Created by steve on 8/16/16.
 */
public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ItemViewHolder> implements ItemTouchHelperAdapter {

    static List<CartModel> itemList;
    Activity activity;
    CartInterFace ci;
    private TextView tvNumber;
    private OnOptionsSelectedInterface onOptionsSelected;
    private DisplayImageOptions options;

    public WishListAdapter(CartInterFace ci, OnOptionsSelectedInterface onOptionsSelected, Activity activity, List<CartModel> itemList) {
        this.activity = activity;
        this.itemList = itemList;
        this.ci = ci;
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

        itemViewHolder.tvAmount.setText(formatter.format(amount));

        itemViewHolder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view, position, itemViewHolder, item.getImageFromPath());
            }
        });
        itemViewHolder.imgButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete from db
                WishDB wishDB = new WishDB(activity);
                wishDB.deleteSingleWishList(item.getId());
                //update list
                deleteItem(position, item.getId());
                (onOptionsSelected).onDeleteItem();
            }
        });
        //download from the url
        com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                .displayImage(item.getImageFromPath(), itemViewHolder.itemImage, options, new SimpleImageLoadingListener() {
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
    }

    public void deleteItem(int index, Integer id) {
        //remove from list
        itemList.remove(index);
        notifyItemRemoved(index);
        notifyItemRangeChanged(index, itemList.size());
        (ci).onDeleted(id);
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wishlist_item, viewGroup, false);
        return new ItemViewHolder(itemView);
    }

    private void showPopup(View v, final int position, final ItemViewHolder itemViewHolder, final String imageFromPath) {
        final CartModel modelItem = itemList.get(position);
        PopupMenu popup = new PopupMenu(activity, v);
        // Inflate the menu from xml
        popup.getMenuInflater().inflate(R.menu.popup_wishliist, popup.getMenu());
        // Setup menu item selection
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.share_this:
                        (onOptionsSelected).onShareButtonclicked(modelItem.getTitle()
                                , String.valueOf(modelItem.getAmount()));
                        return true;
                    case R.id.add_to_cart:

                        (onOptionsSelected).onAddToCartButtonClicked(modelItem.getId(), modelItem.getTitle(),
                                String.valueOf(modelItem.getAmount()), modelItem.getDescription(),
                                imageFromPath, "1", String.valueOf(modelItem.getAmount()));
                        return true;
                    case R.id.delete_wishlist:
                        //delete from db
                        WishDB wishDB = new WishDB(activity);
                        wishDB.deleteSingleWishList(modelItem.getId());
                        //update list
                        deleteItem(position, modelItem.getId());
                        (onOptionsSelected).onDeleteItem();
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
