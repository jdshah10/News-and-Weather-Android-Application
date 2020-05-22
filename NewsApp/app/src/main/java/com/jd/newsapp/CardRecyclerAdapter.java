package com.jd.newsapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

public class CardRecyclerAdapter extends RecyclerView.Adapter<CardRecyclerAdapter.CardViewHolder> {

    private List<OurData> NewsList;
    private Context mContext;
    Toast toast;



    //RequestOptions requestOptions;

    public CardRecyclerAdapter(List<OurData> newsList, Context mContext) {
        this.NewsList = newsList;
        this.mContext = mContext;

        //Request options for glide
       // requestOptions new RequestOptions().centerCrop().placeholder(R.drawable.fallback_image).error(R.drawable.fallback_image);
    }


    @NonNull
    @Override       // create a viewholder instance and return type is  instance of CreateViewHolder class
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.latestnewscardview, null);
        return new CardViewHolder(view);
    }

    @Override       //Bind data to our elements
    public void onBindViewHolder(@NonNull final CardViewHolder holder, final int position) {
        final SharedPreferences pref = mContext.getApplicationContext().getSharedPreferences("MyFavNews", 0);
        final SharedPreferences.Editor editor = pref.edit();
        //editor.clear().commit();
        final OurData ourData = NewsList.get(position);

            holder.newsTitle.setText(ourData.getTitle());
            holder.newsLateTime.setText(ourData.getDate());
            holder.newsSource.setText(ourData.getSource());
            //holder.newsBookmark.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
            //holder.newsImage.setImageDrawable(mContext.getResources().getDrawable(ourData.getImagecheck()));
            Glide.with(mContext).load(ourData.getImagecheck()).into(holder.newsImage);
            //editor.clear();
            if(!pref.contains(ourData.getId())){
//                Gson g = new Gson();
//                String jsondata = g.toJson(ourData);
//                editor.putString(ourData.getId(), jsondata);
                holder.newsBookmark.setImageResource(R.drawable.ic_bookmark_border_black_24dp);

                editor.commit();
            }
            else if(pref.contains(ourData.getId())){
                //editor.remove(ourData.getId());
                holder.newsBookmark.setImageResource((R.drawable.filled_bookmark));
                //editor.clear();
                editor.commit();
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(v.getContext(), "Recycle Click" + position, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext,NewsDetailedPageActivity.class);
                    intent.putExtra("sentArticleId", ourData.getId());
                    mContext.startActivity(intent);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longclick(position,v);
                    return true;
                }
            });

            holder.newsBookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!pref.contains(ourData.getId())){
                        Toast.makeText(mContext, ourData.getTitle()+" was added to bookmarks", Toast.LENGTH_SHORT).show();
                        Gson g = new Gson();
                        String jsondata = g.toJson(ourData);
                        editor.putString(ourData.getId(), jsondata);
                        holder.newsBookmark.setImageResource((R.drawable.filled_bookmark));

                        editor.commit();
                    }
                    else if(pref.contains(ourData.getId())){
                        Toast.makeText(mContext, ourData.getTitle()+" was removed from bookmarks", Toast.LENGTH_SHORT).show();
                        editor.remove(ourData.getId());
                        holder.newsBookmark.setImageResource(R.drawable.ic_bookmark_border_black_24dp);

                        //editor.clear();
                        editor.commit();
                    }
                }
            });

    }

    private void longclick(int position, final View view) {

        final OurData ourData = NewsList.get(position);
        final ImageView dialogueImage, dialogueTwitter, dialogueBookmark;
        TextView dialogueTitle;
        SharedPreferences pref = mContext.getApplicationContext().getSharedPreferences("MyFavNews", 0);
        SharedPreferences.Editor editor = pref.edit();

        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.news_dialoguebox);
        dialogueImage = dialog.findViewById(R.id.dialogueimage);
        dialogueTitle = dialog.findViewById(R.id.dialoguetitle);
        dialogueTwitter = dialog.findViewById(R.id.dialoguetwitter);
        dialogueBookmark = dialog.findViewById(R.id.dialoguebookmark);

        dialogueTitle.setText(ourData.getTitle());

        Glide.with(mContext).load(ourData.getImagecheck()).into(dialogueImage);
        if(!pref.contains(ourData.getId())){
            dialogueBookmark.setImageResource(R.drawable.detailedbookmarkbigsize);
        }
        else if(pref.contains(ourData.getId())){
            dialogueBookmark.setImageResource(R.drawable.filled_bookmark);
        }

        dialog.show();
        dialogueTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String twitter_initial = "https://twitter.com/intent/tweet?text=";
                String twitter_end =  Uri.encode("Check out this Link:" + "\n" + ourData.getUrl() + "\n" + "#CSCI571NewsSearch");
                String twitterlink = twitter_initial + twitter_end;
                Uri uri = Uri.parse(twitterlink);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
                //Toast.makeText(mContext, "tweeted news", Toast.LENGTH_SHORT).show();
            }
        });

        dialogueBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = mContext.getApplicationContext().getSharedPreferences("MyFavNews", 0);
                SharedPreferences.Editor editor = pref.edit();
                if(!pref.contains(ourData.getId())){
                    //String jsonStr = "{\"aid\": ourData.getId(), \"atitle\":ourData.getTitle(), \"asource\":ourData.getSource(), \"adate\":ourData.getDate(), \"aurl\":ourData.getUrl(), \"aimage\":ourData.getImagecheck()}";
                    Gson g = new Gson();
                    String jsondata = g.toJson(ourData);
                    editor.putString(ourData.getId(), jsondata);
                    ImageView curr = (ImageView)v;
                    curr.setImageResource(R.drawable.filled_bookmark);
                    Toast.makeText(mContext, ourData.getTitle()+" was added to bookmarks", Toast.LENGTH_SHORT).show();
//                    ImageView backcurr = (ImageView)view.findViewById(R.id.latestnewscardbookmark);
//                    backcurr.setImageResource(R.drawable.filled_bookmark);
                    notifyDataSetChanged();
                    editor.commit();
                }
                else {
                    editor.remove(ourData.getId());
                    ImageView curr = (ImageView)v;
                    curr.setImageResource(R.drawable.detailedbookmarkbigsize);
                    Toast.makeText(mContext, ourData.getTitle()+" was removed from bookmarks", Toast.LENGTH_SHORT).show();
//                    ImageView backcurr = (ImageView)view.findViewById(R.id.latestnewscardbookmark);
//                    backcurr.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                    notifyDataSetChanged();
                    //editor.clear();
                    editor.commit();
                }
            }
        });
        //Toast.makeText(mContext, "Why did you do that? That REALLY hurts!!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return NewsList.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder{

        ImageView newsImage, newsBookmark;
        TextView newsTitle, newsLateTime, newsSource;
        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            newsTitle = (TextView) itemView.findViewById(R.id.latestnewscardtitle);
            newsLateTime = (TextView) itemView.findViewById(R.id.latestnewscardtime);
            newsSource = (TextView) itemView.findViewById(R.id.latestnewscardsource);
            newsImage = (ImageView) itemView.findViewById(R.id.latestnewscardimage);
            newsBookmark = (ImageView) itemView.findViewById(R.id.latestnewscardbookmark);
        }
    }
}
