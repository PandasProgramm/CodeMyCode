package de.miguel.frozzenlist.codemycode;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 @param: mcontext= save the reference of Context Object from mainActivity
 mQuoteList= use memberinstance for produce a zitat-object
 mLayoutInflater= use instanz for produceing the View hierachy
 mResources= reference auf R.instance
 mPackageName= save the packagename in our App. We need a link for Ressources-ID
 mQuoteAuthorDrawables= Instance <Map> for save Layoutobjects
 @link:  R.resources
 */



public class QuoteArrayAdapter extends ArrayAdapter<Quote> {

    private Context mContext;
    private List<Quote> mQuoteList;
    private LayoutInflater mLayoutInflater;

    private Resources mResources;
    private String mPackageName;
    private Map<String, Drawable> mQuoteAuthorDrawables = new HashMap<>();

    public QuoteArrayAdapter(Context context, List<Quote> quoteList) {
        super(context, R.layout.list_row, quoteList);

        mContext = context;
        mQuoteList = quoteList;
        mLayoutInflater = LayoutInflater.from(context);

        mResources = context.getResources();
        mPackageName = context.getPackageName();
        createQuoteAuthorDrawables();
    }

    private void createQuoteAuthorDrawables() {
        int imageId;
        String[] quoteAuthors = mResources.getStringArray(R.array.quote_authors);

        for (String author : quoteAuthors)
        {
            imageId = mResources.getIdentifier(author, "drawable", mPackageName);
            if (imageId > 0) {
                mQuoteAuthorDrawables.put(author, ContextCompat.getDrawable(mContext, imageId));
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Erzeugen der View-Hierarchie auf Grundlage des Zeilenlayouts
        View rowView;
        if (convertView == null) {
            rowView = mLayoutInflater.inflate(R.layout.list_row, parent,false);
        }
        else {
            rowView = convertView;
        }

        // request listposition from dataobject
        Quote currentQuote = mQuoteList.get(position);

        // find the single view Object from  layout row
        TextView tvQuoteText   = (TextView) rowView.findViewById(R.id.quote_text);
        TextView tvQuoteAuthor = (TextView) rowView.findViewById(R.id.quote_author);
        ImageView ivAuthorImage  = (ImageView) rowView.findViewById(R.id.author_image);

        // Fill View-Objekte with passing context from Dataobject
        tvQuoteText.setText("\"" + currentQuote.getQuoteText() + "\"");
        tvQuoteAuthor.setText(currentQuote.getQuoteAuthor());

       // Drawable quoteAuthorDrawable = mQuoteAuthorDrawables.get(currentQuote.getImageID());
        ivAuthorImage.setImageResource(currentQuote.getImageID());

        // return View from Adapter
        return rowView;
    }
}