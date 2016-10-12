package com.brightdairy.personal.brightdairy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.brightdairy.personal.brightdairy.popup.GeneralLoadingPopup;

/**
 * Created by shuangmusuihua on 2016/10/12.
 */

public abstract class LazyLoadFragment extends Fragment
{
    private boolean hasCreateView;
    private boolean isFragmentVisible;
    protected View fragmentView;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) 
    {
        super.setUserVisibleHint(isVisibleToUser);
        
        if (fragmentView == null)
        {
            return;
        }
        
        hasCreateView = true;
        
        if (isVisibleToUser)
        {
            onFragmentVisibleChange(true);
            isFragmentVisible = true;
            return;
        }
        
        if (isFragmentVisible)
        {
            onFragmentVisibleChange(false);
            isFragmentVisible = false;
        }
        
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        hasCreateView = false;
        isFragmentVisible = false;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) 
    {
        super.onViewCreated(view, savedInstanceState);
        
        if (!hasCreateView && getUserVisibleHint())
        {
            onFragmentVisibleChange(true);
            isFragmentVisible = true;
        }
    }

    private void onFragmentVisibleChange(boolean fragmentVisible)
    {
        if (fragmentVisible)
        {
            onFragmentVisible();
        } else 
        {
            onFragmentInVisible();
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        hideLoading();
    }

    protected abstract void onFragmentInVisible();

    protected abstract void onFragmentVisible();


    private GeneralLoadingPopup generalLoadingPopup;
    protected void showLoading()
    {

        if (generalLoadingPopup == null)
        {
            generalLoadingPopup = new GeneralLoadingPopup();
        }

        generalLoadingPopup.show(getActivity().getSupportFragmentManager(), "loadingdata");
    }


    protected void hideLoading()
    {
        if (generalLoadingPopup != null && generalLoadingPopup.isVisible())
        {
            generalLoadingPopup.dismiss();
        }
    }
}
