package com.brightdairy.personal.brightdairy.fragment;

import com.brightdairy.personal.api.GlobalHttpConfig;
import com.brightdairy.personal.brightdairy.R;

import java.util.ArrayList;

/**
 * Created by shuangmusuihua on 2016/10/12.
 */

public class FragmentHelper
{

    private static ArrayList<OrderCenterPageFragment> orderCenterPageFragments;

    public static ArrayList<OrderCenterPageFragment> getOrderCenterPageFragments()
    {
        if (orderCenterPageFragments == null)
        {
            orderCenterPageFragments = new ArrayList<>();

            orderCenterPageFragments.add(OrderCenterPageFragment
                    .newInstance(GlobalHttpConfig.ORDER_STATUS.ORDER_CREATED));

            orderCenterPageFragments.add(OrderCenterPageFragment
                    .newInstance(GlobalHttpConfig.ORDER_STATUS.ORDER_REJECTED));

            orderCenterPageFragments.add(OrderCenterPageFragment
                    .newInstance(GlobalHttpConfig.ORDER_STATUS.ORDER_PROCESSING));

            orderCenterPageFragments.add(OrderCenterPageFragment
                    .newInstance(GlobalHttpConfig.ORDER_STATUS.ORDER_APPROVED));

            orderCenterPageFragments.add(OrderCenterPageFragment
                    .newInstance(GlobalHttpConfig.ORDER_STATUS.ORDER_COMPLETED));

            orderCenterPageFragments.add(OrderCenterPageFragment
                    .newInstance(GlobalHttpConfig.ORDER_STATUS.ORDER_CANCELLED));
        }


        return orderCenterPageFragments;
    }


    private static ArrayList<String> orderCenterPageTitles;
    public static ArrayList<String> getOrderCenterPageTitles()
    {

        if (orderCenterPageTitles == null)
        {
            orderCenterPageTitles = new ArrayList<>();

            orderCenterPageTitles.add("待付款");
            orderCenterPageTitles.add("未通过");
            orderCenterPageTitles.add("待审核");
            orderCenterPageTitles.add("配送中");
            orderCenterPageTitles.add("已完成");
            orderCenterPageTitles.add("已取消");

        }

        return orderCenterPageTitles;
    }


    private static ArrayList<Integer> orderCenterUnIcon;
    public static ArrayList<Integer> getOrderCenterUnIcon()
    {
        if (orderCenterUnIcon == null)
        {
            orderCenterUnIcon = new ArrayList<>();

            orderCenterUnIcon.add(R.mipmap.order_waitpay);
            orderCenterUnIcon.add(R.mipmap.order_examine);
            orderCenterUnIcon.add(R.mipmap.order_calendar);
            orderCenterUnIcon.add(R.mipmap.order_delivery);
            orderCenterUnIcon.add(R.mipmap.order_finish);
            orderCenterUnIcon.add(R.mipmap.order_cancel);
        }

        return orderCenterUnIcon;
    }


    private static ArrayList<Integer> orderCenterIcon;
    public static ArrayList<Integer> getOrderCenterIcon()
    {
        if (orderCenterIcon == null)
        {
            orderCenterIcon = new ArrayList<>();

            orderCenterIcon.add(R.mipmap.order_waitpay_highlight);
            orderCenterIcon.add(R.mipmap.order_examine_highlight);
            orderCenterIcon.add(R.mipmap.order_calendar_highlight);
            orderCenterIcon.add(R.mipmap.order_delivery_highlight);
            orderCenterIcon.add(R.mipmap.order_finish_highlight);
            orderCenterIcon.add(R.mipmap.order_cancel_highlight);
        }

        return orderCenterIcon;
    }

}
