package com.softtech.stevekamau.buyathome.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.model.PurchasesModel;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by steve on 12/18/16.
 */

public class PurchasedItemsAdapter extends RecyclerView.Adapter<PurchasedItemsAdapter.ViewHolder> {

    private final List<PurchasesModel> mPurchasesModelList;

    public PurchasedItemsAdapter(List<PurchasesModel> purchasesModelList) {
        mPurchasesModelList = purchasesModelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchased_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PurchasesModel PurchasesModel = mPurchasesModelList.get(position);
        holder.title.setText(PurchasesModel.getP_name());
        double amount = Double.parseDouble(PurchasesModel.getSub_total());
        DecimalFormat formatter = new DecimalFormat("#,###.00");

        holder.amount.setText("KSHS." + formatter.format(amount));
        holder.quantity.setText(PurchasesModel.getQuantity());
        holder.id.setText("Purchase Id: " + PurchasesModel.getPurchaseId());
    }

    @Override
    public int getItemCount() {
        return mPurchasesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final TextView amount;
        public final TextView id;
        public final TextView quantity;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tvname);
            quantity = (TextView) view.findViewById(R.id.tvquantity);
            id = (TextView) view.findViewById(R.id.tvid);
            amount = (TextView) view.findViewById(R.id.tvamount);
        }
    }
} 