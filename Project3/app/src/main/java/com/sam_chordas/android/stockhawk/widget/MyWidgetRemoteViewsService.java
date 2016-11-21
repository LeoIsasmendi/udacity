package com.sam_chordas.android.stockhawk.widget;

import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteDatabase;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;


public class MyWidgetRemoteViewsService extends RemoteViewsService {

    private static final String[] STOCK_COLUMNS = {
            QuoteDatabase.QUOTES + "." + QuoteColumns._ID,
            QuoteColumns.SYMBOL,
            QuoteColumns.BIDPRICE,
            QuoteColumns.PERCENT_CHANGE,
            QuoteColumns.ISUP,
            QuoteColumns.CHANGE
    };

    static final int INDEX_STOCK_ID = 0;


    @Override
    public RemoteViewsFactory onGetViewFactory(final Intent intent){
        return new RemoteViewsFactory() {
            private Cursor data = null;


            @Override
            public void onCreate() {
            }

            @Override
            public void onDataSetChanged() {
                if (data!=null){
                    data.close();
                }
                final long identityToken = Binder.clearCallingIdentity();
                data = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                        STOCK_COLUMNS,
                        null,
                        null,
                        null);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data!=null){
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {

                if(position == AdapterView.INVALID_POSITION || data == null || !data.moveToPosition(position)){
                    return null;
                }

                String symbol = data.getString(data.getColumnIndex(QuoteColumns.SYMBOL));
                String bidPrice = data.getString(data.getColumnIndex(QuoteColumns.BIDPRICE));
                String change = data.getString(data.getColumnIndex(QuoteColumns.CHANGE));

                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widget_item_quote);
                remoteViews.setTextViewText(R.id.stock_symbol, symbol);
                remoteViews.setTextViewText(R.id.bid_price, bidPrice);
                remoteViews.setTextViewText(R.id.change, change);

                int isUp = data.getInt(data.getColumnIndex(QuoteColumns.ISUP));
                if(isUp == 0){
                    remoteViews.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_red);
                } else {
                    remoteViews.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_green);
                }

                Intent graphActivityIntent = new Intent();
                graphActivityIntent.setData(QuoteProvider.Quotes.withSymbol(symbol));
                remoteViews.setOnClickFillInIntent(R.id.stock_widget_list, graphActivityIntent);
                return remoteViews;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_item_quote);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position)) {
                    return data.getInt(INDEX_STOCK_ID);
                }
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }

}
