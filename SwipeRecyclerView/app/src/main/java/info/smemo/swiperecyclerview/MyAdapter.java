package info.smemo.swiperecyclerview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 */
public class MyAdapter extends RecyclerView.Adapter {

    private ArrayList<String> stringArrayList;
    private Context context;

    public MyAdapter(Context context) {
        this.context = context;
        stringArrayList = new ArrayList<>();
        stringArrayList.add("neo");
        stringArrayList.add("android");
        stringArrayList.add("ios");
        stringArrayList.add("blog");
        stringArrayList.add("app");
        stringArrayList.add("html");
        stringArrayList.add("python");
        stringArrayList.add("linux");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //获取背景菜单
        View mybg = LayoutInflater.from(parent.getContext()).inflate(R.layout.bg_menu, null);
        mybg.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

        //获取item布局
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        //生成返回RecyclerView.ViewHolder
        return new MyHolder(context, mybg, view, RecyclerViewDragHolder.EDGE_RIGHT).getDragViewHolder();
    }

    class MyHolder extends RecyclerViewDragHolder {

        private TextView title;
        private TextView deleteItem;
        private TextView openBlog;
        private TextView closeApp;

        public MyHolder(Context context, View bgView, View topView) {
            super(context, bgView, topView);
        }

        public MyHolder(Context context, View bgView, View topView, int mTrackingEdges) {
            super(context, bgView, topView, mTrackingEdges);
        }

        @Override
        public void initView(View itemView) {
            title = (TextView) itemView.findViewById(R.id.title);
            deleteItem = (TextView) itemView.findViewById(R.id.delete);
            openBlog = (TextView) itemView.findViewById(R.id.open);
            closeApp = (TextView) itemView.findViewById(R.id.closeMenu);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyHolder myHolder = (MyHolder) RecyclerViewDragHolder.getHolder(holder);
        String data = stringArrayList.get(position);
        myHolder.title.setText(data);
        myHolder.openBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://blog.smemo.info");
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(it);
            }
        });
        myHolder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringArrayList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
            }
        });
        myHolder.closeApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "关闭菜单", Toast.LENGTH_SHORT).show();
                myHolder.close();
            }
        });
    }


    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }


}

