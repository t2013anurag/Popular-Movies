package com.example.anurag.popular_movies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by anurag on 3/4/16.
 */
public class MostPopularActivity extends ArrayAdapter<GridItem> {
    private static final String LOG_TAG = MostPopularActivity.class.getSimpleName();
    private Context context;
    private ArrayList<GridItem> gridData = new ArrayList<GridItem>();
    private int layoutResource;

    public MostPopularActivity(Context context, int layoutResource, ArrayList<GridItem> gridData)
    {
        super(context, layoutResource, gridData);
        this.layoutResource = layoutResource;
        this.context = context;
        this.gridData = gridData;
    }

    public void setGridData(ArrayList<GridItem> gridData) {
        this.gridData = gridData;
        notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = (TextView) convertView.findViewById(R.id.grid_item_title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.grid_item_image);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        GridItem item = gridData.get(position);
        //holder.titleTextView.setText(item.getTitle());
        Picasso.with(context)
                .load(item.getImage())
                .into(holder.imageView);

        return convertView;
    }

    static class ViewHolder {
        TextView titleTextView;
        ImageView imageView;
    }

}
