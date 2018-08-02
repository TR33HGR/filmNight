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
import com.tr33hgr.filmnight.SearchFilmActivity;
import com.tr33hgr.filmnight.filmhandlers.Film;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.FilmCardViewHolder>{

    private List<Film> filmList;//local reference to filmList

    //listener to when at the end of currently received films
    OnBottomReachedListener onBottomReachedListener;

    //holder of each individual film card info
    public static class FilmCardViewHolder extends RecyclerView.ViewHolder{

        TextView filmNameView;
        TextView filmYearView;
        ImageView posterView;

        public FilmCardViewHolder(View cardView){
            super(cardView);

            this.filmNameView = cardView.findViewById(R.id.card_filmTitle_txt);
            this.filmYearView = cardView.findViewById(R.id.card_filmYear_txt);
            this.posterView = cardView.findViewById(R.id.card_poster_img);
        }
    }

    public CardAdapter(List<Film> filmList){
        this.filmList = filmList;
    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){

        this.onBottomReachedListener = onBottomReachedListener;
    }

    @Override
    public FilmCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //add film_card layout to adapter
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_card, parent, false);

        //link to onCardSelectListener (SearchFilmActivity)
        view.setOnClickListener(SearchFilmActivity.onCardSelectListener);

        return new FilmCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FilmCardViewHolder holder, final int listPosition){
        TextView filmNameView = holder.filmNameView;
        TextView filmYearView = holder.filmYearView;
        ImageView posterView = holder.posterView;

        filmNameView.setText(filmList.get(listPosition).getTitle());
        filmYearView.setText(filmList.get(listPosition).getYear());

        //set default poster image is error receiving from url
        RequestOptions options = new RequestOptions().error((R.mipmap.ic_launcher));
        //get poster from url
        Glide.with(posterView.getContext()).load(filmList.get(listPosition).getPosterURL()).apply(options).into(posterView);

        //set condition which says bottom has been reached
        if (listPosition == filmList.size() - 1){

            onBottomReachedListener.onBottomReached();

        }

    }

    @Override
    public int getItemCount(){
        return filmList.size();
    }
}
