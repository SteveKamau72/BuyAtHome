package com.softtech.stevekamau.buyathome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.softtech.stevekamau.buyathome.model.CartModel;
import com.softtech.stevekamau.buyathome.R;

import java.util.ArrayList;

/**
 * Created by Steve Kamau on 08-Jul-15.
 */
public class VerificationListAdapter extends BaseAdapter {

    Context context;
    ArrayList<CartModel> contactList;

    public VerificationListAdapter(Context context, ArrayList<CartModel> list) {

        this.context = context;
        contactList = list;
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
    public View getView(int position, View convertView, ViewGroup arg2) {
        CartModel cartModel = contactList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.order_list_row, null);

        }
        TextView tvSlNo = (TextView) convertView.findViewById(R.id.textView7);
        tvSlNo.setText(cartModel.getTitle());
        TextView tvName = (TextView) convertView.findViewById(R.id.textView8);
        tvName.setText("Kshs."+ cartModel.getAmount());

        return convertView;
    }

}

