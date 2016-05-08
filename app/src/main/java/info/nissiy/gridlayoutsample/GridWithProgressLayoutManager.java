package info.nissiy.gridlayoutsample;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

public class GridWithProgressLayoutManager extends GridLayoutManager {

    public GridWithProgressLayoutManager(Context context,
                                         final int spanCount,
                                         final RecyclerBaseAdapter adapter) {
        super(context, spanCount);
        //progressbar の位置セットアップ
        setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter != null
                        && adapter.getItemViewType(position) == RecyclerBaseAdapter.TYPE_PROG) {
                    System.out.println("GriddWithProgressLayoutManger:GetSpanSize:"+adapter.getItemViewType(position));
                    System.out.println("Rerutn:"+spanCount);
                    return spanCount;
                }
                return 1;
            }
            @Override
            public int getSpanIndex(int position, int spanCount) {
                if (adapter != null
                        && adapter.getItemViewType(position) == RecyclerBaseAdapter.TYPE_PROG) {
                    return 0;
                }
                return position % spanCount;
            }
        });
    }

}
