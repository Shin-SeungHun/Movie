package com.ssh.movie.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ssh.movie.R;
import com.ssh.movie.adapter.ItemAdapter;
import com.ssh.movie.adapter.MessageAdapter;
import com.ssh.movie.model.Item;
import com.ssh.movie.model.Message;
import com.ssh.movie.model.SubItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    LinearLayout frame1, frame2;
    ImageButton btn1, btn2;
    RecyclerView recycler_view;
    TextView tv_welcome;
    EditText et_msg;
    ImageButton btn_send;
    List<Message> messageList;
    MessageAdapter messageAdapter;
    String[] list = {"영화", "드라마", "애니"};

    int movieList[] = {R.drawable.movie1, R.drawable.movie2, R.drawable.movie3, R.drawable.movie4, R.drawable.movie5};
    int dramaList[] = {R.drawable.drama1, R.drawable.drama2, R.drawable.drama3, R.drawable.drama4, R.drawable.drama5};
    int aniList[] = {R.drawable.ani1, R.drawable.ani2, R.drawable.ani3, R.drawable.ani4, R.drawable.ani5};

    private ArrayList<ArrayList<SubItem>> allMovieList = new ArrayList();
    private ArrayList<ArrayList<SubItem>> allDramaList = new ArrayList();
    private ArrayList<ArrayList<SubItem>> allAniList = new ArrayList();
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    //secret key 는 openai 홈페이지 가셔서 직접 복사해서 넣으면 됨
    private static final String MY_SECRET_KEY = "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";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.movieToolbar);

        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);//뒤로가기
        //getSupportActionBar().setTitle("");

        //프레임레이아웃 화면 id연동
        frame1 = (LinearLayout) findViewById(R.id.frame1);
        frame2 = (LinearLayout) findViewById(R.id.frame2);
        //frame3 = (LinearLayout) findViewById(R.id.frame3);

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);

        btn1.setOnClickListener(mClick);
        btn2.setOnClickListener(mClick);


        recycler_view = findViewById(R.id.recycler_view);
        tv_welcome = findViewById(R.id.tv_welcome);
        et_msg = findViewById(R.id.et_msg);
        btn_send = findViewById(R.id.btn_send);

        recycler_view.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        recycler_view.setLayoutManager(manager);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        recycler_view.setAdapter(messageAdapter);

        btn_send.setOnClickListener(view -> {
            String question = et_msg.getText().toString().trim();
            addToChat(question, Message.SENT_BY_ME);
            et_msg.setText("");
            callAPI(question);
            tv_welcome.setVisibility(View.GONE);
        });

        //연결시간 설정. 60초/120초/60초
        client = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();


        // 상위 리사이클러뷰 설정
        RecyclerView rvItem = findViewById(R.id.rv_item);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        ItemAdapter itemAdapter = new ItemAdapter(buildItemList());
        rvItem.setAdapter(itemAdapter);
        rvItem.setLayoutManager(layoutManager);
    }


    // 상위아이템 큰박스 아이템을 3개 만듭니다.
    private List<Item> buildItemList() {
        List<Item> itemList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {

            Item item = new Item(list[i], buildSubItemList(i));
            itemList.add(item);
        }
        return itemList;
    }


    // 그안에 존재하는 하위 아이템 박스(5개씩 보이는 아이템들)
    private List<SubItem> buildSubItemList(int pos) {
        List<SubItem> subItemList = new ArrayList<>();
        if (pos == 0) {
            for (int i = 0; i < movieList.length; i++) {
                SubItem subItem = new SubItem(movieList[i]);
                subItemList.add(subItem);
            }

        } else if (pos == 1) {
            for (int i = 0; i < dramaList.length; i++) {
                SubItem subItem = new SubItem(dramaList[i]);
                subItemList.add(subItem);
            }
        } else if (pos == 2) {
            for (int i = 0; i < aniList.length; i++) {
                SubItem subItem = new SubItem(aniList[i]);
                subItemList.add(subItem);
            }
        }

        return subItemList;
    }


    void addToChat(String message, String sentBy) {
        runOnUiThread(() -> {
            messageList.add(new Message(message, sentBy));
            messageAdapter.notifyDataSetChanged();
            recycler_view.smoothScrollToPosition(messageAdapter.getItemCount());
        });
    }

    void addResponse(String response) {
        messageList.remove(messageList.size() - 1);
        addToChat(response, Message.SENT_BY_BOT);
    }


    View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            frame1.setVisibility(View.INVISIBLE);
            frame2.setVisibility(View.INVISIBLE);
            int id = v.getId();
            switch (id) {
                case R.id.btn1:
                    frame1.setVisibility(View.VISIBLE);
                    break;
                case R.id.btn2:
                    frame2.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;


            }
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 메인 툴바 메뉴 생성
        getMenuInflater().inflate(R.menu.movie_toolbar, menu);


        return true;
    }

    //앱바(App Bar)에 표시된 액션 또는 오버플로우 메뉴가 선택되면
    //액티비티의 onOptionsItemSelected() 메서드가 호출

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.search) {
            Intent memo = new Intent(MainActivity.this, Search.class);
            startActivity(memo);
            return true;
        } else if (itemId == R.id.myPage) {
            //                Intent intent = new Intent(WorkoutMainActivity.this, MyDialogActivity.class);
//                 String tvTodayText = tvToday.getText().toString();
//
//                intent.putExtra("tvTodayText", tvTodayText);
//                startActivity(intent);
            Intent memoAdd = new Intent(MainActivity.this, Mypage.class);
// tvToday의 텍스트를 인텐트하여 다른 액티비티로 전달
            //tvTodayText = tvToday.getText().toString();

            // memoAdd.putExtra("tvTodayText", tvTodayText);

            startActivity(memoAdd);
            return true;
//            case R.id.AllDel:
//
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void callAPI(String question) {
        //okhttp
        messageList.add(new Message("...", Message.SENT_BY_BOT));

        //추가된 내용
        JSONArray arr = new JSONArray();
        JSONObject baseAi = new JSONObject();
        JSONObject userMsg = new JSONObject();
        try {
            //AI 속성설정
            baseAi.put("role", "user");
            baseAi.put("content", "You are a helpful and kind AI Assistant. And you are the movie expert. There are no movies you don't know.");
            //유저 메세지
            userMsg.put("role", "user");
            userMsg.put("content", question);
            //array로 담아서 한번에 보낸다
            arr.put(baseAi);
            arr.put(userMsg);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JSONObject object = new JSONObject();
        try {
            //모델명 변경
            object.put("model", "gpt-3.5-turbo");
            object.put("messages", arr);
//            아래 put 내용은 삭제하면 된다
//            object.put("model", "text-davinci-003");
//            object.put("prompt", question);
//            object.put("max_tokens", 4000);
//            object.put("temperature", 0);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(object.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")  //url 경로 수정됨
                .header("Authorization", "Bearer " + MY_SECRET_KEY)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load response due to " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        //아래 result 받아오는 경로가 좀 수정되었다.
                        String result = jsonArray.getJSONObject(0).getJSONObject("message").getString("content");
                        addResponse(result.trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    addResponse("Failed to load response due to " + response.body().string());
                }
            }
        });
    }

}

