package com.szhua.sharemsgtocircle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by Szhua on 2016/10/25.
 * todo the oritation of the screen ;
 */
public class Adapter extends RecyclerView.Adapter {
  private   SelectedChangeListener selectedChangeListener ;
    private Context  context ;
    /**
     * Save the selected postion
     */
   private  ArrayList<Integer> integers = new ArrayList<Integer>();
    /**
     * the datas ;
     */
    private ArrayList<Item> itemArrayLis = new ArrayList<>();
   private  ArrayList<Item> selectedDatas =new ArrayList<Item>() ;

    private boolean isAllSelected ;
    public  Adapter(Context context){
        this.context =context ;
    }
     interface SelectedChangeListener {
        void selectedNumbers(int counts);
        void selectedDatas(ArrayList<Item> items) ;
    }
    /**
     * the listener to callback for the currentSelected numbers;
     * @param selectedChangeListener
     */
    public void setSelectedChangeListener(SelectedChangeListener selectedChangeListener) {
        this.selectedChangeListener = selectedChangeListener;
    }
    //default numbers maxed;
    private static final int DEFAULT_MAX_NUMBERS =9 ;

    private static int MAX_NUMBERS=DEFAULT_MAX_NUMBERS;

    public static void setMaxNumbers(int maxNumbers) {
        MAX_NUMBERS = maxNumbers;
    }
    public void setItemArrayLis(ArrayList<Item> itemArrayLis) {
        this.itemArrayLis = itemArrayLis;
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false) ;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, final int position) {

        ViewHolder holder = (ViewHolder) h;
        if(isAllSelected){
            holder.itemView.setBackgroundResource(R.color.colorAccent);
        }else{
            holder.itemView.setBackgroundResource(R.color.colorPrimary);
        }
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        holder.itemView.setLayoutParams(new ViewGroup.LayoutParams(width/2,width/2));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(MAX_NUMBERS<=integers.size()){
                                    if(itemArrayLis.get(position).isSelected()){
                                        itemArrayLis.get(position).setSelected(false);
                                        int pos =integers.indexOf(position) ;
                                        integers.remove(pos) ;
                                        selectedDatas.remove(itemArrayLis.get(position)) ;
                                    }else{
                                        Toast.makeText(context,"最多选择："+MAX_NUMBERS+"张图片",Toast.LENGTH_SHORT).show();
                                    }
                    }else{
                                    if(itemArrayLis.get(position).isSelected()){
                                        itemArrayLis.get(position).setSelected(false);
                                        int pos =integers.indexOf(position) ;
                                        integers.remove(pos) ;
                                        selectedDatas.remove(itemArrayLis.get(position)) ;
                                    }else{
                                        itemArrayLis.get(position).setSelected(true);
                                        integers.add(position);
                                        selectedDatas.add(itemArrayLis.get(position));
                                    }
                    }
                    selectedChangeListener.selectedNumbers(integers.size());
                    selectedChangeListener.selectedDatas(selectedDatas);
                    notifyItemChanged(position);
                }
            });
        if(itemArrayLis.get(position).isSelected()){
            holder.iv.setVisibility(View.VISIBLE);
        }else{
            holder.iv.setVisibility(View.GONE);
        }
        if(itemArrayLis.get(position).getImgRes()!=0)
        holder.selectedTag.setImageResource(itemArrayLis.get(position).getImgRes());

    }

    @Override
    public int getItemCount() {
        return itemArrayLis==null?0:itemArrayLis.size();
    }

   class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.selected_tag)
        ImageView selectedTag;
        @InjectView(R.id.iv)
        ImageView iv;
        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    isAllSelected =true ;
                    notifyDataSetChanged();
                    return true;
                }
            });
        }
    }
}
