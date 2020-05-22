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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.CardViewHolder> {

    private Context mContext;
    private List<OurData> NewsList;
    private TextView empty_bookmark;

    public BookmarkAdapter(List<OurData> newsList, Context context, TextView empty_bookmark) {
        this.mContext = context;
        this.NewsList = newsList;
        this.empty_bookmark = empty_bookmark;
    }


    @NonNull
    @Override   // create a viewholder instance and return type is  instance of CreateViewHolder class
    public BookmarkAdapter.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.bookmarkscard, null);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookmarkAdapter.CardViewHolder holder, final int position) {

        final SharedPreferences pref = mContext.getApplicationContext().getSharedPreferences("MyFavNews", 0);
        final SharedPreferences.Editor editor = pref.edit();
        final OurData ourData = NewsList.get(position);

        holder.bTitle.setText(ourData.getTitle());
        holder.bTime.setText(ourData.getDate());
        holder.bSource.setText(ourData.getSource());
        Glide.with(mContext).load(ourData.getImagecheck()).into(holder.bImage);

//        if(!pref.contains(ourData.getId())){
////                Gson g = new Gson();
////                String jsondata = g.toJson(ourData);
////                editor.putString(ourData.getId(), jsondata);
//            holder.bBookmark.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
//
//            editor.commit();
//        }
//        else
        if(pref.contains(ourData.getId())){
            //editor.remove(ourData.getId());
            holder.bBookmark.setImageResource((R.drawable.filled_bookmark));
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
        holder.bBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.remove(ourData.getId());
                Toast.makeText(mContext, ourData.getTitle()+" was removed from bookmarks", Toast.LENGTH_SHORT).show();
                editor.commit();
                NewsList.remove(position);
                if(NewsList.size() == 0){
                    empty_bookmark.setVisibility(View.VISIBLE);
                }
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, NewsList.size());
                notifyDataSetChanged();
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longclick(position,v);
                return true;
            }
        });

//        holder.bBookmark.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!pref.contains(ourData.getId())){
//                    Gson g = new Gson();
//                    String jsondata = g.toJson(ourData);
//                    editor.putString(ourData.getId(), jsondata);
//                    holder.bBookmark.setImageResource((R.drawable.filled_bookmark));
//                    editor.commit();
//                }
//                else if(pref.contains(ourData.getId())){
//                    editor.remove(ourData.getId());
//                    holder.bBookmark.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
//                    //editor.clear();
//                    editor.commit();
//                }
//            }
//        });

    }

    private void longclick(final int position, final View view) {

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
//        if(!pref.contains(ourData.getId())){
//            dialogueBookmark.setImageResource(R.drawable.detailedbookmarkbigsize);
//        }
//        else
        if(pref.contains(ourData.getId())){
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
                if(pref.contains(ourData.getId())){
                    Toast.makeText(mContext, ourData.getTitle()+" was removed from bookmarks", Toast.LENGTH_SHORT).show();
                    editor.remove(ourData.getId());
//                    ImageView curr = (ImageView)v;
//                    curr.setImageResource(R.drawable.detailedbookmarkbigsize);
//                    ImageView backcurr = (ImageView)view.findViewById(R.id.bookmarknewscardbookmark);
//                    backcurr.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                    //editor.clear();

                    editor.commit();
                    dialog.dismiss();


                    if(NewsList.size() == 0){
                        empty_bookmark.setVisibility(View.VISIBLE);
                    }
                    else{
                        NewsList.remove(position);
                    }

                    if(NewsList.size() == 0){
                        empty_bookmark.setVisibility(View.VISIBLE);
                    }
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, NewsList.size());
                    notifyDataSetChanged();
                }
            }
        });

        //Toast.makeText(mContext, "Why did you do that? That REALLY hurts!!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return NewsList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {

        ImageView bImage, bBookmark;
        TextView bTitle, bTime, bSource, empty_bookmark;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);

            mContext = itemView.getContext();
            bTitle = (TextView) itemView.findViewById(R.id.bookmarknewscardtitle);
            bTime = (TextView) itemView.findViewById(R.id.bookmarknewscardtime);
            bSource = (TextView) itemView.findViewById(R.id.bookmarknewscardsource);
            bImage = (ImageView) itemView.findViewById(R.id.bookmarknewscardimage);
            bBookmark = (ImageView) itemView.findViewById(R.id.bookmarknewscardbookmark);
            //empty_bookmark = (TextView) itemView.findViewById(R.id.empty_bookmark);
        }
    }
}
