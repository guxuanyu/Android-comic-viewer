package com.example.ganger.comic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ganger on 2017/5/1.
 */
public class ImageBrowseActivity2 extends Activity implements ViewPager.OnPageChangeListener,View.OnClickListener,ImageBrowseView{

    private ViewPager vp;
    private TextView hint;
    private TextView save;
    private ViewPageAdapter adapter;
    private ImageBrowsePresenter presenter;
    private ProgressBar progressBar;
    private ArrayList<String> jpgList=new ArrayList<String>();
    private String url;//http://manhua.fzdm.com/39/62/
    private Thread th1;
    private boolean th1isRun=true;
    private int position;
    public ImageBrowseActivity2() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_image_browse);
        vp = (ViewPager) this.findViewById(R.id.viewPager);
        hint = (TextView) this.findViewById(R.id.hint);
        save = (TextView) this.findViewById(R.id.save);
        progressBar= (ProgressBar) findViewById(R.id.progressBar);
        Log.i("progressssss",progressBar.toString());
        //save.setOnClickListener(this);
        initPresenter();
        presenter.loadImage(jpgList);
        //setImageBrowse(jpgList);
        Log.i("jwjwjwjwjw","jwjwjwjwjw");
        Intent intent=getDataIntent();
        url=intent.getStringExtra("url");
        Log.i("url:______",url);

        th1=new Thread(new Runnable() {
            @Override
            public void run() {

                Looper.prepare();
                try {
                    Document doc = Jsoup.connect(url).timeout(5000).get();
                    Elements links = doc.select("img[src$=.jpg]"); //带有href属性的a元素

                    final String  linkText = links.first().attr("src");

                    if (links != null&&!jpgList.contains(linkText)) {

                        Message msg = new Message();
                           handler.sendMessage(msg);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                jpgList.add(linkText);
                                adapter.notifyDataSetChanged();
                            }
                        });


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for(int i=1;th1isRun;i++)
                {


                    try {
                        //Log.i("22222222222","2222222222222222");
                        Document doc = Jsoup.connect(String.format(url+"index_%d.html",i)).timeout(5000).get();
                        Elements links = doc.select("img[src$=.jpg]"); //带有href属性的a元素

                        final String linkText = links.first().attr("src");

                        if (links != null&&!jpgList.contains(linkText)) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    jpgList.add(linkText);
                                    adapter.notifyDataSetChanged();
                                }
                            });

//                            Message msg = new Message();
//                            handler.sendMessage(msg);
                            Log.i("11111111111", String.valueOf(jpgList.size()));
                        } else
                            Log.i("nainainai", "emememeemme");
//                    for (Element link : links) {
//                        String linkText = link.attr("src");
//                        Log.i("11111111111",linkText);
//                    }

                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                        //e.printStackTrace();
                        //Log.i("eeeeeeeee","333333333333");
                    }finally {
                        //Toast.makeText(MainActivity.this, "Load OK", Toast.LENGTH_SHORT).show();

                    }
                    if (th1isRun) {

                    }
                }

            }
        });
        th1.start();
    }

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            progressBar.setVisibility(View.GONE);

            return false;
        }
    });

    @Override
    protected void onDestroy() {
        super.onDestroy();
        th1isRun=false;
    }

    public void initPresenter(){
        presenter = new ImageBrowsePresenter(this);
    }

    @Override
    public Intent getDataIntent() {
        return getIntent();
    }

    @Override
    public Context getMyContext() {
        return this;
    }

    @Override
    public void setImageBrowse(List<String> images) {
        if(adapter == null /*&& images != null && images.size() != 0*/){
            adapter = new ViewPageAdapter(this,images);
            Log.i("jzjzjzjz","jzjzjzjzjz");
            vp.setAdapter(adapter);
            //vp.setCurrentItem(position);
            vp.addOnPageChangeListener(this);
            //hint.setText(position + 1 + "/" + images.size());
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        presenter.setPosition(position);
        hint.setText(position + 1 + "/" + presenter.getImages().size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        //presenter.saveImage();
    }


    public static void startActivity(Context context, ArrayList<String> images, int position){
        Intent intent = new Intent(context,ImageBrowseActivity.class);
        intent.putStringArrayListExtra("images",images);
        intent.putExtra("position",position);
        context.startActivity(intent);
    }

    public String getPositionImage(){
        return jpgList.get(position);
    }

    public List<String> getImages() {
        return jpgList;
    }

    public void setPosition(int position) {
        this.position = position;
    }


}