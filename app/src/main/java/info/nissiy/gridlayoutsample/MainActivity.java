package info.nissiy.gridlayoutsample;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.BindDimen;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener, EndlessScrollListener.OnLoadMoreListener {
    private static Handler handler = new Handler();
    private Resources res;
    private PhotoGridAdapter adapter;
    private EndlessScrollListener scrollListener;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindDimen(R.dimen.unit_margin)
    int unitMargin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        res = getResources();

        //toolbar setup
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.app_name));
        }
        //
        int spanCount = 2;

        //landscape setting
        if (Util.isLandscape(getApplicationContext())) {
            spanCount = 3;
        }

        //
        //gridviewadapter setup
        //

        //1.画像のセットアップアダプターの作成
        adapter = new PhotoGridAdapter(this, new ArrayList<>(), spanCount);
        //2.プログレスバーのレイアウトマネージャーの作成,引数(activityとspancount,画像のアダプタ)を代入
        GridWithProgressLayoutManager layoutManager = new GridWithProgressLayoutManager(this, spanCount, adapter);
        //3.スクロールリスナを作成.
        scrollListener = new EndlessScrollListener(1, layoutManager);
        //4.スクロールリスナをon
        scrollListener.setOnLoadMoreListener(this);

        //
        //recyclerview setup
        //

        //1.リサイクラーレイアウトマネージャーに、プログレスバーのセットアダプターを反映
        recyclerView.setLayoutManager(layoutManager);
        //2.リサイクラーviewにスクロールリスナを反映
        recyclerView.addOnScrollListener(scrollListener);
        //3.
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, unitMargin));
        //4.リサイクラーに、画像のセットアップアダプターの反映
        recyclerView.setAdapter(adapter);

        //swiperrefreshの設定
        swipeRefreshLayout.setColorSchemeResources(R.color.color_primary);
        swipeRefreshLayout.setOnRefreshListener(this);


        loadData(1);
    }

    @Override
    public void onLoadMore(int currentPage) {
        System.out.println("onLoadMorre:currentpade:currentPage" + currentPage);
        loadData(currentPage);
    }

    private void loadData(final int page) {
        // Set the Stub for display the ProgressBar
        final ProgressStub progressStub = new ProgressStub();
        if (page > 1) {
            adapter.add(progressStub);
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Remove the Stub
                if (page > 1) {
                    adapter.remove(progressStub);
                }

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                int startNum = 10 * (page - 1);
                int endNum = 10 * page - 1;

                //追加取得処理
                for (; startNum <= endNum; startNum++) {
                    int suffix = startNum % 6;
                    int drawableResId = res.getIdentifier("w" + suffix, "drawable", getPackageName());
                    adapter.add(new Photo(drawableResId));
                }
            }
        }, 1000);
    }

    @Override
    public void onRefresh() {
        adapter.clear();
        scrollListener.onRefresh();

        loadData(1);
    }

}
