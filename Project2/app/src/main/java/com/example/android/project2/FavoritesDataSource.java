package com.example.android.project2;

import android.content.Context;
import android.provider.BaseColumns;

public class FavoritesDataSource {

    //Metainformación de la base de datos
    public static final String FAVORITES_TABLE_NAME = "Favorites";
    public static final String STRING_TYPE = "text";
    public static final String INT_TYPE = "integer";
    public static final String LONG_TYPE = "long";
    public static final String DOUBLE_TYPE = "double";

    //Campos de la tabla Quotes
    public static class ColumnFavorites{
        public static final String ID_FAVORITES = BaseColumns._ID;
        public static final String ID_MOVIES = "movies";
        public static final String TITLE = "title";
        public static final String ORIGINAL_TITLE = "original_title";
        public static final String POSTER = "poster";
        public static final String OVERVIEW = "overview";
        public static final String RELEASE_DATE = "release_date";
        public static final String VOTE_AVERAGE = "vote_average";
    }

    //Script de Creación de la tabla Quotes
    public static final String CREATE_SCRIPT =
            "create table "+FAVORITES_TABLE_NAME+"(" +
                    ColumnFavorites.ID_FAVORITES+" "+INT_TYPE+" primary key autoincrement," +
                    ColumnFavorites.ID_MOVIES+" "+LONG_TYPE+" not null," +
                    ColumnFavorites.TITLE+" "+STRING_TYPE+" not null," +
                    ColumnFavorites.ORIGINAL_TITLE+" "+STRING_TYPE+" not null," +
                    ColumnFavorites.POSTER+" "+STRING_TYPE+" not null," +
                    ColumnFavorites.OVERVIEW+" "+STRING_TYPE+" not null," +
                    ColumnFavorites.RELEASE_DATE+" "+STRING_TYPE+" not null," +
                    ColumnFavorites.VOTE_AVERAGE+" "+DOUBLE_TYPE+" not null)";

    //Scripts de inserción por defecto
/*    public static final String INSERT_SCRIPT =
            "insert into "+FAVORITES_TABLE_NAME+" values" +
                    "(null,"+
                    "293660," +
                    "\"TITLE\"," +
                    "\"ORIGINAL_TITLE\"," +
                    "\"http://image.tmdb.org/t/p/w185/inVq3FRqcYIRl2la8iZikYYxFNR.jpg\","+
                    "\"OVERVIEW\"," +
                    "\"RELEASE_DATA\"," +
                    "3.3" +
                    ")";
//                    "(null, 246655," + "\"http://image.tmdb.org/t/p/w185/zSouWWrySXshPCT4t3UKCQGayyo.jpg\")";
*/
    private FavoritesDB openHelper;

    public FavoritesDataSource(Context context) {
        //Creando una instancia hacia la base de datos
        openHelper = new FavoritesDB(context);
    }


}