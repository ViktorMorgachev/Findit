package findit.sedi.viktor.com.findit.ui.tournament.data_provider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import findit.sedi.viktor.com.findit.App;
import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.data_providers.data.Tournament;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private static Context mContext;
    private ArrayList<Tournament> mTournaments;
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");


    public MyAdapter(Context context, ArrayList<Tournament> data) {
        mTournaments = data;
        mContext = context;
        this.inflater = LayoutInflater.from(context);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {

        View view = inflater.inflate(R.layout.item_tournament_group, parent, false);
        // Получаем нормальную View из вложенного View
        return new MyViewHolder(view); // vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tournamentID.setText(App.instance.getResources().getString(R.string.id) + ": " + mTournaments.get(position).getID());

        holder.countOfTeams.setVisibility(mTournaments.get(position).getTeamsIDs().size() > 0 ? View.VISIBLE : View.GONE);


        holder.totalPlayers.setText(App.instance.getResources().getString(R.string.total_players) + ": " + mTournaments.get(position).getPlayersIDs().size());

        holder.dataStart.setText(App.instance.getResources().getString(R.string.start_date) + ": " + mSimpleDateFormat.format(mTournaments.get(position).getDateFrom().toDate()));
        holder.dataFinish.setText(App.instance.getResources().getString(R.string.end_date) + ": " + mSimpleDateFormat.format(mTournaments.get(position).getDateTo().toDate()));
        holder.difficultyValue.setRating(mTournaments.get(position).getDifficulty());

        if (mTournaments.get(position).getTournamentType() == Tournament.TournamentType.One_By_One) {
            holder.typeOfTournament.setText(App.instance.getResources().getString(R.string.tounament_type) + ": " + "Каждый сам за себя");
            holder.arrow.setVisibility(View.GONE);
            holder.countOfTeams.setVisibility(View.GONE);
        }

        if (mTournaments.get(position).getTournamentType() == Tournament.TournamentType.Teams) {
            holder.typeOfTournament.setText(App.instance.getResources().getString(R.string.tounament_type) + ": " + "Командная");
            holder.countOfTeams.setText(App.instance.getResources().getString(R.string.count_of_commands) + " " + String.valueOf(mTournaments.get(position).getTeamsIDs().size()));
            holder.arrow.setVisibility(View.VISIBLE);
            holder.countOfTeams.setVisibility(View.VISIBLE);
        }

        holder.nameOfTournament.setText(App.instance.getResources().getString(R.string.tounament_name) + ": " + mTournaments.get(position).getDescribe());

        holder.teamsIDs = mTournaments.get(position).getTeamsIDs();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
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
        public TextView totalPlayers;
        public RatingBar difficultyValue;
        public ImageView arrow;
        private LinearLayout mLinearLayout;

        // Data for child
        private boolean isExpand;
        private List<String> teamsIDs;
        private List<View> childViews = new ArrayList<>();


        public MyViewHolder(View v) {
            super(v);

            mLinearLayout = v.findViewById(R.id.layout_item_parent);
            typeOfTournament = v.findViewById(R.id.tv_tournament_type);
            tournamentID = v.findViewById(R.id.tv_id_rournament);
            countOfTeams = v.findViewById(R.id.tv_count_of_teams);
            dataStart = v.findViewById(R.id.tv_date_from);
            dataFinish = v.findViewById(R.id.tv_date_to);
            arrow = v.findViewById(R.id.iv_arrow);
            totalPlayers = v.findViewById(R.id.tv_total_players);
            nameOfTournament = v.findViewById(R.id.tv_tounament_name);
            difficulty = v.findViewById(R.id.tv_difficulty);


            arrow.setImageResource(R.drawable.ic_keyboard_arrow_down_24dp);

            v.findViewById(R.id.cv_tournament).setOnClickListener(this);
            difficultyValue = v.findViewById(R.id.rt_dificulty);

        }

        private void showChilds() {


            arrow.setImageResource(R.drawable.ic_keyboard_arrow_up_24dp);
            isExpand = true;

            LayoutInflater layoutInflater = LayoutInflater.from(mLinearLayout.getContext());

            // в LinearLayout вставляем нащ View и инициализизируем и показываем значени
            for (int i = 0; i < teamsIDs.size(); i++) {

                int finalI = i;

                View view = layoutInflater.inflate(R.layout.item_card_tournament_child, mLinearLayout);

                ((TextView) view.findViewById(R.id.tv_count_of_players)).setText(App.instance.getResources().getString(R.string.count_of_players) + ": " + ManagersFactory.getInstance().getTeamManager().getTeam(teamsIDs.get(finalI)).getPlayersIds().size());
                ((TextView) view.findViewById(R.id.tv_name)).setText
                        (App.instance.getResources().getString(R.string.name) + ": " + (ManagersFactory.getInstance().getTeamManager().getTeam(teamsIDs.get(finalI).trim()).getName()));
                ((TextView) view.findViewById(R.id.tv_count_of_bonus)).setText(App.instance.getResources().getString(R.string.count_of_bonus) + ": "
                        + ManagersFactory.getInstance().getTeamManager().getTeam(teamsIDs.get(finalI)).getBonus());

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Запускаем активность и передаём туда ID турнира и ID команды в которую хочет
                        // попасть участник
                        String TournamentID = filterData(R.string.id, tournamentID.getText().toString());
                        String teamID = teamsIDs.get(finalI).trim();


                        // Если уже принадлежим турниру
                        if (!ManagersFactory.getInstance().getAccountManager().getUser().getTournamentID().equalsIgnoreCase("")) {
                            if (ManagersFactory.getInstance().getAccountManager().getUser().getTournamentID().equalsIgnoreCase(TournamentID))
                                Toast.makeText(mContext, "Вы уже в данном турнире", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(mContext, "Вы не можете менять турнир и команду", Toast.LENGTH_SHORT).show();
                        } else showDialog(TournamentID, teamID);


                    }
                });

                childViews.add(view);

            }
        }

        private void showDialog(String tournamentID, String teamID) {


            AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
            alertDialog.setTitle("Присоединение к турниру");
            alertDialog.setMessage("Вы хотите присоединиться к данной команде?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ДА",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ManagersFactory.getInstance().getAccountManager().joinToTournament(tournamentID, teamID);
                            dialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "НЕТ",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();


        }


        @Override
        public void onClick(View v) {
            // Добавляем или удаляем детей и сначала скрываем
            if (!isExpand && ManagersFactory.getInstance()
                    .getTournamentManager().getTournament(filterData(R.string.id, tournamentID.getText().toString().trim())).getTournamentType() == Tournament.TournamentType.Teams)
                showChilds();
            else if (isExpand) {
                hideChilds();
            }
        }

        private String filterData(int RDataId, String text) throws Resources.NotFoundException {


            StringBuilder stringBuilder = new StringBuilder();

            try {
                stringBuilder.append(text.replaceFirst(App.instance.getResources().getString(RDataId), "").trim());

                // Удаляем двоеточия при необходимости
                if (stringBuilder.toString().startsWith(": ")) {
                    stringBuilder.replace(0, stringBuilder.length(), stringBuilder.toString().replace(": ", ""));
                }
                return stringBuilder.toString().trim();

            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
            return "";
        }

        private void hideChilds() {

            arrow.setImageResource(R.drawable.ic_keyboard_arrow_down_24dp);
            isExpand = false;

            // Просто удаляем все кроме первого
            for (int i = 1; i < childViews.size(); i++) {
                mLinearLayout.getChildAt(i).setVisibility(View.GONE);
            }

            mLinearLayout.removeViews(1, childViews.size());

            childViews.clear();
            mLinearLayout.invalidate();

        }
    }


}
