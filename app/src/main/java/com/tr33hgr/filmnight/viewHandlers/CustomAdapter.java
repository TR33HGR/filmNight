package com.tr33hgr.filmnight.viewHandlers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tr33hgr.filmnight.R;
import com.tr33hgr.filmnight.filmhandlers.Film;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{

    private List<Film> filmList;

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView filmNameView;
        TextView filmYearView;
        ImageView posterView;

        public MyViewHolder(View itemView){
            super(itemView);

            this.filmNameView = itemView.findViewById(R.id.filmNameView);
            this.filmYearView = itemView.findViewById(R.id.filmYearView);
            this.posterView = itemView.findViewById(R.id.posterView);
        }
    }

    OnBottomReachedListener onBottomReachedListener;

    public CustomAdapter(List<Film> filmList){
        this.filmList = filmList;
    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){

        this.onBottomReachedListener = onBottomReachedListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_card, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition){
        TextView filmNameView = holder.filmNameView;
        TextView filmYearView = holder.filmYearView;
        ImageView posterView = holder.posterView;

        filmNameView.setText(filmList.get(listPosition).getTitle());
        filmYearView.setText(filmList.get(listPosition).getYear());

        RequestOptions options = new RequestOptions().error((R.mipmap.ic_launcher));
        Glide.with(posterView.getContext()).load(filmList.get(listPosition).getPosterURL()).apply(options).into(posterView);


        if (listPosition == filmList.size() - 1){

            onBottomReachedListener.onBottomReached(listPosition);

        }

    }

    @Override
    public int getItemCount(){
        return filmList.size();
    }
}
