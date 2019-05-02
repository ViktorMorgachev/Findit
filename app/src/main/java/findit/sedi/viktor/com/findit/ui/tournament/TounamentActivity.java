package findit.sedi.viktor.com.findit.ui.tournament;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;

import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.ui.tournament.data_provider.MyAdapter;

public class TounamentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ManagersFactory.getInstance().setContext(this);

        setContentView(R.layout.tournament_layout);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

     //   mAdapter = new MyAdapter(myDataset);
        recyclerView.setAdapter(mAdapter);


    }


}
