package findit.sedi.viktor.com.findit.ui.show_first_tips;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import findit.sedi.viktor.com.findit.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.TipsHolder> {

    private List<String> mStringList = new ArrayList<>();


    @Override
    public TipsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tip_info, parent, false);
        return new TipsHolder(view);
    }

    @Override
    public void onBindViewHolder(TipsHolder holder, int position) {
        holder.bind(mStringList.get(position));
    }

    @Override
    public int getItemCount() {
        return mStringList.size();
    }

    public void setItems(ArrayList<String> tips) {
        mStringList.addAll(tips);
        notifyDataSetChanged();
    }


    class TipsHolder extends RecyclerView.ViewHolder {
        // Ваш ViewHolder должен содержать переменные для всех
        // View-компонентов, которым вы хотите задавать какие-либо свойства
        // в процессе работы пользователя со списком
        private TextView tipInfo;

        // Мы также создали конструктор, который принимает на вход View-компонент строкИ
        // и ищет все дочерние компоненты
        public TipsHolder(View itemView) {
            super(itemView);
            tipInfo = itemView.findViewById(R.id.tv_tip);
        }

        public void bind(String tip) {
            tipInfo.setText(tip);
        }


    }

}
