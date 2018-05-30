package ttc.project.stoku.room;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

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

public class StokuRepository {
    private ItemDao mItemDao;
    private CategoryDao mCategoryDao;
    private ItemCategoryJoinDao mItemCategoryJoinDao;
    private ListDao mListItemDao;
    private ListCategoryJoinDao mListCategoryJoinDao;
    private ReportDao mReportDao;
    private ReportItemDao mReportItemDao;
    private ReportItemCategoryJoinDao mReportItemCategoryJoinDao;

    private LiveData<List<UserItemCategoryJoinEntity>> mAllItemsLiveData;
    private List<UserItemCategoryJoinEntity> mAllItems;

    private LiveData<List<CategoryEntity>> mAllCategoriesLiveData;
    private List<CategoryEntity> mAllCategories;

    StokuRepository(Application application){
        StokuDatabase db = StokuDatabase.getDatabase(application);
        mItemDao = db.itemDao();
        mCategoryDao = db.categoryDao();
        mItemCategoryJoinDao = db.itemCategoryJoinDao();
        mListItemDao = db.listDao();
        mReportDao = db.reportDao();
        mReportItemDao = db.reportItemDao();
        mReportItemCategoryJoinDao = db.reportItemCategoryJoinDao();
//        mAllItems = mItemCategoryJoinDao.getAllUserItems();
        mAllCategoriesLiveData = mCategoryDao.getAllCategoriesLiveData();
        mListCategoryJoinDao = db.listCategoryJoinDao();

    }

    public LiveData<List<UserItemCategoryJoinEntity>> getAllItemsLiveData(){
        return mAllItemsLiveData;
    }
    public List<CategoryEntity> getAllCategories(){
        return mCategoryDao.getAllCategories();
    }
    public List<UserItemCategoryJoinEntity> getAllItems(){
        return mItemCategoryJoinDao.getAllUserItems();
    }
    public List<ListCategoryJoinEntity> getAllListItems(){
        return mListCategoryJoinDao.getAllListItems();
    }
    public LiveData<Boolean> getItemStatus(long itemId){
        return mItemDao.getStatus(itemId);
    }

    public LiveData<List<CategoryEntity>> getmAllCategoriesLiveData(){
        return mAllCategoriesLiveData;
    }

    public void insertItem(UserItemEntity itemEntity){
        new insertAsyncTask(mItemDao).execute(itemEntity);
    }
    private static class insertAsyncTask extends AsyncTask<UserItemEntity, Void, Void>{
        private ItemDao mAsyncTaskDao;

        insertAsyncTask(ItemDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(UserItemEntity... userItemEntities) {
            mAsyncTaskDao.insertItem(userItemEntities[0]);
            return null;
        }
    }

    public void insertListItems(ListEntity... itemEntities){
        new insertListItemsAsyncTask(mListItemDao).execute(itemEntities);
    }
    private static class insertListItemsAsyncTask extends AsyncTask<ListEntity, Void, Void>{
        private ListDao mAsyncTaskDao;

        insertListItemsAsyncTask(ListDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(ListEntity... itemEntity) {
            mAsyncTaskDao.insertListItems(itemEntity);
            return null;
        }
    }

    public void insertListItem(ListEntity itemEntity){
        new insertListItemAsyncTask(mListItemDao).execute(itemEntity);
    }
    private static class insertListItemAsyncTask extends AsyncTask<ListEntity, Void, Void>{
        private ListDao mAsyncTaskDao;

        insertListItemAsyncTask(ListDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(ListEntity... itemEntity) {
            mAsyncTaskDao.insertListItem(itemEntity[0]);
            return null;
        }
    }

    public void updateListItem(ListEntity itemEntity){
        new insertListItemAsyncTask(mListItemDao).execute(itemEntity);
    }
    private static class updateListItemAsyncTask extends AsyncTask<ListEntity, Void, Void>{
        private ListDao mAsyncTaskDao;

        updateListItemAsyncTask(ListDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(ListEntity... itemEntity) {
            mAsyncTaskDao.updateListItem(itemEntity[0]);
            return null;
        }
    }

    public void deleteListItem(ListEntity itemEntity){
        new deleteListItemAsyncTask(mListItemDao).execute(itemEntity);
    }
    private static class deleteListItemAsyncTask extends AsyncTask<ListEntity, Void, Void>{
        private ListDao mAsyncTaskDao;

        deleteListItemAsyncTask(ListDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(ListEntity... itemEntity) {
            mAsyncTaskDao.deleteListItem(itemEntity[0]);
            return null;
        }
    }

    public void insertCategory(CategoryEntity categoryEntity){
        new insertCategoryAsyncTask(mCategoryDao).execute(categoryEntity);
    }

    private static class insertCategoryAsyncTask extends AsyncTask<CategoryEntity, Void, Void>{
        private CategoryDao mAsyncTaskDao;

        insertCategoryAsyncTask(CategoryDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(CategoryEntity... categoryEntities) {
            mAsyncTaskDao.insertCategory(categoryEntities[0]);
            return null;
        }
    }

    public void deleteCategory(CategoryEntity categoryEntity){
        new deleteCategoryAsyncTask(mCategoryDao).execute(categoryEntity);
    }

    private static class deleteCategoryAsyncTask extends AsyncTask<CategoryEntity, Void, Void>{
        private CategoryDao mAsyncTaskDao;

        deleteCategoryAsyncTask(CategoryDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(CategoryEntity... categoryEntities) {
            mAsyncTaskDao.deleteCategory(categoryEntities[0]);
            return null;
        }
    }

    public void updateCategory(CategoryEntity categoryEntity){
        new updateCategoryAsyncTask(mCategoryDao).execute(categoryEntity);
    }

    private static class updateCategoryAsyncTask extends AsyncTask<CategoryEntity, Void, Void>{
        private CategoryDao mAsyncTaskDao;

        updateCategoryAsyncTask(CategoryDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(CategoryEntity... categoryEntities) {
            mAsyncTaskDao.updateCategory(categoryEntities[0]);
            return null;
        }
    }

    public void updateItem(UserItemEntity userItemEntity){
        new updateItemAsyncTask(mItemDao).execute(userItemEntity);
    }

    private static class updateItemAsyncTask extends AsyncTask<UserItemEntity, Void, Void>{
        private ItemDao mAsyncTaskDao;

        updateItemAsyncTask(ItemDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(UserItemEntity... userItemEntities) {
            mAsyncTaskDao.updateItem(userItemEntities[0]);
            return null;
        }
    }

    public void updateItems(UserItemEntity... userItemEntity){
        new updateItemsAsyncTask(mItemDao).execute(userItemEntity);
    }

    private static class updateItemsAsyncTask extends AsyncTask<UserItemEntity, Void, Void>{
        private ItemDao mAsyncTaskDao;

        updateItemsAsyncTask(ItemDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(UserItemEntity... userItemEntities) {
            mAsyncTaskDao.updateItems(userItemEntities);
            return null;
        }
    }

    public void deleteItem(UserItemEntity userItemEntity){
        new deleteItemAsyncTask(mItemDao).execute(userItemEntity);
    }

    private static class deleteItemAsyncTask extends AsyncTask<UserItemEntity, Void, Void>{
        private ItemDao mAsyncTaskDao;

        deleteItemAsyncTask(ItemDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(UserItemEntity... userItemEntities) {
            mAsyncTaskDao.deleteItem(userItemEntities[0]);
            return null;
        }
    }

    public long insertReport(ReportEntity reportEntity){
        return mReportDao.insertReport(reportEntity);
    }

    public void insertReportItems(ReportItemEntity... reportItemEntities){
        new insertReportItemAsyncTask(mReportItemDao).execute(reportItemEntities);
    }
    private static class insertReportItemAsyncTask extends AsyncTask<ReportItemEntity, Void, Void>{
        private ReportItemDao mAsyncTaskDao;

        insertReportItemAsyncTask(ReportItemDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(ReportItemEntity... reportEntity) {
            mAsyncTaskDao.insertReportItems(reportEntity);
            return null;
        }
    }

    public LiveData<List<ReportItemCategoryJoinEntity>> getAllReportItemsData(long reportId){
        return mReportItemCategoryJoinDao.getAllItemReportLiveData(reportId);
    }

    public void deleteAllListData(){
        new deleteAllListDataAsyncTask(mListItemDao).execute();
    }
    private static class deleteAllListDataAsyncTask extends AsyncTask<Void, Void, Void>{
        private ListDao mAsyncTaskDao;

        deleteAllListDataAsyncTask(ListDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAllListData();
            return null;
        }

    }

    public LiveData<List<ReportEntity>> getAllReports(){
        return mReportDao.getAllReports();
    }

    public long[] insertCategoryWithReturn(CategoryEntity... categoryEntity){
        return mCategoryDao.insertCategoryWithReturn(categoryEntity);
    }

    public long[] insertItemWithReturn(UserItemEntity... userItemEntity){
        return mItemDao.insertItemWithReturn(userItemEntity);
    }

    public void deleteReport(ReportEntity reportEntity){
        new deleteReportAsyncTask(mReportDao).execute(reportEntity);
    }

    private static class deleteReportAsyncTask extends AsyncTask<ReportEntity, Void, Void>{
        private ReportDao mAsyncTaskDao;

        deleteReportAsyncTask(ReportDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(ReportEntity... reportEntities) {
            mAsyncTaskDao.deleteReport(reportEntities[0]);
            return null;
        }
    }
}
