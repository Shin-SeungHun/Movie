package com.ssh.movie.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ssh.movie.R;

public class Search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


//        Toolbar toolbar = findViewById(R.id.searchToolbar);
//
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//뒤로가기
//        getSupportActionBar().setTitle("");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_toolbar, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setQueryHint("프로그램, 영화, 배우 검색...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String s) {
                Toast.makeText(getApplicationContext(), "입력중입니다.", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String s) {
                Toast.makeText(getApplicationContext(), "검색을 완료했습니다.", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        MenuItem item_like = menu.add(0, 0, 0, "히든 메뉴");
        item_like.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) { // 뒤로가기 버튼을 눌렀을 때
            onBackPressed(); // 현재 액티비티를 종료하고 이전 화면으로 돌아갑니다.
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}