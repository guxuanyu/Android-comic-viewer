package com.example.ganger.comic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.SearchView;
import android.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainMainActivity extends AppCompatActivity {

    private  List<String> data=new ArrayList<String>();
    private List<String> linkData=new ArrayList<String>();
    private SharedPreferences sharedPreferences;
    private ListView comics;
    private TextView txv;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_main);

        sharedPreferences = getSharedPreferences("wujay", Context.MODE_PRIVATE);
        comics= (ListView) findViewById(R.id.main_listView);
        comics.setTextFilterEnabled(true);
        initSearch();

        //txv= (TextView) findViewById(R.id.main_last_text);


        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initLatText();
    }
    public void initSearch(){
        searchView= (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    //comics.setFilterText(newText);
                    Pattern r = Pattern.compile(".*"+newText+".*");
                    int i=0;
                    for(String s:data){
                        Matcher m = r.matcher(s);
                        if(m.find()){
                            break;
                        }
                        i++;
                    }
                    if(i!=data.size()) {
                        //int po=data.indexOf(r);
                        //Log.i("pattern:  ",r);
                        Log.i("pos:", String.valueOf(i));
                        Log.i("data i:", data.get(i));
                        comics.smoothScrollToPosition(i);
                    }
                }
                else {
                    //comics.clearTextFilter();
                }
                return false;
            }
        });
    }

    public void initLatText(){
        txv= (TextView) findViewById(R.id.main_last_text);
        if(sharedPreferences.contains("lasturl")){
            txv.setText("上回看到:"+sharedPreferences.getString("lasttext","没有"));
            txv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MainMainActivity.this,ImageBrowseActivity2.class);
                    String url=sharedPreferences.getString("lasturl","");
                    intent.putExtra("url",url);
                    startActivity(intent);
                }
            });
        }
        else {
            txv.setText("欢迎");
        }
    }

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Log.i("size",String.valueOf(data.size()));
            comics.setAdapter(new mAdpter(data));
            return false;
        }
    });

    public void initData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    Log.i("22222222222","2222222222222222");
                    Document doc = Jsoup.connect("http://manhua.fzdm.com").timeout(10000).get();
                    Elements li=doc.select("div.round");
                    //Elements linkss = li.select("a[href]"); //带有href属性的a元素

//                    if (links != null) {
//                        //jpgList.add(linkText);
//                        Log.i("11111111111", String.valueOf(links.size()));
//                    } else
//                        Log.i("nainainai", "emememeemme");
                    for (Element link : li) {
                        Elements l = link.select("a[href]");
                        Element map=l.last();

                        String linkText = map.text();
                        String att=map.attr("href");
                        data.add(linkText);
                        linkData.add(att);
                        //Log.i("link____:",linkText);
                        //Log.i("att____:",att);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                handler.sendMessage(msg);

            }
        }).start();
    }


    class mAdpter extends BaseAdapter {
        List<String> data;
        LayoutInflater mInflater;
        public mAdpter(List list){
            data=list;
            mInflater=LayoutInflater.from(MainMainActivity.this);
        }
        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item, null);

                holder = new ViewHolder();
                holder.textView = (TextView)convertView.findViewById(R.id.list_item);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            //Log.i("1111111111",data.get(position));
            holder.textView.setText(data.get(position));
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MainMainActivity.this,MainActivity.class);
                    String url="http://manhua.fzdm.com/"+linkData.get(position);
                    intent.putExtra("url",url);


//                    SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
//                    editor.putString("lasturl", url);
//                    editor.putString("lasttext",data.get(position));
//                    editor.commit();

                    startActivity(intent);
                }
            });
            return convertView;
        }
    }
    public static class ViewHolder {
        public TextView textView;
    }
}
