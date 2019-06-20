package findit.sedi.viktor.com.findit.ui.rating.data_provider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import findit.sedi.viktor.com.findit.App;
import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.data_providers.data.Player;
import findit.sedi.viktor.com.findit.data_providers.data.User;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Player> mPlayers;


    public MyAdapter(Context context, ArrayList<Player> data) {
        mPlayers = data;

        // Добавляем в список себя чтобы видеть ещё свой рейтинг
        User user = ManagersFactory.getInstance().getAccountManager().getUser();

        // Добавляем пользователя только  тогда, если его почта не существует в списке
        boolean userExists = false;

        for (int i = 0; i < mPlayers.size(); i++) {
            if (user.getID().equalsIgnoreCase(mPlayers.get(i).getID())) {
                userExists = true;
                break;
            }
        }

        if (!userExists)
            mPlayers.add(new Player(user.getBonus(), user.getName(), user.getPhotoUrl(),
                    user.getID(), user.isNetStatus(), user.getTournamentID(), user.getTeamID(),
                    user.getTotalBonus(), user.getLatitude(), user.getLongtude(), user.getGender(), user.getSumOfFondedPoints(), user.getSumOfDiscoveredPoints()));


        // Сортируем данные чв порядке уменьшения балов


        this.inflater = LayoutInflater.from(context);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {

        View view = inflater.inflate(R.layout.item_rating_group, parent, false);
        // Получаем нормальную View из вложенного View
        return new MyViewHolder(view); // vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.mTextViewTotalBonus.setText(App.instance.getResources().getString(R.string.bonus) + ": " + mPlayers.get(position).getTotalBonus());
        holder.mTextViewRatingPosition.setText(String.valueOf(position + 1) + ".");
        holder.mTextViewPlayersName.setText(mPlayers.get(position).getName());

        holder.mPlayer = mPlayers.get(position);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mPlayers.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private TextView mTextViewTotalBonus;
        private TextView mTextViewRatingPosition;
        private TextView mTextViewPlayersName;
        public ImageView arrow;
        private LinearLayout mLinearLayout;


        // Data for child
        private Player mPlayer;
        private boolean isExpand;
        private ArrayList<View> childView = new ArrayList<>();


        public MyViewHolder(View v) {
            super(v);

            mLinearLayout = v.findViewById(R.id.layout_item_parent);
            arrow = v.findViewById(R.id.iv_arrow);
            mTextViewPlayersName = v.findViewById(R.id.tv_profile_name);
            mTextViewRatingPosition = v.findViewById(R.id.tv_rating_number);
            mTextViewTotalBonus = v.findViewById(R.id.tv_profile_bonus);


            arrow.setImageResource(R.drawable.ic_keyboard_arrow_down_24dp);

            v.findViewById(R.id.cv_tournament).setOnClickListener(this);

        }

        private void showChilds() {


            arrow.setImageResource(R.drawable.ic_keyboard_arrow_up_24dp);
            isExpand = true;

            LayoutInflater layoutInflater = LayoutInflater.from(mLinearLayout.getContext());

            // в LinearLayout вставляем нащ View и инициализизируем и показываем значение

            View view = layoutInflater.inflate(R.layout.item_card_players_rating_child, mLinearLayout);

            ((TextView) view.findViewById(R.id.tv_profile_name_child)).setText(mPlayer.getName());
            ((TextView) view.findViewById(R.id.tv_bonus_info)).setText
                    (App.instance.getResources().getString(R.string.bonus) + ": " + mPlayer.getTotalBonus());


            if (mPlayer.getID().equalsIgnoreCase(ManagersFactory.getInstance().getAccountManager().getUser().getID()))
                ((ImageView) view.findViewById(R.id.iv_status)).setImageResource(R.drawable.ic_status_online_24dp);
            else if (mPlayer.isNet_status()) {
                ((ImageView) view.findViewById(R.id.iv_status)).setImageResource(R.drawable.ic_status_online_24dp);
            } else
                ((ImageView) view.findViewById(R.id.iv_status)).setImageResource(R.drawable.ic_status_offline_24dp);

            // Если Он учавствует в Турнире и  имеет наличия команды:
            if (!mPlayer.getTournamentID().equalsIgnoreCase("") && !mPlayer.getTeamID().equalsIgnoreCase("")) {
                ((TextView) view.findViewById(R.id.tv_tournament_name)).setVisibility(View.VISIBLE);
                ((TextView) view.findViewById(R.id.tv_team_name)).setVisibility(View.VISIBLE);

                ((TextView) view.findViewById(R.id.tv_tournament_name))
                        .setText(App.instance.getResources().getString(R.string.tounament_name) + ": " +
                                ManagersFactory.getInstance().getTournamentManager().getTournament(mPlayer.getTournamentID().trim()).getDescribe());

                ((TextView) view.findViewById(R.id.tv_team_name)).setText(App.instance.getResources().getString(R.string.team) + ": " +
                        ManagersFactory.getInstance().getTeamManager().getTeam(mPlayer.getTeamID().trim()).getName());
            }

            ((TextView) view.findViewById(R.id.tv_gender)).setText(App.instance.getResources().getTextArray(R.array.Gender)[(int) mPlayer.getGender()]);


            ((TextView) view.findViewById(R.id.tv_discovered)).setText(view.getContext().getResources().getString(R.string.discovered) + " " + mPlayer.getSumOFDiscoveredFinds());

            ((TextView) view.findViewById(R.id.tv_fonded)).setText(view.getContext().getResources().getString(R.string.fonded) + " " + mPlayer.getSumOfFondedFinds());


            childView.add(view);

        }

        @Override
        public void onClick(View v) {
            // Добавляем или удаляем детей и сначала скрываем
            if (!isExpand)
                showChilds();
            else
                hideChilds();

        }


        private void hideChilds() {

            arrow.setImageResource(R.drawable.ic_keyboard_arrow_down_24dp);
            isExpand = false;

            mLinearLayout.removeViews(1, childView.size());

            childView.clear();
            mLinearLayout.invalidate();

        }
    }
}
