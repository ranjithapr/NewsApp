package com.udacity.ranjitha.newsapp.data;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.udacity.ranjitha.newsapp.R;

import java.util.List;

public class NewsInfoAdapter extends ArrayAdapter <NewsPojo> {

    public NewsInfoAdapter(Context context, List<NewsPojo> newsFeed) {
        super(context, 0, newsFeed);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Check if existing view is being reused, otherwise inflate the view
        View listViewItem = convertView;
        if(listViewItem == null){
            listViewItem  = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item,parent,false);
        }
        //get the object located in the list
        NewsPojo currentItem = getItem(position);

        TextView webTitle = (TextView) listViewItem.findViewById(R.id.title_view);
        webTitle.setText(currentItem.getTitle());

        TextView sectionName = (TextView) listViewItem.findViewById(R.id.section_name);
        sectionName.setText(currentItem.getSectionTitle());


        TextView authorName = (TextView)listViewItem.findViewById(R.id.author_name);
        authorName.setText(currentItem.getAuthor());

        TextView webPublicationDate = (TextView) listViewItem.findViewById(R.id.publication_date);
        webPublicationDate.setText(currentItem.getPublicationDate());

        return listViewItem;
    }
}
