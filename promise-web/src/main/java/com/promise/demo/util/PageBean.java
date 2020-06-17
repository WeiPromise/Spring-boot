package com.promise.demo.util;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by fishiung on 2018-12-16
 */

public class PageBean<T> {

    private static final Integer DEFAULT_PAGE_SIZE = 10;


    private List<T> pageDatas = new ArrayList<>();

    private Integer totalCount;

    private PageBean(Integer totalCount, List<T> pageDatas) {
        if(pageDatas != null) {
            this.pageDatas = pageDatas;
        }
        this.totalCount = totalCount;
    }


    public List<T> getPageDatas() {
        return pageDatas;
    }

    public void setPageDatas(List<T> pageDatas) {
        this.pageDatas = pageDatas;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }



    public static <T> PageBean<T> of(Integer totalCount, List<T> pageDatas) {
        return new PageBean<>(totalCount, pageDatas);
    }

    public static <T> PageBean<T> ofFull(Integer pageNum, Integer pageSize, List<T> fullData) {
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        if (fullData == null) {
            fullData = Collections.emptyList();
        }
        int totalCount = fullData.size();
        int startIndex = (pageNum - 1) * pageSize;
        if (startIndex > totalCount) {
            startIndex = totalCount;
        }
        int toIndex = startIndex + pageSize;
        if (toIndex > totalCount) {
            toIndex = totalCount;
        }
        List<T> pageData = fullData.subList(startIndex, toIndex);
        return new PageBean<>(totalCount, pageData);
    }
}
