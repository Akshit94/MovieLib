package com.example.jainsaab.movielib.trailers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jainsaab.movielib.R;
import com.example.jainsaab.movielib.utility.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class TrailerAdapter extends ArrayAdapter<Trailer>{

    private Context mContext;
    private ArrayList<Trailer> mTrailerArrayList;

    TrailerAdapter(Context context, ArrayList<Trailer> trailerArrayList) {
        super(context, 0, trailerArrayList);
        mContext = context;
        mTrailerArrayList = trailerArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        final Trailer trailerItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trailer_layout_item, parent, false);
        }
        // Lookup view for data population
        TextView trailerName = (TextView) convertView.findViewById(R.id.trailer_name);
        ImageView trailerImage = (ImageView) convertView.findViewById(R.id.trailer_image_view);
        CardView trailerCard = (CardView) convertView.findViewById(R.id.trailer_item);

        // Populate the data into the template view using the data object
        if (trailerItem != null) {
            trailerName.setText(trailerItem.getTrailerName());
            String TRAILER_IMAGE_URL = Constants.TRAILER_IMAGE_URL + trailerItem.getTrailerKey() + Constants.TRAILER_IMAGE_QUALITY;
            Picasso.with(mContext).load(TRAILER_IMAGE_URL.trim()).into(trailerImage);
            trailerCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent trailerIntent = new Intent();
                    trailerIntent.setAction(Intent.ACTION_VIEW);
                    trailerIntent.setData(Uri.parse(Constants.TRAILER_VIDEO_URL + trailerItem.getTrailerKey()));
                    mContext.startActivity(trailerIntent);
                }
            });
        }
        // Return the completed view to render on screen
        return convertView;
    }

    @Nullable
    @Override
    public Trailer getItem(int position) {
        return mTrailerArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return mTrailerArrayList.size();
    }

    void setTrailer(List<Trailer> trailerList){
        if(trailerList != null){
            mTrailerArrayList.clear();
            mTrailerArrayList.addAll(trailerList);
            notifyDataSetChanged();
        }
    }

}
