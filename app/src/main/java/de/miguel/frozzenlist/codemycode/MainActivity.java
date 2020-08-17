package de.miguel.frozzenlist.codemycode;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;



import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String LOG = MainActivity.class.getSimpleName();
    private List<Quote>mQuoteList= new ArrayList<>();
    private QuoteArrayAdapter mquoteArrayAdapter;
    private ListView mquoteListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAppBar();
        intitSwipeRefreshLayout();

        createQuoteList();
        bindAdapterToListView();

        registerListViewListener();
        registerLongListViewListener();

    }

    //menu action
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_activity__main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case R.id.action_get_data:
                //instruction for progress indicator view
                mSwipeRefreshLayout.setRefreshing(true);
                refreshView();
                return true;
            case R.id.action_settings:
                 Toast toast= Toast.makeText(MainActivity.this,"Der Einstellungen-" +
                                                 "Menüeintrag wurde angeklickt.",Toast.LENGTH_LONG);
                 toast.show();
                 return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void createQuoteList(){
    String[] sampleQuotes = getResources().getStringArray(R.array.sampe_quotes);

    Quote sampleQuote = new Quote(sampleQuotes[0],"Johann Wolfgang von Goethe",R.drawable.goethe);
    mQuoteList.add(sampleQuote);
    mQuoteList.add(new Quote(sampleQuotes[1],"Friedrich Schiller",R.drawable.schiller));
    mQuoteList.add(new Quote(sampleQuotes[2],"Johann Wolfgang v. Goethe",R.drawable.goethe));
    mQuoteList.add(new Quote(sampleQuotes[3],"Friedrich Schiller",R.drawable.schiller));
    mQuoteList.add(new Quote(sampleQuotes[4],"Johann Wolfgang v. Goethe",R.drawable.goethe));
    mQuoteList.add(new Quote(sampleQuotes[5],"Friedrich Schiller",R.drawable.schiller));
    mQuoteList.add(new Quote(sampleQuotes[6],"Johann Wolfgang v. Goethe",R.drawable.goethe));
    mQuoteList.add(new Quote(sampleQuotes[7],"Friedrich Schiller",R.drawable.schiller));
    mQuoteList.add(new Quote(sampleQuotes[8],"Johann Wolfgang v. Goethe",R.drawable.goethe));
    mQuoteList.add(new Quote(sampleQuotes[9],"unbekannt",R.drawable.unknown));
    }
    private void bindAdapterToListView(){
       mquoteArrayAdapter= new QuoteArrayAdapter(this,mQuoteList);
       mquoteListView= (ListView)findViewById(R.id.listview_activity_main);
       mquoteListView.setAdapter(mquoteArrayAdapter);
    }


    private void registerListViewListener(){

        AdapterView.OnItemClickListener onItemClickListener= new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int duration = Toast.LENGTH_LONG;
                String quoteAuthor= mQuoteList.get(position).getQuoteAuthor();
                String message = "Ein Zitat von "+ quoteAuthor+" wurde angeklickt";

                Toast toast= Toast.makeText(getApplicationContext(),message,duration);
                toast.show();
            }
        };
        mquoteListView.setOnItemClickListener(onItemClickListener);
    }
    private void registerLongListViewListener(){
        mquoteListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                /*instance AlertDialog with Construktor
                  Context = MainActivity
               */
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(mQuoteList.get(position).getQuoteAuthor())
                        .setMessage(mQuoteList.get(position).getQuoteAuthor())
                        .setMessage(mQuoteList.get(position).getQuoteText())

                        //Button for output dialog
                        .setPositiveButton("Schliessen", null);
                AlertDialog dialog = builder.create();
                dialog.show();
                //treaded LongItemListerner =>
                return true;
            }
        });
    }
    private void refreshView(){

        RequestQuotesTask requestQuotesTask= new RequestQuotesTask();
        requestQuotesTask.execute(10,0);
    }
    private void initAppBar(){
        Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
    }
    private void intitSwipeRefreshLayout(){
        mSwipeRefreshLayout= (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout_activity_main);

        SwipeRefreshLayout.OnRefreshListener onRefreshListener;
        onRefreshListener= new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                refreshView();
            }
        };
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
    }
    private class RequestQuotesTask extends AsyncTask<Integer, String, List<Quote>> {

        private final String INNER_TAG = RequestQuotesTask.class.getSimpleName();

        @Override
        protected List<Quote> doInBackground(Integer... intParams) {

            int quotesCount = intParams[0];
            int parsingMethod = intParams[1];
            List<Quote> newQuoteList = new ArrayList<>();

            publishProgress("Bitte warten! " + quotesCount + " Zitate werden geladen.");

            for (int i = 0; i < quotesCount; i++) {
                // Mit Thread.sleep(350) simulieren wir eine Wartezeit von 350 ms
                // Dann fügen wir ein Quote-Objekt in die neue Zitat-Liste ein
                try {
                    Thread.sleep(350);
                    newQuoteList.add(new Quote(getString(R.string.sample_quote),
                            getString(R.string.sample_author), R.id.author_image));
                } catch (Exception e) {
                    Log.e(INNER_TAG, "Thread-Error in inner class.", e);
                }
            }

            publishProgress(newQuoteList.size() + " Zitate wuden erfolgreich geladen.");

            return newQuoteList;
        }

        @Override
        protected void onProgressUpdate(String... stringParams) {
            // Auf dem Bildschirm geben wir eine Statusmeldung aus, immer wenn
            // publishProgress() von doInBackground() aufgerufen wird
            String message = stringParams[0];
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(List<Quote> receivedQuoteList) {
            // Die Datenquelle wird mit den in der doInBackground() Methode erstellten Daten gefüllt
            // und der Adapter wird informiert, dass sich der Inhalt seiner Datenquelle geändert hat
            mQuoteList.clear();
            mQuoteList.addAll(receivedQuoteList);
            mquoteArrayAdapter.notifyDataSetChanged();

            // Das SwipeRefreshLayout wird angewiesen die Fortschrittsanzeige wieder auszublenden
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

}
