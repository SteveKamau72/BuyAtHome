package com.softtech.stevekamau.buyathome.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.softtech.stevekamau.buyathome.model.CartModel;
import com.softtech.stevekamau.buyathome.databaseHandlers.WishDB;
import com.softtech.stevekamau.buyathome.R;

import java.util.ArrayList;

/**
 * Created by Steve Kamau on 02-Jul-15.
 */
public class CustomListAdapterFav extends BaseAdapter {
    ListView obj;
    Context context;
    ArrayList<CartModel> contactList;
    WishDB favDB;
    private Activity parentActivity;
    public CustomListAdapterFav(Context context, ArrayList<CartModel> list, Activity parentActivity) {

        this.context = context;
        contactList = list;
        this.parentActivity = parentActivity;
    }



    @Override
    public int getCount() {

        return contactList.size();
    }

    @Override
    public Object getItem(int position) {

        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        final CartModel cartModel = contactList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fav_list_row, null);

        }
        TextView tvSlNo = (TextView) convertView.findViewById(R.id.tv_slno);
        tvSlNo.setText(cartModel.getTitle());
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        tvName.setText(""+ cartModel.getAmount());
        TextView tvPhone = (TextView) convertView.findViewById(R.id.tv_phone);
        tvPhone.setText(cartModel.getDescription());

        ImageView img=(ImageView)convertView.findViewById(R.id.imageView4);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mytext = cartModel.getTitle();
                String mytext2 = cartModel.getDescription();
                String mytext3 = String.valueOf(cartModel.getAmount());
                detailsDialog(mytext, mytext2, mytext3);
            }
        });
        return convertView;
    }
    private void detailsDialog(String mytext, String mytext2, String mytext3) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(parentActivity);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Details");
        alertDialog.setMessage(mytext + "\n" + "\n" + mytext2 + "\n" + "\n" + "Price: Kshs." + mytext3);
        alertDialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();

    }

}
