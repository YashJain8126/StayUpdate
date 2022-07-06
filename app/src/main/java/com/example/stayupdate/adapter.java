package com.example.stayupdate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class adapter extends RecyclerView.Adapter<adapter.viewHolder> {
    Context context;
    JSONArray news;

    public adapter(Context context, JSONArray news) {
        this.context = context;
        this.news = news;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.list_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        String title,description,timeStamps,url,urlToImage;
        try {
            JSONObject object=news.getJSONObject(position);
            title=object.getString("title");
            description=object.getString("description");
            timeStamps=object.getString("publishedAt");
            url=object.getString("url");
            urlToImage=object.getString("urlToImage");

            holder.title.setText(title);
            holder.description.setText(description);
            holder.timeStamps.setText(timeStamps);
            holder.read.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    context.startActivity(intent);
                }
            });
            if(!urlToImage.isEmpty()) {
                try {
                    Picasso.with(context).load(urlToImage).into(holder.image);
                }
                catch (Exception e){

                }
            }
            holder.title.setText(title);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return news.length();
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
        TextView title,description,timeStamps;
        CardView read;
        ImageView image;
        public viewHolder(View itemView){
            super(itemView);
            title=itemView.findViewById(R.id.title);
            description=itemView.findViewById(R.id.description);
            timeStamps=itemView.findViewById(R.id.timestamp);
            read=itemView.findViewById(R.id.read);
            image=itemView.findViewById(R.id.image);
        }
    }
}
