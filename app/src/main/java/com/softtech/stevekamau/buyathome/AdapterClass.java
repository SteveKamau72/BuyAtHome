package com.softtech.stevekamau.buyathome;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Steve Kamau on 26-Feb-15.
 */
public class AdapterClass extends RecyclerView.Adapter<AdapterClass.MyViewHolder> {
    public static int selected_item = 0;
    List<Information> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public AdapterClass(Context context, List<Information> data) {
        this.context = context;

        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (position == selected_item) {
            holder.title.setTextColor(Color.parseColor("#00aaff"));
            //  holder.icon.setBackgroundResource(R.drawable.ic_circle);
        } else {
            holder.title.setTextColor(Color.parseColor("#6a6a6a"));
            holder.icon.setBackgroundResource(0);
        }
        Information current = data.get(position);
        holder.title.setText(current.title);
        holder.icon.setImageResource(current.iconId);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);
            itemView.setClickable(true);
            // itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           /* final Intent intent;
            switch (getPosition()) {
                case 0:
                    intent = new Intent(context, Phones.class);
                   *//* Fragment fragment = new PhonesFragment();
                    android.app.FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.main_activity, fragment).commit();*//*

                   *//* PhonesFragment fragment2 = new PhonesFragment();
                    android.app.FragmentManager fragmentManager =((Activity) context).getFragmentManager();
                    android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_activity, fragment2);
                    fragmentTransaction.commit();*//*

                   *//* PhonesFragment fr = new PhonesFragment();
                   // fr.setArguments(args);
                    FragmentManager fm = ((Activity) context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_place, fr);
                    fragmentTransaction.commit();*//*

                    break;
                case 1:
                    intent = new Intent(context, Laptops.class);
                    break;
                case 2:
                    intent = new Intent(context, PhoneAccessories.class);
                    break;
                default:
                    intent = new Intent(context, LaptopAccessories.class);
                    break;
            }
            context.startActivity(intent);
        }*/

        }
    }
}


