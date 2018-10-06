package com.apps.adam.inventory;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardHolder> {
    private ArrayList<Card> cards;
    private Context context;
    private LayoutInflater inflater;

    public CardAdapter(Context context, ArrayList<Card> cards) {
        this.inflater = LayoutInflater.from(context);
        this.cards = cards;
    }


    @Override
    public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card, parent, false);
        return new CardHolder(view);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    @Override
    public void onBindViewHolder(CardHolder holder, int position) {
        Card currentCard = cards.get(position);
        holder.cardTitle.setText(currentCard.getTitle());
        holder.cardPrice.setText(currentCard.getPrice().toString());
        holder.cardQuantity.setText(Integer.toString(currentCard.getQuantity()));

    }

    public void swapData(ArrayList<Card> data) {
            this.cards = data;
            notifyDataSetChanged();

    }

    //Inner CardHolder class
    public class CardHolder extends RecyclerView.ViewHolder {
        public TextView cardTitle;
        public TextView cardPrice;
        public TextView cardQuantity;

        //CardHolder constructor
        public CardHolder(View cardView) {
            super(cardView);

            cardTitle = (TextView) cardView.findViewById(R.id.productName);
            cardPrice = (TextView) cardView.findViewById(R.id.productPrice);
            cardQuantity = (TextView) cardView.findViewById(R.id.productQuantity);
        }


    }
}
