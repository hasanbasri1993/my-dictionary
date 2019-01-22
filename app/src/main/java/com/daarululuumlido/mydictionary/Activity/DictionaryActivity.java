package com.daarululuumlido.mydictionary.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.daarululuumlido.mydictionary.Adapter.DictionaryAdapter;
import com.daarululuumlido.mydictionary.Database.DictionaryHelper;
import com.daarululuumlido.mydictionary.Model.DictionaryModel;
import com.daarululuumlido.mydictionary.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DictionaryActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.fab_switch_language)
    FloatingActionButton buttonSwicthLanguage;
    @BindView(R.id.progressbar)
    ProgressBar progressBarIndoInggris;
    @BindView(R.id.rv_vocab)
    RecyclerView listView;

    DictionaryAdapter dictionaryAdapter;
    DictionaryHelper dictionaryHelper;
    List<DictionaryModel> listDictionary = new ArrayList<>();
    boolean isIndo = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(R.string.dictionary);
            String subTitle = getString(R.string.ind_ing);
            actionBar.setSubtitle(subTitle);
        }

        ButterKnife.bind(this);
        dictionaryHelper = new DictionaryHelper(this);
        buttonSwicthLanguage.setOnClickListener(this);
        showRecyclerList();
    }

    private void showRecyclerList() {
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setHasFixedSize(true);
        dictionaryAdapter = new DictionaryAdapter(this);

        dictionaryHelper.open();
        listDictionary = dictionaryHelper.getAllData(isIndo);
        dictionaryHelper.close();

        dictionaryAdapter.setDictionaryList(listDictionary);
        progressBarIndoInggris.setVisibility(View.GONE);

        listView.setAdapter(dictionaryAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the search menu action bar.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);

        // Get the search menu.
        MenuItem searchMenu = menu.findItem(R.id.app_bar_menu_search);

        // Get SearchView object.
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenu);

        // Below event is triggered when submit search query.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                dictionaryHelper.open();
                listDictionary = dictionaryHelper.getDataByWord(isIndo, newText);
                dictionaryHelper.close();
                dictionaryAdapter.setDictionaryList(listDictionary);
                progressBarIndoInggris.setVisibility(View.GONE);
                Log.d("HASIL", isIndo + newText);
                return true;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_switch_language) {
            if (isIndo) {
                isIndo = false;
            } else {
                isIndo = true;
            }
            setUpActionBar(isIndo);
        }
    }

    void setUpActionBar(boolean isIndo) {
        dictionaryHelper.open();
        if (isIndo) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setSubtitle(R.string.ind_ing);
                Toast.makeText(this, R.string.ind_ing, Toast.LENGTH_SHORT).show();

            }
            listDictionary = dictionaryHelper.getAllData(isIndo);
            dictionaryAdapter.setDictionaryList(listDictionary);
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setSubtitle(R.string.ing_ind);
                Toast.makeText(this, R.string.ing_ind, Toast.LENGTH_SHORT).show();

            }
            listDictionary = dictionaryHelper.getAllData(isIndo);
            dictionaryAdapter.setDictionaryList(listDictionary);
        }
        dictionaryHelper.close();
    }


}
