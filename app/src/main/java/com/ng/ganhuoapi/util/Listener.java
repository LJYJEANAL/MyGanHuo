package com.ng.ganhuoapi.util;

/**
 * Created by Administrator on 2017/12/20.
 */

public abstract class Listener<Status, Result> {

    public Listener() {
    }

    public abstract void onCallBack(Status status, Result reply);

    public void onSuccess(Result reply) {

    }

    public void onComplete() {

    }

    public void onFailed(String reply) {

    }

}