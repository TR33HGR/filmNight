package com.tr33hgr.filmnight.viewHandlers;

import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.innovattic.rangeseekbar.RangeSeekBar;
import com.tr33hgr.filmnight.R;
import com.tr33hgr.filmnight.filmhandlers.FilmEvent;

public class FormAdapter extends RecyclerView.Adapter<FormAdapter.EventFormViewHolder>{

    private FilmEvent filmEvent;//local reference to filmEvent

    //holder of each individual film card info
    public static class EventFormViewHolder extends RecyclerView.ViewHolder{

        ImageView posterView;
        TextView filmNameView;
        TextView filmYearView;
        TextView filmGenreView;
        TextView filmLanguageView;
        TextView filmPlotView;

        EditText hostName;
        EditText hostAddress;
        EditText eventTime;
        EditText eventDate;
        EditText numGuests;
        TextView ageRangeTitle;
        RangeSeekBar ageRange;
        EditText minAge;
        EditText maxAge;

        TextView foodQView;
        TextView drinksQView;
        RadioGroup foodRadioGroup;
        RadioGroup drinksRadioGroup;
        RadioButton noFood;
        RadioButton byoFood;
        RadioButton supFood;
        RadioButton noDrinks;
        RadioButton byoDrinks;
        RadioButton supDrinks;
        CheckBox vegetarian;
        CheckBox vegan;
        CheckBox alcohol;

        Button submit;


        public EventFormViewHolder(View formView){
            super(formView);

            this.posterView = formView.findViewById(R.id.eventForm_poster_img);
            this.filmNameView = formView.findViewById(R.id.eventForm_filmTitle_txt);
            this.filmYearView = formView.findViewById(R.id.eventForm_filmYear_txt);
            this.filmGenreView = formView.findViewById(R.id.eventForm_filmGenre_txt);
            this.filmLanguageView = formView.findViewById(R.id.eventForm_filmLanguage_txt);
            this.filmPlotView = formView.findViewById(R.id.eventForm_filmPlot_txt);

            this.hostName = formView.findViewById(R.id.eventForm_hostName_txt);
            this.hostAddress = formView.findViewById(R.id.eventForm_hostAddress_txt);
            this.eventTime = formView.findViewById(R.id.eventForm_eventTime_txt);
            this.eventDate = formView.findViewById(R.id.eventForm_eventDate_txt);
            this.numGuests = formView.findViewById(R.id.eventForm_numGuests_txt);
            this.ageRangeTitle = formView.findViewById(R.id.eventForm_ageRangeTitle_txt);
            this.ageRange = formView.findViewById(R.id.eventForm_ageRange_range);
            this.minAge = formView.findViewById(R.id.eventForm_ageRangeMinSel_txt);
            this.maxAge = formView.findViewById(R.id.eventForm_ageRangeMaxSel_txt);

            this.foodQView = formView.findViewById(R.id.eventForm_foodQ_txt);
            this.drinksQView = formView.findViewById(R.id.eventForm_drinksQ_txt);
            this.foodRadioGroup = formView.findViewById(R.id.eventForm_foodRadioGroup_radGroup);
            this.drinksRadioGroup = formView.findViewById(R.id.eventForm_drinksRadioGroup_radGroup);
            this.noFood = formView.findViewById(R.id.eventForm_noFood_radio);
            this.byoFood = formView.findViewById(R.id.eventForm_byoFood_radio);
            this.supFood = formView.findViewById(R.id.eventForm_supFood_radio);
            this.noDrinks = formView.findViewById(R.id.eventForm_noDrinks_radio);
            this.byoDrinks = formView.findViewById(R.id.eventForm_byoDrinks_radio);
            this.supDrinks = formView.findViewById(R.id.eventForm_supDrinks_radio);
            this.vegetarian = formView.findViewById(R.id.eventForm_vegetarian_check);
            this.vegan = formView.findViewById(R.id.eventForm_vegan_check);
            this.alcohol = formView.findViewById(R.id.eventForm_alcohol_check);

            this.submit = formView.findViewById(R.id.eventForm_submit_butt);
        }
    }

    public FormAdapter(FilmEvent filmEvent){
        this.filmEvent = filmEvent;
    }

    @Override
    public EventFormViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //add film_card layout to adapter
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_form, parent, false);

        return new EventFormViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EventFormViewHolder holder, final int listPosition){
        ImageView posterView = holder.posterView;
        TextView filmNameView = holder.filmNameView;
        TextView filmYearView = holder.filmYearView;
        TextView filmGenreView = holder.filmGenreView;
        TextView filmLanguageView = holder.filmLanguageView;
        TextView filmPlotView = holder.filmPlotView;

        EditText hostName = holder.hostName;
        EditText hostAddress = holder.hostAddress;
        EditText eventTime = holder.eventTime;
        EditText eventDate = holder.eventDate;
        EditText numGuests = holder.numGuests;
        TextView ageRangeTitle = holder.ageRangeTitle;
        final RangeSeekBar ageRange = holder.ageRange;
        final EditText minAge = holder.minAge;
        final EditText maxAge = holder.maxAge;

        TextView foodQView = holder.foodQView;
        TextView drinksQView = holder.drinksQView;
        RadioGroup foodRadioGroup = holder.foodRadioGroup;
        RadioGroup drinksRadioGroup = holder.drinksRadioGroup;
        final RadioButton noFood = holder.noFood;
        RadioButton byoFood = holder.byoFood;
        RadioButton supFood = holder.supFood;
        final RadioButton noDrinks = holder.noDrinks;
        RadioButton byoDrinks = holder.byoDrinks;
        RadioButton supDrinks = holder.supDrinks;
        final CheckBox vegetarian = holder.vegetarian;
        final CheckBox vegan = holder.vegan;
        final CheckBox alcohol = holder.alcohol;

        Button submit = holder.submit;

        //set default poster image is error receiving from url
        RequestOptions options = new RequestOptions().error((R.mipmap.ic_launcher));
        //get poster from url
        Glide.with(posterView.getContext()).load(filmEvent.getPosterURL()).apply(options).into(posterView);

        filmNameView.setText(filmEvent.getTitle());
        filmYearView.setText(filmEvent.getYear());
        filmGenreView.setText(filmEvent.getGenre());
        filmLanguageView.setText(filmEvent.getLanguage());
        filmPlotView.setText(filmEvent.getPlot());

//TODO:https://android-arsenal.com/details/1/7063 find out how this works
//        ageRange.setOnTouchListener(new View.OnTouchListener(){
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                minAge.setText(ageRange.getMinThumbValue());
//                maxAge.setText(ageRange.getMaxThumbValue());
//                return false;
//            }
//        });

        vegetarian.setVisibility(View.GONE);
        vegan.setVisibility(View.GONE);

        foodRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(noFood.isChecked() != true){
                    vegetarian.setVisibility(View.VISIBLE);
                    vegan.setVisibility(View.VISIBLE);
                }else{
                    vegetarian.setVisibility(View.GONE);
                    vegan.setVisibility(View.GONE);
                }
            }
        });

        alcohol.setVisibility(View.GONE);

        drinksRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(noDrinks.isChecked() != true){
                    alcohol.setVisibility(View.VISIBLE);
                }else{
                    alcohol.setVisibility(View.GONE);
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //TODO:send all info to a database https://developers.google.com/android/guides/overview
            }
        });

    }

    @Override
    public int getItemCount(){
        return 1;
    }
}
