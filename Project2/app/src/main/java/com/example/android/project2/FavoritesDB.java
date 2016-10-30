package com.example.android.project2;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoritesDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Favorites.db";
    private static final String FAVORITES_TABLE_NAME = "Favorites";
    private static final int DATABASE_VERSION = 1;
    private static FavoritesDB sInstance;

    public FavoritesDB(Context context) {
        super(context,
                DATABASE_NAME,//String name
                null,//factory
                DATABASE_VERSION//int version
        );
    }

    public static synchronized FavoritesDB getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new FavoritesDB(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Crear la base de datos
        db.execSQL(FavoritesDataSource.CREATE_SCRIPT);
        //Insertar registros iniciales
        //db.execSQL(FavoritesDataSource.INSERT_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Actualizar la base de datos
    }

    public Cursor getFavorites() {

        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(
                FAVORITES_TABLE_NAME,  //Nombre de la tabla
                null,  //Lista de Columnas a consultar
                null,  //Columnas para la clausula WHERE
                null,  //Valores a comparar con las columnas del WHERE
                null,  //Agrupar con GROUP BY
                null,  //Condición HAVING para GROUP BY
                null  //Clausula ORDER BY
        );

    }

    public void insertToFavorites(MovieDataParcelable movie) {

        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FavoritesDataSource.ColumnFavorites.ID_MOVIES, movie.getId());
        values.put(FavoritesDataSource.ColumnFavorites.TITLE, movie.getTitle());
        values.put(FavoritesDataSource.ColumnFavorites.ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(FavoritesDataSource.ColumnFavorites.POSTER, movie.getPoster());
        values.put(FavoritesDataSource.ColumnFavorites.OVERVIEW, movie.getOverview());
        values.put(FavoritesDataSource.ColumnFavorites.RELEASE_DATE, movie.getReleaseDate());
        values.put(FavoritesDataSource.ColumnFavorites.VOTE_AVERAGE, movie.getVoteAverage());


        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                FAVORITES_TABLE_NAME,
                null,
                values);

    }

    public boolean removeFromFavorites(long movieId) {

        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(FAVORITES_TABLE_NAME, FavoritesDataSource.ColumnFavorites.ID_MOVIES + "=" + movieId, null) > 0;

    }

    public Boolean checkMovie(long movieId) {

        SQLiteDatabase db = this.getReadableDatabase();
        String mQuery = "SELECT * " +
                " FROM " + FAVORITES_TABLE_NAME +
                " WHERE " + FavoritesDataSource.ColumnFavorites.ID_MOVIES + " = ?";

        Cursor mCursor = db.rawQuery(mQuery, new String[]{Long.toString(movieId)});
        return mCursor.getCount() != 0;

    }
}