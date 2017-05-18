package com.example.ganger.comic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

import javax.net.ssl.HandshakeCompletedEvent;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> jpgList=new ArrayList<String>();
    private  List<String> data=new ArrayList<String>();
    private List<String> linkData=new ArrayList<String>();
    private  ListView listView;
    private TextView txv;
    private SharedPreferences sharedPreferences;
    private String intentUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Picasso.with(this).load("http://s1.nb-pintai.com/2014/10/142054441.jpg").into(imv);

        Intent i=getIntent();
        intentUrl=i.getStringExtra("url");
        Log.i("intentUrl_____:",intentUrl);

        sharedPreferences = getSharedPreferences("wujay", Context.MODE_PRIVATE);
        listView= (ListView) findViewById(R.id.listView);
        initData();


//        Button loadBtn= (Button) findViewById(R.id.button);
//        loadBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        for(int i=1;;i++)
//                        {
//                            try {
//                                //Log.i("22222222222","2222222222222222");
//                                Document doc = Jsoup.connect(String.format("http://manhua.fzdm.com/39/62/index_%d.html",i)).timeout(1000).get();
//                                Elements links = doc.select("img[src$=.jpg]"); //带有href属性的a元素
//
//                                String linkText = links.first().attr("src");
//
//                                if (links != null) {
//                                    jpgList.add(linkText);
//                                    Log.i("11111111111", String.valueOf(jpgList.size()));
//                                } else
//                                    Log.i("nainainai", "emememeemme");
////                    for (Element link : links) {
////                        String linkText = link.attr("src");
////                        Log.i("11111111111",linkText);
////                    }
//
//                            } catch (IOException e) {
//
//                                break;
//                                //e.printStackTrace();
//                                //Log.i("eeeeeeeee","333333333333");
//                            }finally {
//                                //Toast.makeText(MainActivity.this, "Load OK", Toast.LENGTH_SHORT).show();
//
//                            }
//                            Intent intent = new Intent(MainActivity.this,ImageBrowseActivity.class);
//                            intent.putStringArrayListExtra("images",jpgList);
//                            //intent.putExtra("position",position);
//                            startActivity(intent);
//                        }
//
//
//                    }
//                }).start();
//
//            }
//        });
//        Button startBtn= (Button) findViewById(R.id.button2);
//        startBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //initLatText();

    }

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            listView.setAdapter(new mAdp(data));
            return false;
        }
    });

//    public void initLatText(){
//        txv= (TextView) findViewById(R.id.last_text);
//        if(sharedPreferences.contains("lasturl")){
//            txv.setText("上回看到:"+sharedPreferences.getString("lasttext","没有"));
//            txv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent=new Intent(MainActivity.this,ImageBrowseActivity2.class);
//                    String url=sharedPreferences.getString("lasturl","");
//                    intent.putExtra("url",url);
//                    startActivity(intent);
//                }
//            });
//        }
//        else {
//            txv.setText("欢迎");
//        }
//    }

    public void initData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    Log.i("22222222222","2222222222222222");
                    Document doc = Jsoup.connect(intentUrl).timeout(10000).get();
                    Elements li=doc.select("li.pure-u-1-2.pure-u-lg-1-4");
                    Elements links = li.select("a[href]"); //带有href属性的a元素

                    if (links != null) {
                        //jpgList.add(linkText);
                        Log.i("11111111111", String.valueOf(li.size()));
                    } else
                        Log.i("nainainai", "emememeemme");
                    for (Element link : links) {
                        String linkText = link.text();
                        String att=link.attr("href");
                        data.add(linkText);
                        linkData.add(att);
                        //Log.i(linkText,att);
                        //Log.i(linkText,att);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                handler.sendMessage(msg);

            }
        }).start();
    }

    class mAdp extends BaseAdapter {
        List<String> data;
        LayoutInflater mInflater;
        public mAdp(List list){
            data=list;
            mInflater=LayoutInflater.from(MainActivity.this);
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
                    Intent intent=new Intent(MainActivity.this,ImageBrowseActivity2.class);
                    String url=intentUrl+linkData.get(position);
                    intent.putExtra("url",url);


                    SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                    editor.putString("lasturl", url);
                    editor.putString("lasttext",data.get(position));
                    editor.commit();

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
