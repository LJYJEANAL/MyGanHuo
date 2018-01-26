package com.ng.ganhuoapi.fragment;

import android.content.Context;

import com.ng.ganhuoapi.base.IBasePresenter;
import com.ng.ganhuoapi.base.IBaseView;
import com.ng.ganhuoapi.constant.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 干货Presenter逻辑
 */

public class FragmentPresenter implements IBasePresenter {
    private  IBaseView iBaseView;

    private Context context;
    private String [] tabTitle;

    public FragmentPresenter(Context context, IBaseView iBaseView,String [] tabTitle) {
        this.context = context;
        this.iBaseView=iBaseView;
        this.tabTitle=tabTitle;
    }



    @Override
    public void doLoadData(boolean isLoadMore,int limit,int start) {
        try{
            iBaseView.onShowLoading();
            if (tabTitle!=null){
                List<String>  titleList=new ArrayList<>(Arrays.asList(tabTitle));
                iBaseView.onHideLoading();
                iBaseView.showData(titleList);
            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void doRefresh() {
        doLoadData(false, Constant.limit,Constant.start);

    }

}
