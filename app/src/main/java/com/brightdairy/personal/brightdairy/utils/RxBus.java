package com.brightdairy.personal.brightdairy.utils;


import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by shuangmusuihua on 2016/9/19.
 */
public class RxBus
{
    private static RxBus mRxBus;
    private RxBus() {}
    public static RxBus EventBus()
    {
        if (mRxBus == null)
        {
            mRxBus = new RxBus();
        }

        return mRxBus;
    }

    private final Subject<Object, Object> RXBUS = new SerializedSubject<>(PublishSubject.create());

    public void dispatchEvent(Object event)
    {
        RXBUS.onNext(event);
    }

    public  Observable<Object> EventDispatcher()
    {
        return  RXBUS;
    }

    public boolean hasObservers()
    {
        return  RXBUS.hasObservers();
    }
}
