package com.bm.entity;


import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/17.
 */
public class PageDataList<T> implements Serializable {
    public String total;
    public List<T> list;
}
