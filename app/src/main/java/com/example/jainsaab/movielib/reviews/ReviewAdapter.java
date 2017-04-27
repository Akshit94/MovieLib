package com.example.jainsaab.movielib.reviews;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.jainsaab.movielib.R;

import java.util.ArrayList;
import java.util.List;

class ReviewAdapter extends ArrayAdapter<Review>{

    private Context mContext;
    private ArrayList<Review> mReviewArrayList;

    ReviewAdapter(Context context, ArrayList<Review> reviewArrayList) {
        super(context, 0, reviewArrayList);
        mContext = context;
        mReviewArrayList = reviewArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        final Review reviewItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_layout_item, parent, false);
        }
        // Lookup view for data population
        TextView authorName = (TextView) convertView.findViewById(R.id.review_author_name);
        TextView reviewContent = (TextView) convertView.findViewById(R.id.review_content);
        Button readMoreBtn = (Button) convertView.findViewById(R.id.read_more);

        // Populate the data into the template view using the data object
        if (reviewItem != null) {
            authorName.setText(reviewItem.getAuthorName());
            reviewContent.setText(reviewItem.getReviewContent());
            readMoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(reviewItem.getReviewUrl()));
                    mContext.startActivity(intent);
                }
            });
        }
        // Return the completed view to render on screen
        return convertView;
    }

    @Nullable
    @Override
    public Review getItem(int position) {
        return mReviewArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return mReviewArrayList.size();
    }

    void setReview(List<Review> reviewList){
        if(reviewList != null){
            mReviewArrayList.clear();
            mReviewArrayList.addAll(reviewList);
            notifyDataSetChanged();
        }
    }

}
