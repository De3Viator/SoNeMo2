package com.team.sonemo.Adapter;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;
import com.team.sonemo.R;

import com.team.sonemo.Model.PostModel;
import com.team.sonemo.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {
    private List<PostModel> postList;
    private OnPostClickListener<PostModel> listener;
    private DatabaseReference likeRef;
    private DatabaseReference postRef;
    boolean processLike = false;
    private PostHolder holder;
    public static String MORE = "MORE";
    public static String LIKE = "LIKE";
    public static String COMMENT = "COMMENT";
    public static String SHARE = "SHARE";
    String uid;

    public PostAdapter(List<PostModel> postList, OnPostClickListener<PostModel> listener) {
        this.postList = postList;
        this.listener = listener;
        notifyDataSetChanged();
    }

    public PostAdapter() {
    }

    @NonNull
    @Override
    public PostAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_posts, parent, false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostHolder holder, int position) {
        holder.onBind(postList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public interface OnPostClickListener<T> {
        void onClick(String event, T model);
    }


    class PostHolder extends RecyclerView.ViewHolder {
        private ImageView ivPictureUser, ivShowPost;
        private TextView txtpuName, txtpTime, txtpTitle, txtpDescription, txtpLikes;
        private ImageButton imgbtnMore;
        private Button btnLike, btnShare, btnComment;

        public PostHolder(@NonNull View itemView) {

            super(itemView);
            ivPictureUser = itemView.findViewById(R.id.ivPictureUser);
            ivShowPost = itemView.findViewById(R.id.ivShowPost);
            txtpuName = itemView.findViewById(R.id.txtpuName);
            txtpDescription = itemView.findViewById(R.id.txtpDescription);
            txtpLikes = itemView.findViewById(R.id.txtLike);
            btnLike = itemView.findViewById(R.id.btnLike);
            btnShare = itemView.findViewById(R.id.btnShare);
            btnComment = itemView.findViewById(R.id.btnDetailComment);
        }

        public void onBind(PostModel model, OnPostClickListener listener) {
            String uid = model.getuId();
            String puName = model.getPuName();
            String puTitle = model.getPostTitle();
            String puDescription = model.getPostDescription();
            String pImage = model.getPostImage();
            String pTimeStape = model.getpTimeStape();
            String puPicture = model.getPuPicture();
            ArrayList<String> pLikes = model.getpLikes();
            String postId = model.getPostId();

            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            String pTime = DateFormat.format("dd/MM/yyyy hh:mm:aa", calendar).toString();
            //calendar.setTimeInMillis(Long.parseLong(pTimeStape));

            txtpDescription.setText(puDescription);
            txtpTitle.setText(puTitle);
            txtpuName.setText(puName);
            txtpTime.setText(pTime);
            txtpLikes.setText(pLikes.size() + "Likes");

            imgbtnMore.setOnClickListener(v -> listener.onClick(MORE, model));

            btnLike.setOnClickListener(v -> listener.onClick(LIKE, model));

            btnShare.setOnClickListener(v -> listener.onClick(SHARE, model));

            btnComment.setOnClickListener(v -> listener.onClick(COMMENT, model));


            try {
                Picasso.get().load(puPicture).placeholder(R.drawable.ic_deafult_img).into(ivPictureUser);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (pImage != null) {
                if (pImage.equals("NoImage")) {
                    ivShowPost.setVisibility(View.GONE);

                } else {
                    try {
                        Picasso.get().load(pImage).into(ivShowPost);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


        }
    }
}
