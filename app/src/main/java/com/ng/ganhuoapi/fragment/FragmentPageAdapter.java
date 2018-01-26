package com.ng.ganhuoapi.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/12/22.
 */

public class FragmentPageAdapter extends FragmentPagerAdapter {
    private List<String> titleList;
    private List<Fragment> fragmentList;

    /**
     * 注意：
     *
     * @param fm           这里的FragmentManager应该是getChildFragmentManager（）
     *                     在faragment的fragment中  应该是getChildFragmentManager（） 否则无法显示Viewpage内容
     * @param titleList
     * @param fragmentList
     */
    public FragmentPageAdapter(FragmentManager fm, List<String> titleList, List<Fragment> fragmentList) {
        super(fm);
        this.titleList = titleList;
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        if (fragmentList != null) {
            return fragmentList.get(position);
        } else {
            return null;
        }

    }

    @Override
    public int getCount() {
        if (titleList != null) {
            return titleList.size();
        } else {
            return 0;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title;
        if (titleList.get(position).equals("all")) {
            title = "全部";

        } else {
            title = titleList.get(position);
        }
        return title;
    }
}
