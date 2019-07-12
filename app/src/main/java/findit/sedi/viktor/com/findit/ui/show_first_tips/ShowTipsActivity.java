package findit.sedi.viktor.com.findit.ui.show_first_tips;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

import findit.sedi.viktor.com.findit.App;
import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.data_providers.data.User;

public class ShowTipsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.first_tainiks_tips);


        initData();


    }

    private void initData() {

        mAdapter = new RecyclerAdapter();
        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        User user = App.instance.getAccountManager().getUser();
        mAdapter.setItems((ArrayList<String>) App.instance.getTournamentManager().getTournament(user.getTournamentID()).getTips());


    }

}
