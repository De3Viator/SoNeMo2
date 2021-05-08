package com.team.sonemo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.team.sonemo.Messager;
import com.team.sonemo.R;
import com.team.sonemo.Model.Users;
//import net.lebdeveloper.firebaseapp.Model.Users;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    //FirebaseAuth mAuth;
    private Context context;
    private List<Users> mUsers;
    private boolean isChat;

    //Constructor
    public UserAdapter(Context context, List<Users> mUsers, boolean isChat) {
        this.context = context;
        this.mUsers = mUsers;
        this.isChat = isChat;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.users_item,
                parent,
                false);


        return new  UserAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Users users = mUsers.get(position);
        holder.getUsername().setText(users.getUsername());

        if (users.getImageURL() != null && users.getImageURL().equals("default")){
            holder.imageView.setImageResource(R.mipmap.ic_launcher);
        }else {
            Glide.with(context)
                    .load(users.getImageURL())
                    .into(holder.imageView);
        }

        //Status Check
        if (isChat){
            if (users.getStatus() != null && users.getStatus().equals("online")) {
                holder.imageViewOn.setVisibility(View.VISIBLE);
                holder.imageViewOff.setVisibility(View.GONE);
            }else {
                holder.imageViewOn.setVisibility(View.GONE);
                holder.imageViewOff.setVisibility(View.VISIBLE);
            }
        }else {
            holder.imageViewOn.setVisibility(View.GONE);
            holder.imageViewOff.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Messager.class);
                i.putExtra("id", users.getId());
                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView username;
        public ImageView imageView;
        public ImageView imageViewOn;
        public ImageView imageViewOff;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            setUsername(itemView.findViewById(R.id.username));
            imageView = itemView.findViewById(R.id.imageView);
            imageViewOn = itemView.findViewById(R.id.statusIMGoN);
            imageViewOff = itemView.findViewById(R.id.statusIMGoFF);

        }

        public TextView getUsername() {
            return username;
        }

        public void setUsername(TextView username) {
            this.username = username;
        }
    }

}
