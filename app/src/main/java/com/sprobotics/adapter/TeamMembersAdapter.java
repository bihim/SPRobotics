package com.sprobotics.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sprobotics.R;
import com.sprobotics.model.TeamMembersStaticModel;

import java.util.ArrayList;

public class TeamMembersAdapter extends RecyclerView.Adapter<TeamMembersAdapter.TeamMembersViewHolder> {

    final ArrayList<TeamMembersStaticModel> teamMembersStaticModelArrayList;
    final Context context;

    public TeamMembersAdapter(ArrayList<TeamMembersStaticModel> teamMembersStaticModelArrayList, Context context) {
        this.teamMembersStaticModelArrayList = teamMembersStaticModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public TeamMembersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TeamMembersViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_team_members, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TeamMembersViewHolder holder, int position) {
        holder.name.setText(teamMembersStaticModelArrayList.get(position).getName());
        holder.designation.setText(teamMembersStaticModelArrayList.get(position).getDesignation());
        holder.description.setText(teamMembersStaticModelArrayList.get(position).getDescription());
        holder.imageView.setImageResource(teamMembersStaticModelArrayList.get(position).getImageId());
    }

    @Override
    public int getItemCount() {
        return teamMembersStaticModelArrayList.size();
    }

    public class TeamMembersViewHolder extends RecyclerView.ViewHolder {
        TextView name, designation, description;
        ImageView imageView;
        public TeamMembersViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.team_name);
            designation = itemView.findViewById(R.id.team_designation);
            description = itemView.findViewById(R.id.team_description);
            imageView = itemView.findViewById(R.id.team_image);
        }
    }
}
