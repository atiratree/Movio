package cz.muni.fi.pv256.movio2.fk410022.rx;

import android.database.Cursor;

import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.query.From;
import com.activeandroid.rxschedulers.AndroidSchedulers;
import com.activeandroid.sqlbrite.SqlBrite;
import com.activeandroid.util.SQLiteUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RxDbHelper {

    public static <T extends Model> rx.Observable<List<T>> execute(Class<T> resultClass, From select) {
        return execute(resultClass, Collections.singletonList(Cache.getTableName(resultClass)), select);
    }

    public static <T extends Model> rx.Observable<List<T>> execute(Class<T> resultClass, Collection<String> observeTables, From select) {
        return Cache.openDatabase().createQuery(observeTables, select.toSql(), select.getArguments())
                .subscribeOn(Schedulers.io())
                .map(new Func1<SqlBrite.Query, List<T>>() {
                    @Override
                    public List<T> call(SqlBrite.Query query) {
                        try {
                            Cursor cursor = query.run();
                            return SQLiteUtils.processCursor(resultClass, cursor);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T extends Model> rx.Observable<T> executeSingle(Class<T> resultClass, From select) {
        return executeSingle(resultClass, Collections.singletonList(Cache.getTableName(resultClass)), select);
    }

    public static <T extends Model> rx.Observable<T> executeSingle(Class<T> resultClass, Collection<String> observeTables, From select) {
        return Cache.openDatabase().createQuery(observeTables, select.toSql(), select.getArguments())
                .subscribeOn(Schedulers.io())
                .map(query -> {
                    try {
                        Cursor cursor = query.run();
                        if (cursor != null && cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            Model model = resultClass.newInstance();
                            model.loadFromCursor(cursor);
                            return (T) model;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    return null;
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T extends Model> Observable<Integer> count(Class<T> resultClass, From select) {
        return count(resultClass, Collections.singletonList(Cache.getTableName(resultClass)), select);
    }

    /**
     * Gets the number of rows returned by the query.
     */
    public static <T extends Model> Observable<Integer> count(Class<T> resultClass, Collection<String> observeTables, From select) {
        return Cache.openDatabase().createQuery(observeTables, select.toSql(), select.getArguments())
                .subscribeOn(Schedulers.io())
                .map(query -> {
                    Cursor cursor = query.run();
                    if (cursor.moveToFirst()) {
                        return cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(0)));
                    }
                    return 0;
                })
                .observeOn(AndroidSchedulers.mainThread());
    }
}
