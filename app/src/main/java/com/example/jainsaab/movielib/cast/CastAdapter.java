package com.example.jainsaab.movielib.cast;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jainsaab.movielib.R;
import com.example.jainsaab.movielib.utility.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

class CastAdapter extends ArrayAdapter<Cast> {
    private Context mContext;
    private ArrayList<Cast> castArrayList;

    CastAdapter(Context context, ArrayList<Cast> castArrayList1) {
        super(context, 0, castArrayList1);
        mContext = context;
        castArrayList = castArrayList1;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        final Cast castItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.person_layout_item, parent, false);
        }
        // Lookup view for data population
        TextView actorName = (TextView) convertView.findViewById(R.id.actor_name);
        TextView characterName = (TextView) convertView.findViewById(R.id.character_name);
        CircleImageView profileImage = (CircleImageView) convertView.findViewById(R.id.actor_profile_image);

        // Populate the data into the template view using the data object
        if (castItem != null) {
            actorName.setText(castItem.getActorName());
            characterName.setText(castItem.getCharacterName());
            String PROFILE_PATH_URL = Constants.IMAGE_BASE_URL + Constants.POSTER_SIZE_SMALL + castItem.getProfilePath();
            Picasso.with(mContext).load(PROFILE_PATH_URL.trim()).into(profileImage);
        }
        // Return the completed view to render on screen
        return convertView;
    }

    @Nullable
    @Override
    public Cast getItem(int position) {
        return castArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return castArrayList.size();
    }

    void setCast(List<Cast> castArrayList1){
        if(castArrayList1 != null){
            castArrayList.clear();
            castArrayList.addAll(castArrayList1);
            notifyDataSetChanged();
        }
    }
}
