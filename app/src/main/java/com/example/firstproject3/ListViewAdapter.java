package com.example.firstproject3;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.CustomViewHolder> {
    private ArrayList<Timer_Item> arrayList;
    private Context context;
    private FirebaseFirestore firebaseFirestore;
    private String TAG = "MainActivity";

    public ListViewAdapter(ArrayList<Timer_Item> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timer_listview, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(holder.itemView).load(arrayList.get(position).getTimerImgRec()).into(holder.timerImgRec);
        holder.timerName.setText(arrayList.get(position).getTimerName());
        holder.timerGoal.setText(arrayList.get(position).getTimerGoal());
        holder.timerRecord.setText(arrayList.get(position).getTimerRecord());

        holder.timerImgRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore = FirebaseFirestore.getInstance();
                DocumentReference docRef = firebaseFirestore.collection("user timer").document(arrayList.get(position).getTimerId());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Intent intent = new Intent(context, TimerActivity.class);
                            intent.putExtra("timer_id", document.getString("timer_id"));
                            context.startActivity(intent);
                        }
                    }
                });

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder myAlertBuilder =
                        new AlertDialog.Builder(v.getContext());
                // alert??? title??? Messege ??????
                myAlertBuilder.setTitle("????????? ??????");
                myAlertBuilder.setMessage("????????? ????????? ??????????????????????");
                // ?????? ?????? (Ok ????????? Cancle ?????? )
                myAlertBuilder.setPositiveButton("???",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int which){
                        // OK ????????? ????????? ??????
                        firebaseFirestore = FirebaseFirestore.getInstance();
                        DocumentReference docRef = firebaseFirestore.collection("user timer").document(arrayList.get(position).getTimerId());

                        docRef.delete();
                    }
                });
                myAlertBuilder.setNegativeButton("?????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                // Alert??? ??????????????? ???????????? ?????????(show??? ???????????? Alert??? ?????????)
                myAlertBuilder.show();
                return true;
            }
        });


    }

    public void onClickShowAlert(View view) {

    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView timerName, timerGoal, timerRecord;
        ImageView timerImgRec;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.timerName = itemView.findViewById(R.id.timerTextName);
            this.timerGoal = itemView.findViewById(R.id.timerTextGoal);
            this.timerRecord = itemView.findViewById(R.id.timerTextRecord);
            this.timerImgRec = itemView.findViewById(R.id.timerImgRec);
        }
    }
}
