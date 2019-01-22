package com.daarululuumlido.mydictionary.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.daarululuumlido.mydictionary.Model.DictionaryModel;
import com.daarululuumlido.mydictionary.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.daarululuumlido.mydictionary.Adapter.DictionaryAdapter.WORDS;

public class DictionaryDetailActivity extends AppCompatActivity {
    DictionaryModel words;

    @BindView(R.id.tv_detail_word)
    TextView textViewDetailWord;

    @BindView(R.id.tv_detail_translate)
    TextView textViewDetailTranslate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_detail);
        ButterKnife.bind(this);

        words = getIntent().getParcelableExtra(WORDS);

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(R.string.description);
            actionBar.setSubtitle(words.getWord());
        }

        textViewDetailWord.setText(words.getWord());
        textViewDetailTranslate.setText(words.getTranslate());
    }
}
