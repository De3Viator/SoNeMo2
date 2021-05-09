package com.team.sonemo.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.team.sonemo.R;
import com.team.sonemo.Model.PostModel;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder>  {
    private DatabaseReference comRef;
    private List<PostModel> commentList = new ArrayList();
    private OnItemTypeClickListener listener;

    public CommentAdapter ( OnItemTypeClickListener listener){
        this.listener = listener;
    }

    public void setCommentList(List<PostModel> commentList){
        this.commentList = commentList;
        notifyDataSetChanged();
    }

    public CommentAdapter.CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comment, parent, false);
        return new CommentAdapter.CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentHolder holder, int position) {
        holder.onBindComment(commentList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
    public interface OnItemTypeClickListener{
        void onItemClick(String type, PostModel model);
    }


    class CommentHolder extends RecyclerView.ViewHolder {
        private ImageView ivCommentAvatar;
        private TextView txtCommentDescription, txtCommentUsername;
        private EditText etComment;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            ivCommentAvatar = itemView.findViewById(R.id.ivCommentAvatar);
            txtCommentUsername= itemView.findViewById(R.id.txtCommentUserName);
            txtCommentDescription= itemView.findViewById(R.id.txtCommentDesciption);
            etComment = itemView.findViewById(R.id.etComment);
        }

        public void onBindComment(PostModel model, OnItemTypeClickListener listener){
            String commentUsername = model.getPuName();
            ArrayList <String> commentDescription = model.getComments();


           if (commentDescription.size()>=1)txtCommentDescription.setText(commentDescription.get(commentDescription.size()-1));
            txtCommentUsername.setText(commentUsername);
            itemView.setOnClickListener(v-> {
                listener.onItemClick("comment", model);
            });
        }
    }


}
