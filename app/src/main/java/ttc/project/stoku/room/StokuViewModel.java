package ttc.project.stoku.room;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import ttc.project.stoku.room.dao.ItemCategoryJoinDao;
import ttc.project.stoku.room.entity.CategoryEntity;
import ttc.project.stoku.room.entity.ListCategoryJoinEntity;
import ttc.project.stoku.room.entity.ListEntity;
import ttc.project.stoku.room.entity.ReportEntity;
import ttc.project.stoku.room.entity.ReportItemCategoryJoinEntity;
import ttc.project.stoku.room.entity.ReportItemEntity;
import ttc.project.stoku.room.entity.UserItemCategoryJoinEntity;
import ttc.project.stoku.room.entity.UserItemEntity;

public class StokuViewModel extends AndroidViewModel {
    private StokuRepository mStokuRepository;
    private LiveData<List<UserItemCategoryJoinEntity>> mAllItemsLiveData;
    private List<UserItemCategoryJoinEntity> mAllItems;

    private LiveData<List<CategoryEntity>> mAllCategories;

    public StokuViewModel(Application application){
        super(application);
        mStokuRepository = new StokuRepository(application);
//        mAllItems = mStokuRepository.getAllItems();
        mAllCategories = mStokuRepository.getmAllCategoriesLiveData();
    }

    public LiveData<List<UserItemCategoryJoinEntity>> getmAllItemsLiveData(){
        return mAllItemsLiveData;
    }

    public List<UserItemCategoryJoinEntity> getAllItems(){
        return mStokuRepository.getAllItems();
    }

    public List<CategoryEntity> getAllCategories(){
        return mStokuRepository.getAllCategories();
    }

    public List<ListCategoryJoinEntity> getAllListItems(){
        return mStokuRepository.getAllListItems();
    }

    public LiveData<Boolean> getItemStatus(long itemId){
        return mStokuRepository.getItemStatus(itemId);
    }

    public LiveData<List<CategoryEntity>> getAllCategoriesLiveData(){
        return mAllCategories;
    }

    public void insertItem(UserItemEntity itemEntity){
        mStokuRepository.insertItem(itemEntity);
    }

    public void insertListItems(ListEntity... itemEntity){
        mStokuRepository.insertListItems(itemEntity);
    }

    public void insertListItem(ListEntity listEntity){
        mStokuRepository.insertListItem(listEntity);
    }

    public void insertCategory(CategoryEntity categoryEntity){
        mStokuRepository.insertCategory(categoryEntity);
    }

    public void deleteCategory(CategoryEntity categoryEntity){
        mStokuRepository.deleteCategory(categoryEntity);
    }

    public void updateCategory(CategoryEntity categoryEntity){
        mStokuRepository.updateCategory(categoryEntity);
    }

    public void updateItem(UserItemEntity userItemEntity){
        mStokuRepository.updateItem(userItemEntity);
    }

    public void updateItems(UserItemEntity... userItemEntity){
        mStokuRepository.updateItems(userItemEntity);
    }

    public void deleteItem(UserItemEntity userItemEntity){
        mStokuRepository.deleteItem(userItemEntity);
    }

    public void updateListItem(ListEntity listEntity){
        mStokuRepository.updateListItem(listEntity);
    }

    public void deleteListItem(ListEntity listEntity){
        mStokuRepository.deleteListItem(listEntity);
    }

    public long insertReport(ReportEntity reportEntity){
        return mStokuRepository.insertReport(reportEntity);
    }

    public void insertReportItem(ReportItemEntity... reportItemEntities){
        mStokuRepository.insertReportItems(reportItemEntities);
    }

    public LiveData<List<ReportItemCategoryJoinEntity>> getAllReportItemData(long reportId){
        return mStokuRepository.getAllReportItemsData(reportId);
    }

    public void deleteAllListData(){
        mStokuRepository.deleteAllListData();
    }

    public LiveData<List<ReportEntity>> getAllReports(){
        return mStokuRepository.getAllReports();
    }

    public long[] insertItemWithReturn(UserItemEntity... userItemEntity){
       return mStokuRepository.insertItemWithReturn(userItemEntity);
    }

    public long[] insertCategoryWithReturn(CategoryEntity... categoryEntity){
        return mStokuRepository.insertCategoryWithReturn(categoryEntity);
    }

    public void deleteReport(ReportEntity reportEntity){
        mStokuRepository.deleteReport(reportEntity);
    }
}
