package org.artoolkit.ar.samples.ARSimpleNativeCars;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by flora on 2/27/17.
 */

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final int imageId;
    private final int imageId2;
    private List<String> items;

    public CustomListAdapter(Activity context, List<String> list, int imageId, int imageId2) {
        super(context, R.layout.list_item, list);
        this.context = context;
        this.imageId = imageId;
        this.imageId2 = imageId2;
        items = list;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_item, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        ImageView imageView2 = (ImageView) rowView.findViewById(R.id.img2);

        txtTitle.setTextColor(Color.WHITE);
        txtTitle.setText(items.get(position));

        imageView.setImageResource(imageId);
        imageView2.setImageResource(imageId2);
        return rowView;
    }


}