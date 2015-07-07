# android-SwipeRecyclerView
android可滑动菜单RecyclerView

# Help
需要添加
compile 'com.android.support:support-v4:22.2.0'

compile 'com.android.support:recyclerview-v7:22.2.0'

使用非侵入式添加功能支持

step1:
```android
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
```

step2:
```android
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
```

step3:
```android
@Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyHolder myHolder = (MyHolder) RecyclerViewDragHolder.getHolder(holder);
        
    }
```

#Screen
![](https://github.com/szpnygo/android-SwipeRecyclerView/blob/master/demo.png)


#About Devloper

Neo (http://blog.smemo.info) 
