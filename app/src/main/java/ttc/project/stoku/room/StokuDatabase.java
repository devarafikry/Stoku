package ttc.project.stoku.room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import ttc.project.stoku.room.dao.CategoryDao;
import ttc.project.stoku.room.dao.ItemCategoryJoinDao;
import ttc.project.stoku.room.dao.ItemDao;
import ttc.project.stoku.room.dao.ListCategoryJoinDao;
import ttc.project.stoku.room.dao.ListDao;
import ttc.project.stoku.room.dao.ReportDao;
import ttc.project.stoku.room.dao.ReportItemCategoryJoinDao;
import ttc.project.stoku.room.dao.ReportItemDao;
import ttc.project.stoku.room.entity.CategoryEntity;
import ttc.project.stoku.room.entity.ListCategoryJoinEntity;
import ttc.project.stoku.room.entity.ListEntity;
import ttc.project.stoku.room.entity.ReportEntity;
import ttc.project.stoku.room.entity.ReportItemCategoryJoinEntity;
import ttc.project.stoku.room.entity.ReportItemEntity;
import ttc.project.stoku.room.entity.UserItemCategoryJoinEntity;
import ttc.project.stoku.room.entity.UserItemEntity;

@Database(entities = {UserItemEntity.class,
        CategoryEntity.class,
        UserItemCategoryJoinEntity.class,
        ListEntity.class,
        ListCategoryJoinEntity.class,
        ReportEntity.class,
        ReportItemEntity.class,
        ReportItemCategoryJoinEntity.class}, version = 1, exportSchema = false)
public abstract class StokuDatabase extends RoomDatabase{
    private static StokuDatabase INSTANCE;

    public abstract ItemDao itemDao();
    public abstract CategoryDao categoryDao();
    public abstract ItemCategoryJoinDao itemCategoryJoinDao();
    public abstract ListDao listDao();
    public abstract ListCategoryJoinDao listCategoryJoinDao();
    public abstract ReportDao reportDao();
    public abstract ReportItemDao reportItemDao();
    public abstract ReportItemCategoryJoinDao reportItemCategoryJoinDao();

    public static StokuDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (StokuDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            StokuDatabase.class,
                            "stoku_database"
                    ).addCallback(sStokuDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    public static boolean isStokuDatabaseExist(){
        return INSTANCE==null;
    }

    private static RoomDatabase.Callback sStokuDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
//            new InitiateFirstCategory(INSTANCE).execute();

        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };

//    private static class InitiateFirstCategory extends AsyncTask<Void, Void, Void>{
//        private final CategoryDao categoryDao;
//
//        InitiateFirstCategory(StokuDatabase db){
//            categoryDao = db.categoryDao();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            CategoryEntity categoryEntity = new CategoryEntity(
//                    1, "Tidak Terkategori", -5592406
//            );
//
//            categoryDao.insertCategory(categoryEntity);
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
////            new InitiateFirstItem(INSTANCE).execute();
//        }
//    }

//    private static class InitiateFirstItem extends AsyncTask<Void, Void, Void>{
//        private final ItemDao itemDao;
//
//        InitiateFirstItem(StokuDatabase db){
//            itemDao = db.itemDao();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            return null;
//        }
//    }
}
