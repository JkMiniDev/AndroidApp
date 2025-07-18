package com.jkminidev.clashberry.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jkminidev.clashberry.R;
import com.jkminidev.clashberry.models.Attack;
import com.jkminidev.clashberry.models.Member;

import java.util.ArrayList;
import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {
    
    private List<Member> members = new ArrayList<>();
    private Context context;

    public MemberAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_member, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        Member member = members.get(position);
        holder.bind(member, context);
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public void updateMembers(List<Member> newMembers) {
        this.members.clear();
        this.members.addAll(newMembers);
        notifyDataSetChanged();
    }

    static class MemberViewHolder extends RecyclerView.ViewHolder {
        private TextView thEmoji;
        private TextView memberName;
        private TextView memberTag;
        private TextView mapPosition;
        private LinearLayout attacksContainer;

        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            thEmoji = itemView.findViewById(R.id.thEmoji);
            memberName = itemView.findViewById(R.id.memberName);
            memberTag = itemView.findViewById(R.id.memberTag);
            mapPosition = itemView.findViewById(R.id.mapPosition);
            attacksContainer = itemView.findViewById(R.id.attacksContainer);
        }

        public void bind(Member member, Context context) {
            thEmoji.setText(member.getThEmoji());
            memberName.setText(member.getName());
            memberTag.setText(member.getTag());
            mapPosition.setText(String.valueOf(member.getMapPosition()));
            
            // Clear previous attacks
            attacksContainer.removeAllViews();
            
            // Add attacks
            if (member.getAttacks() != null && !member.getAttacks().isEmpty()) {
                for (Attack attack : member.getAttacks()) {
                    View attackView = LayoutInflater.from(context)
                            .inflate(R.layout.item_attack, attacksContainer, false);
                    
                    TextView starsText = attackView.findViewById(R.id.starsText);
                    TextView destructionText = attackView.findViewById(R.id.destructionText);
                    
                    // Create stars display
                    StringBuilder starsBuilder = new StringBuilder();
                    for (int i = 0; i < 3; i++) {
                        if (i < attack.getStars()) {
                            starsBuilder.append("★");
                        } else {
                            starsBuilder.append("☆");
                        }
                    }
                    
                    starsText.setText(starsBuilder.toString());
                    destructionText.setText(String.format("%.0f%%", attack.getDestructionPercentage()));
                    
                    attacksContainer.addView(attackView);
                }
            } else {
                // No attacks view
                View noAttacksView = LayoutInflater.from(context)
                        .inflate(R.layout.item_attack, attacksContainer, false);
                
                TextView starsText = noAttacksView.findViewById(R.id.starsText);
                TextView destructionText = noAttacksView.findViewById(R.id.destructionText);
                
                starsText.setText("");
                destructionText.setText(context.getString(R.string.no_attacks_yet));
                
                attacksContainer.addView(noAttacksView);
            }
        }
    }
}