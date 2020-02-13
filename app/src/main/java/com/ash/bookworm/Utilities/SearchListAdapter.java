package com.ash.bookworm.Utilities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.ash.bookworm.R;
import com.squareup.picasso.Picasso;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {
    private Book[] listData;

    // RecyclerView recyclerView;
    public SearchListAdapter(Book[] listData) {
        this.listData = listData;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View searchItem= layoutInflater.inflate(R.layout.search_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(searchItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Book book = listData[position];
        //holder.bookNameTv.setTag(R.string.TAG_BOOK_ID, listData[position].getBookId());
        holder.bookNameTv.setText(listData[position].getBookName());
        holder.authorNameTv.setText(listData[position].getAuthorName());
        Picasso.get()
                .load(listData[position].getImageUrl().replace("http","https"))
                .placeholder(R.drawable.book_placeholder)
                .into(holder.bookImage);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUtil.addBookToInventory(book);
                Toast.makeText(view.getContext(),"click on item: "+ book.getBookId(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView bookImage;
        public TextView bookNameTv, authorNameTv;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.bookImage = itemView.findViewById(R.id.book_image);
            this.bookNameTv = itemView.findViewById(R.id.tv_book_name);
            this.authorNameTv = itemView.findViewById(R.id.tv_author_name);
            linearLayout = itemView.findViewById(R.id.linear_layout);
        }
    }

}
