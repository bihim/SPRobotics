package com.sproboticworks.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.orhanobut.logger.Logger;
import com.sproboticworks.R;
import com.sproboticworks.model.FaqModel;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.FaqViewHolder> {
    final ArrayList<FaqModel> faqModelArrayList;
    final Context context;

    public FaqAdapter(ArrayList<FaqModel> faqModelArrayList, Context context) {
        this.faqModelArrayList = faqModelArrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public FaqViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FaqViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_faq, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FaqViewHolder holder, int position) {
        holder.materialButton.setText(faqModelArrayList.get(position).getButtonText());
        holder.textView.setText(faqModelArrayList.get(position).getLayoutText());
        holder.materialButton.setOnClickListener(v->{
            Logger.d("Clicked FAQ "+faqModelArrayList.get(position).getButtonText());
            if (holder.expandableLayout.isExpanded()) {
                holder.expandableLayout.collapse();
            } else {
                holder.expandableLayout.expand();
            }
        });
    }

    @Override
    public int getItemCount() {
        return faqModelArrayList.size();
    }

    class FaqViewHolder extends RecyclerView.ViewHolder {
        MaterialButton materialButton;
        TextView textView;
        ExpandableLayout expandableLayout;

        public FaqViewHolder(@NonNull View itemView) {
            super(itemView);
            materialButton = itemView.findViewById(R.id.faqbutton);
            textView = itemView.findViewById(R.id.faqText);
            expandableLayout = itemView.findViewById(R.id.faqlayout);
        }
    }
}
