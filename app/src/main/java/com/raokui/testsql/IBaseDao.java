package com.raokui.testsql;

/**
 * Created by 饶魁 on 2017/12/22.
 */

public interface IBaseDao<T> {

    Long insert(T entity);

}
