package findit.sedi.viktor.com.findit.ui.rating;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;

import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.ui.rating.data_provider.MyAdapter;

// Нужно попытаться обновлять активность при её необходимости а лучше всего
// При старте подписаться на получение событий использую шину даннных
// и отписываться при выходе
public class RatingActivity extends AppCompatActivity {

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

        setContentView(R.layout.recycler_layout);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new MyAdapter(this, ManagersFactory.getInstance().getPlayersManager().getPlayers());
        recyclerView.setAdapter(mAdapter);


    }


}
