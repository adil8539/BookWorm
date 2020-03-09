package com.ash.bookworm.fragments.explore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ash.bookworm.R;
import com.ash.bookworm.activities.ScannerActivity;
import com.ash.bookworm.helpers.list_adapters.SearchListAdapter;
import com.ash.bookworm.helpers.models.Book;
import com.ash.bookworm.helpers.utilities.BooksUtil;

import java.util.ArrayList;
import java.util.List;

public class ExploreFragment extends Fragment {
    private View root;
    private SearchView searchBar;
    private RecyclerView resultsRv;
    private ImageView scanBarcode;
    private int waitingTime = 200;
    private CountDownTimer timer;
    private String ISBN;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_explore, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Explore");

        findViews();

        final List<Book> books = new ArrayList<>();
        final SearchListAdapter adapter = new SearchListAdapter(books, 2, getParentFragmentManager());
        resultsRv.setHasFixedSize(true);
        resultsRv.setLayoutManager(new LinearLayoutManager(getContext()));
        resultsRv.setAdapter(adapter);

        scanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivityForResult(new Intent(getActivity(), ScannerActivity.class),32);
                startActivityForResult(new Intent(getActivity(), ScannerActivity.class),32);
            }
        });

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(resultsRv.getContext(),
                DividerItemDecoration.VERTICAL);
        resultsRv.addItemDecoration(mDividerItemDecoration);

        searchBar.setActivated(true);
        searchBar.onActionViewExpanded();
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String s) {
                if (!s.trim().equals("") && s.trim().length() > 2) {
                    if(timer != null){
                        timer.cancel();
                    }
                    timer = new CountDownTimer(waitingTime, 500) {

                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            BooksUtil.searchBooks(getContext(), adapter, s.trim());
                        }
                    };
                    timer.start();
                }
                return false;
            }
        });

        return root;
    }

    private void findViews() {
        searchBar = root.findViewById(R.id.search_bar);
        resultsRv = root.findViewById(R.id.rv_results);
        scanBarcode = root.findViewById(R.id.barcode_button);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==32 && resultCode== Activity.RESULT_OK){
            ISBN = data.getStringExtra("ISBN");
            Toast.makeText(getContext(),ISBN, Toast.LENGTH_SHORT).show();

        }
    }
}