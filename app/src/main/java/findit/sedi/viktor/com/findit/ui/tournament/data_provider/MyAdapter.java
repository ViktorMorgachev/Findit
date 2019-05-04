package findit.sedi.viktor.com.findit.ui.tournament.data_provider;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.data_providers.data.Team;
import findit.sedi.viktor.com.findit.data_providers.data.Tournament;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<Tournament> mTournaments;


    public MyAdapter(Context context, List<Tournament> data) {
        mTournaments = data;
        this.inflater = inflater.from(context);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {

        View view = inflater.inflate(R.layout.item_tournament_group, parent, false);
        return new MyViewHolder(view); // vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tournamentID.setText(mTournaments.get(position).getID());
        holder.countOfTeams.setVisibility(mTournaments.get(position).getTeams().size() > 0 ? View.VISIBLE : View.GONE);
        holder.dataStart.setText(mTournaments.get(position).getDateFrom().toString());
        holder.dataFinish.setText(mTournaments.get(position).getDateTo().toString());
        holder.difficultyValue.setRating(mTournaments.get(position).getDifficulty());

        if (mTournaments.get(position).getTournamentType() == Tournament.TournamentType.One_By_One) {
            holder.typeOfTournament.setText("Каждый сам за себя");
            holder.countOfTeams.setVisibility(View.GONE);
        }

        if (mTournaments.get(position).getTournamentType() == Tournament.TournamentType.Teams) {
            holder.typeOfTournament.setText("Командная");
            holder.countOfTeams.setText(String.valueOf(mTournaments.get(position).getTeams()));
            holder.countOfTeams.setVisibility(View.VISIBLE);
        }

        holder.nameOfTournament.setText(mTournaments.get(position).getDescribe());

        holder.teamsIDs = mTournaments.get(position).getTeamsIDs();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        //  return mDataset.length;
        return mTournaments.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tournamentID;
        public TextView dataStart;
        public TextView dataFinish;
        public TextView nameOfTournament;
        public TextView typeOfTournament;
        public TextView countOfTeams;
        public TextView difficulty;
        public RatingBar difficultyValue;
        private LinearLayout mLinearLayout;
        private boolean isExpand;
        private List<String> teamsIDs;
        private List<View> childViews = new ArrayList<>();


        public MyViewHolder(View v) {
            super(v);

            mLinearLayout = v.findViewById(R.id.layout_item_parent);
            typeOfTournament = v.findViewById(R.id.tv_tournament_type);
            tournamentID = v.findViewById(R.id.tv_id_rournament);

            mLinearLayout.setOnClickListener(this);


        }

        private void showChilds() {


            LayoutInflater layoutInflater = LayoutInflater.from(mLinearLayout.getContext());

            // в LinearLayout вставляем нащ View и инициализизируем и показываем значени
            for (int i = 0; i < teamsIDs.size(); i++) {

                int finalI = i;

                View view = layoutInflater.inflate(R.layout.item_card_tournament_child, mLinearLayout);

                ((TextView) view.findViewById(R.id.tv_count_of_players)).setText(teamsIDs.get(finalI));
                ((TextView) view.findViewById(R.id.tv_name)).setText
                        ((ManagersFactory.getInstance().getTeamManager().getTeam(teamsIDs.get(finalI))).getName());

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Запускаем активность и передаём туда ID турнира и ID команды в которую хочет
                        // попасть участник
                        String TournamentID = teamsIDs.get(finalI);
                        String teamID = tournamentID.getText().toString();

                    }
                });

                childViews.add(view);

            }
        }

        @Override
        public void onClick(View v) {
            // Добавляем или удаляем детей и сначала скрываем
            if (!isExpand && ManagersFactory.getInstance().getTournamentManager().getTournament(tournamentID.getText().toString()).getTeams().size() > 0)
                showChilds();
            else if (isExpand) {
                hideChilds();
            }
        }

        private void hideChilds() {

            // Просто удаляем все кроме первого
            for (int i = 0; i < childViews.size(); i++) {
                mLinearLayout.removeView(childViews.get(i));
            }

            childViews.clear();

        }
    }
}
