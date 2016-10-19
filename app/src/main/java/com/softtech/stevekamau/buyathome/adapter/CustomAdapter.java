package com.softtech.stevekamau.buyathome.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.softtech.stevekamau.buyathome.activites.LatestLaptops;
import com.softtech.stevekamau.buyathome.activites.LatestPhones;
import com.softtech.stevekamau.buyathome.activites.MainActivity;
import com.softtech.stevekamau.buyathome.activites.MostRated;
import com.softtech.stevekamau.buyathome.R;

/**
 * Created by Steve Kamau on 10-Jul-15.
 */
public class CustomAdapter extends BaseAdapter{
    private static LayoutInflater inflater = null;
    String [] result;
    Context context;
    int [] imageId;
    public CustomAdapter(MainActivity mainActivity, String[] prgmNameList, int[] prgmImages) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        context=mainActivity;
        imageId=prgmImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.hot_list, null);
        holder.tv=(TextView) rowView.findViewById(R.id.textView1);
        holder.img=(ImageView) rowView.findViewById(R.id.imageView1);
        holder.tv.setText(result[position]);
        holder.img.setImageResource(imageId[position]);
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent(context, MostRated.class);
                        break;
                    case 1:
                        intent = new Intent(context, LatestLaptops.class);
                        break;
                    default:
                        intent = new Intent(context, LatestPhones.class);
                        break;
                }
                context.startActivity(intent);
               }
        });
        return rowView;
    }

    public class Holder {
        TextView tv;
        ImageView img;
    }

}
