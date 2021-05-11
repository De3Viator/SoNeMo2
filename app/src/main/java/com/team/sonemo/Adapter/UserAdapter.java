package com.team.sonemo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.team.sonemo.Messager;
import com.team.sonemo.Model.UserModel;
import com.team.sonemo.R;
import com.team.sonemo.Model.Users;
import com.team.sonemo.data.FirebaseHelper;
//import net.lebdeveloper.firebaseapp.Model.Users;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    //FirebaseAuth mAuth;
    private Context context;
    private List<UserModel> mUsers;
    private boolean isChat;
    DatabaseReference reference;


    //Constructor
    public UserAdapter(Context context, List<UserModel> mUsers, boolean isChat) {
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

        final UserModel users = mUsers.get(position);
        holder.getUsername().setText(users.getUsername());

        Query query = reference.orderByChild("email").equalTo(users.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String username = "" + ds.child("username").getValue();
                    String country = "" + ds.child("country").getValue();
                    String image = "" + ds.child("image").getValue();
                    String age = "" + ds.child("age").getValue();


                    if (image != null) FirebaseHelper.getInstance()
                            .getReference(image)
                            .getDownloadUrl()
                            .addOnSuccessListener(uri ->
                                    Picasso.get().load(uri).placeholder(R.drawable.ic_deafult_img).into(holder.imageView))
                            .addOnFailureListener(e -> Log.e("Firebase storage:",e.getLocalizedMessage()));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*if (users.getImageURL() != null && users.getImageURL().equals("default")){
            holder.imageView.setImageResource(R.mipmap.ic_launcher);
        }else {
            Glide.with(context)
                    .load(users.getImageURL())
                    .into(holder.imageView);
        }*/

        //Status Check
        if (isChat){
            if (users.getStatus() != null && users.getStatus().equals("online")) {
                holder.imageViewOn.setVisibility(View.VISIBLE);
            }else {
                holder.imageViewOn.setVisibility(View.GONE);
            }
        }else {
            holder.imageViewOn.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Messager.class);
                i.putExtra("id", users.getUid());
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            setUsername(itemView.findViewById(R.id.username));
            imageView = itemView.findViewById(R.id.imageView);
            imageViewOn = itemView.findViewById(R.id.statusIMGoN);

        }

        public TextView getUsername() {
            return username;
        }

        public void setUsername(TextView username) {
            this.username = username;
        }
    }

}
