package com.daarululuumlido.mydictionary.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daarululuumlido.mydictionary.Activity.DictionaryDetailActivity;
import com.daarululuumlido.mydictionary.Model.DictionaryModel;
import com.daarululuumlido.mydictionary.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.ViewHolder> {

    public static final String WORDS = "words";
    List<DictionaryModel> wordsList = new ArrayList<>();
    Context context;

    public DictionaryAdapter(Context context) {
        this.context = context;
    }

    public void setDictionaryList(List<DictionaryModel> wordsList){
        this.wordsList = wordsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_vocab, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(wordsList.get(position));
    }

    @Override
    public int getItemCount() {
        return wordsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_word) TextView tv_word;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(final DictionaryModel words){
            tv_word.setText(words.getWord());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(itemView.getContext(), DictionaryDetailActivity.class);
                    i.putExtra(WORDS, words);
                    itemView.getContext().startActivity(i);
                }
            });
        }
    }
}