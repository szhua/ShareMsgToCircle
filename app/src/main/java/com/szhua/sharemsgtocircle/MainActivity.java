package com.szhua.sharemsgtocircle;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DialogTitle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Notification;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * To test the RecycleView's ItemDecoration : the define mine itemdecoration ;
 * To test to share the text and pictures to WeChatCircle at the same time ;
 * Created by Szhua ;
 */

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.recycleview)
    RecyclerView recycleview;
    @InjectView(R.id.floataction)
    FloatingActionButton floataction;
    private ArrayList<Item> itemArrayList =new ArrayList<Item>() ;
    private ArrayList<Item> selectedItems =new ArrayList<>() ;
    private ArrayList<File> files =new ArrayList<File>();
    private Subscription subscription ;
    String shareContents ="我说分享一段文字再加上图片你怎么就不信呢，还有九张图片的限制，限制个毛线啊，我说的就是这个问题，妈的，实在说不下去了，就这样吧！";
    private String  pkgName;
    private String  className;
    private int  IMAGE_NAME;
    private ProgressDialog  pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);



        for (int i = 0; i <10 ; i++) {
            Item item =new Item()  ;
            switch (i){
                case 0:
                    item.setImgRes(R.drawable.share_1);
                        break ;
                case 1:
                    item.setImgRes(R.drawable.share_2);
                    break ;
                case 2:
                    item.setImgRes(R.drawable.share_3);
                    break ;
                case 3:
                    item.setImgRes(R.drawable.share_1);
                    break ;
                case 4:
                    item.setImgRes(R.drawable.share_2);
                    break ;
                case 5:
                    item.setImgRes(R.drawable.share_3);
                    break ;
                case 6:
                    item.setImgRes(R.drawable.share_1);
                    break ;
                case 7:
                    item.setImgRes(R.drawable.share_3);
                    break ;
                case 8:
                    item.setImgRes(R.drawable.share_2);
                    break ;
                case 9:
                    item.setImgRes(R.drawable.share_1);
                    break ;

            }
            itemArrayList.add(item) ;
        }
        Adapter adapter =new Adapter(this) ;
        adapter.setItemArrayLis(itemArrayList);
        recycleview.setHasFixedSize(true);
        recycleview.addItemDecoration(new ColorDivider(this.getResources(),R.color.colorPrimaryDark,1, LinearLayoutManager.VERTICAL));
        recycleview.setLayoutManager(new GridLayoutManager(this,2));
        recycleview.setAdapter(adapter);
        adapter.setSelectedChangeListener(new Adapter.SelectedChangeListener() {
            @Override
            public void selectedNumbers(int counts) {

            }
            @Override
            public void selectedDatas(ArrayList<Item> items) {
               selectedItems.clear();
               selectedItems.addAll(items) ;
            }
        });


        Intent intent = new Intent(Intent.ACTION_SEND, null);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("image/*");
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> mApps = packageManager.queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        for (ResolveInfo info : mApps){
            if ("com.tencent.mm.ui.tools.ShareToTimeLineUI".equals(info.activityInfo.name)) {
                pkgName = info.activityInfo.packageName;
                className = info.activityInfo.name;
            }
        }

        floataction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedItems.size()==0){
                    Toast.makeText(MainActivity.this,"Seleted 0" ,Toast.LENGTH_SHORT).show(); return; }
                    pro=ProgressDialog.show(MainActivity.this,"正在处理图片","正在处理图片");
                    saveImageToSdCard(selectedItems);
            }
        });

    }

    /**
     *
     * @param images
     */
    public void saveImageToSdCard(ArrayList<Item> images ) {
        IMAGE_NAME =0 ;
        files.clear();
        Observable.from(images)
                 .doOnEach(new Action1<Notification<? super Item>>() {
                     @Override
                     public void call(Notification<? super Item> notification) {
                         Log.i("Leilei","fjksdf") ;
                     }
                 })
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Item, File>() {
            @Override
            public File call(Item item) {
                return getFile(item.getImgRes());
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<File>() {
            @Override
            public void onCompleted() {
                pro.cancel();
                Intent intent = new Intent();
                ComponentName comp = new ComponentName("com.tencent.mm",
                        "com.tencent.mm.ui.tools.ShareToTimeLineUI");
                intent.setComponent(comp);
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.setType("image/*");
                intent.putExtra("Kdescription", shareContents);
                ArrayList<Uri> imageUris = new ArrayList<Uri>();
                for (File f : files) {
                    imageUris.add(Uri.fromFile(f));
                }
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
                startActivity(intent);

            }
            @Override
            public void onError(Throwable e) {
                Log.i("Leilei",e.toString()) ;
                pro.cancel();
            }
            @Override
            public void onNext(File file) {
                files.add(file) ;
            }
        });
    }
    public  File createStableImageFile() throws IOException {
        IMAGE_NAME++;
        String imageFileName = Integer.toString(IMAGE_NAME) + ".jpg";
        File storageDir = getExternalCacheDir();
        File image = new File(storageDir, imageFileName);
        return image;
    }

    public  File getFile(int res){
        boolean success = false;
        File file = null;
        try {
            file = createStableImageFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        BitmapDrawable drawable = (BitmapDrawable)getResources().getDrawable(res);
        Bitmap bitmap = drawable.getBitmap();
        FileOutputStream outStream;
        try {
            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (success) {
            return file;
        } else {
            return null;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription!=null) {
            subscription.unsubscribe();
        }
    }



}
