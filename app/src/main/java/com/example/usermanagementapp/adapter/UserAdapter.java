package com.example.usermanagementapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.usermanagementapp.R;
import com.example.usermanagementapp.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {

    private List<User> users = new ArrayList<>();
    private OnItemClickListener listener;
    private OnDeleteClickListener deleteListener;

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new UserHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        User currentUser = users.get(position);
        holder.textViewName.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
        Glide.with(holder.imageViewAvatar.getContext())
                .load(currentUser.getAvatar())
                .into(holder.imageViewAvatar);

        // הוספת אנימציות מתקדמות לפריטים
        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public User getUserAt(int position) {
        return users.get(position);
    }

    public List<User> getUsers() {
        return users;
    }

    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(User user);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteListener = listener;
    }

    private void setAnimation(View viewToAnimate, int position) {
        // יצירת אפקט של שינוי גודל, סיבוב והזזה
        Random random = new Random();
        int animationType = random.nextInt(3); // בחירת אנימציה אקראית

        switch (animationType) {
            case 0:
                ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setDuration(500);
                viewToAnimate.startAnimation(scaleAnimation);
                break;
            case 1:
                RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setDuration(500);
                viewToAnimate.startAnimation(rotateAnimation);
                break;
            case 2:
                TranslateAnimation translateAnimation = new TranslateAnimation(-100f, 0f, 100f, 0f);
                translateAnimation.setDuration(500);
                viewToAnimate.startAnimation(translateAnimation);
                break;
        }
    }

    class UserHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private ImageView imageViewAvatar;
        private Button deleteButton;

        public UserHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.userName);
            imageViewAvatar = itemView.findViewById(R.id.userAvatar);
            deleteButton = itemView.findViewById(R.id.deleteUserButton);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(users.get(position));
                }
            });

            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (deleteListener != null && position != RecyclerView.NO_POSITION) {
                    deleteListener.onDeleteClick(users.get(position));
                }
            });
        }
    }
}