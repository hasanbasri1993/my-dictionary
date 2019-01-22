package com.daarululuumlido.mydictionary.Activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daarululuumlido.mydictionary.Database.DictionaryHelper;
import com.daarululuumlido.mydictionary.Model.DictionaryModel;
import com.daarululuumlido.mydictionary.Prefs.AppPreference;
import com.daarululuumlido.mydictionary.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreloadActivity extends AppCompatActivity {
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.tv_loading)
    TextView textViewLoading;

    String textViewLoadingContent = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preload);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        ButterKnife.bind(this);
        new LoadData().execute();
    }

    /*
    Asynctask untuk menjalankan preload data
     */
    private class LoadData extends AsyncTask<Void, Integer, Void> {
        final String TAG = LoadData.class.getSimpleName();
        DictionaryHelper dictionaryHelper;
        AppPreference appPreference;
        double progress;
        double maxprogress = 100;

        /*
        Persiapan sebelum proses dimulai
        Berjalan di Main Thread
         */
        @Override
        protected void onPreExecute() {

            dictionaryHelper = new DictionaryHelper(PreloadActivity.this);
            appPreference = new AppPreference(PreloadActivity.this);
        }

        /*
        Proses background terjadi di method doInBackground
         */
        @Override
        protected Void doInBackground(Void... params) {

            /*
            Panggil preference first run
             */
            Boolean firstRun = appPreference.getFirstRun();
            Log.d("firstRun", firstRun.toString());

            /*
            Jika first run true maka melakukan proses pre load,
            jika first run false maka akan langsung menuju home
             */
            if (firstRun) {
                /*
                Load raw data dari file txt ke dalam array model mahasiswa
                 */
                ArrayList<DictionaryModel> indoInggrisModels = preLoadRaw(R.raw.indonesia_english);
                ArrayList<DictionaryModel> inggrisIndoModels = preLoadRaw(R.raw.english_indonesia);

                dictionaryHelper.open();
                progress = 30;
                publishProgress((int) progress);
                Double progressMaxInsertIndo = 80.0;
                Double progressDiffIndo = (progressMaxInsertIndo - progress) / indoInggrisModels.size();

                /*
                Gunakan ini untuk query insert yang transactional
                 */
                dictionaryHelper.beginTransaction();
                textViewLoadingContent = getString(R.string.prepare_data_ind_ing);
                try {
                    for (DictionaryModel model : indoInggrisModels) {

                        dictionaryHelper.insertTransaction(model, true);
                        progress += progressDiffIndo;
                        publishProgress((int) progress);
                    }

                } catch (Exception e) {
                    // Jika gagal maka do nothing
                    Log.e(TAG, "doInBackground: Exception");
                }

                textViewLoadingContent = getString(R.string.prepare_data_ing_indo);
                try {
                    for (DictionaryModel model : inggrisIndoModels) {
                        dictionaryHelper.insertTransaction(model, false);
                        progress += progressDiffIndo;
                        publishProgress((int) progress);
                    }

                } catch (Exception e) {
                    // Jika gagal maka do nothing
                    Log.e(TAG, "doInBackground: Exception");
                }

                // Jika semua proses telah di set success maka akan di commit ke database
                dictionaryHelper.setTransactionSuccess();
                dictionaryHelper.endTransaction();

                // Close helper ketika proses query sudah selesai
                dictionaryHelper.close();

                /*
                Set preference first run ke false
                Agar proses preload tidak dijalankan untuk kedua kalinya
                */
                appPreference.setFirstRun(false);

                publishProgress((int) maxprogress);

            } else {
                try {
                    synchronized (this) {
                        this.wait(300);

                        publishProgress(50);

                        this.wait(300);
                        publishProgress((int) maxprogress);
                    }
                } catch (Exception e) {
                }
            }
            return null;
        }

        //Update prosesnya
        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
            textViewLoading.setText(textViewLoadingContent);
        }

        /*
        Setelah proses selesai
        Berjalan di Main Thread
        */
        @Override
        protected void onPostExecute(Void result) {
            Intent i = new Intent(PreloadActivity.this, DictionaryActivity.class);
            startActivity(i);
            finish();
        }
    }

    /**
     * Parsing raw data text berupa data menjadi array mahasiswa
     *
     * @return array model dari semua mahasiswa
     */
    public ArrayList<DictionaryModel> preLoadRaw(int data) {
        ArrayList<DictionaryModel> indoInggrisModels = new ArrayList<>();
        String line;
        BufferedReader reader;
        InputStream raw_dict;

        try {
            Resources res = getResources();
            raw_dict = res.openRawResource(data);
            reader = new BufferedReader(new InputStreamReader(raw_dict));
            int count = 0;
            do {
                line = reader.readLine();
                String[] splitstr = line.split("\t");

                DictionaryModel mahasiswaModel;

                mahasiswaModel = new DictionaryModel(splitstr[0], splitstr[1]);
                indoInggrisModels.add(mahasiswaModel);
                count++;
            } while (line != null);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return indoInggrisModels;
    }

}


