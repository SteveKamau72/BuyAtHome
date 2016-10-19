package com.softtech.stevekamau.buyathome.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.app.AppController;
import com.softtech.stevekamau.buyathome.model.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steve Kamau on 16-Jul-15.
 */
public class SearchListAdapter extends BaseAdapter implements Filterable {
    List<Model> mStringFilterList;
    ValueFilter valueFilter;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private Activity activity;
    private LayoutInflater inflater;
    private List<Model> modelItems;
    private int mDefaultImageId;

    public SearchListAdapter(Activity activity, List<Model> modelItems) {
        this.activity = activity;
        this.modelItems = modelItems;
        mStringFilterList = modelItems;
    }


    public void setDefaultImageResId(int defaultImage) {
        mDefaultImageId = defaultImage;
    }

    @Override
    public int getCount() {
        return modelItems.size();
    }

    @Override
    public Object getItem(int location) {
        return modelItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.search_list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);

        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView desc = (TextView) convertView.findViewById(R.id.desc);
        TextView amount = (TextView) convertView.findViewById(R.id.amount);
        Model m = modelItems.get(position);
        title.setText(m.getName());
        desc.setText(m.getDetails());
        amount.setText(m.getAmount());
        thumbNail.setImageUrl(m.getImage_url(), imageLoader);
       /* // thumbnail image
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
        thumbNail.setTag(position);
        // title
        title.setText(m.getTitle());

        // amount
        rating.setText(String.valueOf(m.getRating()));

        // description
        String genreStr = "";
        for (String str : m.getGenre()) {
            genreStr += str + ", ";
        }
        genreStr = genreStr.length() > 0 ? genreStr.substring(0,
                genreStr.length() - 2) : genreStr;
        genre.setText(genreStr);*/

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<Model> filterList = new ArrayList<Model>();
                for (int i = 0; i < mStringFilterList.size(); i++)
                    /*if ((mStringFilterList.get(i).getTitle().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {


                        Model m = new Model(mStringFilterList.get(i)
                                .getTitle(), mStringFilterList.get(i)
                                .getThumbnailUrl(), mStringFilterList.get(i)
                                .getRating(), mStringFilterList.get(i)
                                .getGenre());

                        filterList.add(m);
                    }*/
                    results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            modelItems = (List<Model>) results.values;
            notifyDataSetChanged();
        }

    }

}

