package findit.sedi.viktor.com.findit.ui.tournament;

import androidx.lifecycle.Lifecycle;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;

import com.squareup.otto.Subscribe;

import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.common.dialogs.DialogManager;
import findit.sedi.viktor.com.findit.presenter.otto.FinditBus;
import findit.sedi.viktor.com.findit.presenter.otto.events.UpdateTournamentsEvent;
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

        DialogManager.getInstance().setContext(this);
        ManagersFactory.getInstance().setContext(this);

        setContentView(R.layout.recycler_layout);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new MyAdapter(this, ManagersFactory.getInstance().getTournamentManager().getTournaments());
        recyclerView.setAdapter(mAdapter);

        FinditBus.getInstance().register(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FinditBus.getInstance().unregister(this);
    }


    @Subscribe
    public void updatePlayerLocation(UpdateTournamentsEvent updateTournamentsEvent) {

        if (this.getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
            mAdapter.notifyDataSetChanged();
        }

    }
}
