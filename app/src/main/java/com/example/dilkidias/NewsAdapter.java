package com.example.dilkidias.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by DILKI DIAS on 11-Jun-17.
 */

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(@NonNull Context context, List<News> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_item, parent, false);
        }

        News currentNews = getItem(position);

        TextView newsTitle = (TextView) listItemView.findViewById(R.id.news_title);
        assert currentNews != null;
        newsTitle.setText(currentNews.getNewsTitle());

        TextView newsTags = (TextView) listItemView.findViewById(R.id.news_description);
        newsTags.setText("Tags: " + currentNews.getTags());

        ImageView imageView = (ImageView) listItemView.findViewById(R.id.news_image);
        if (currentNews.getImageBitmap() != null) {
            imageView.setImageBitmap(currentNews.getImageBitmap());
        } else {
            imageView.setImageResource(R.drawable.noimagefound);
        }

        ImageView rating_1 = (ImageView) listItemView.findViewById(R.id.news_rating_1);
        ImageView rating_2 = (ImageView) listItemView.findViewById(R.id.news_rating_2);
        ImageView rating_3 = (ImageView) listItemView.findViewById(R.id.news_rating_3);
        ImageView rating_4 = (ImageView) listItemView.findViewById(R.id.news_rating_4);
        ImageView rating_5 = (ImageView) listItemView.findViewById(R.id.news_rating_5);

        int ratings = currentNews.getRatings();
        switch (ratings) {
            case 1:
                rating_1.setImageResource(R.drawable.ic_action_star_10);
                rating_2.setImageResource(R.drawable.ic_action_star_0);
                rating_3.setImageResource(R.drawable.ic_action_star_0);
                rating_4.setImageResource(R.drawable.ic_action_star_0);
                rating_5.setImageResource(R.drawable.ic_action_star_0);
                break;
            case 2:
                rating_1.setImageResource(R.drawable.ic_action_star_10);
                rating_2.setImageResource(R.drawable.ic_action_star_10);
                rating_3.setImageResource(R.drawable.ic_action_star_0);
                rating_4.setImageResource(R.drawable.ic_action_star_0);
                rating_5.setImageResource(R.drawable.ic_action_star_0);
                break;
            case 3:
                rating_1.setImageResource(R.drawable.ic_action_star_10);
                rating_2.setImageResource(R.drawable.ic_action_star_10);
                rating_3.setImageResource(R.drawable.ic_action_star_10);
                rating_4.setImageResource(R.drawable.ic_action_star_0);
                rating_5.setImageResource(R.drawable.ic_action_star_0);
                break;
            case 4:
                rating_1.setImageResource(R.drawable.ic_action_star_10);
                rating_2.setImageResource(R.drawable.ic_action_star_10);
                rating_3.setImageResource(R.drawable.ic_action_star_10);
                rating_4.setImageResource(R.drawable.ic_action_star_10);
                rating_5.setImageResource(R.drawable.ic_action_star_0);
                break;
            case 5:
                rating_1.setImageResource(R.drawable.ic_action_star_10);
                rating_2.setImageResource(R.drawable.ic_action_star_10);
                rating_3.setImageResource(R.drawable.ic_action_star_10);
                rating_4.setImageResource(R.drawable.ic_action_star_10);
                rating_5.setImageResource(R.drawable.ic_action_star_10);
                break;
            default:
                rating_1.setVisibility(View.GONE);
                rating_2.setVisibility(View.GONE);
                rating_3.setVisibility(View.GONE);
                rating_4.setVisibility(View.GONE);
                rating_5.setVisibility(View.GONE);
        }

        if (currentNews.getPublisher() != null) {
            TextView publisher_text = (TextView) listItemView.findViewById(R.id.news_publisher);
            publisher_text.setText("Published by: " + currentNews.getPublisher());
        }

        TextView publishedDate = (TextView) listItemView.findViewById(R.id.news_date);
        publishedDate.setText(currentNews.getPublishedDate());
        return listItemView;
    }
}
