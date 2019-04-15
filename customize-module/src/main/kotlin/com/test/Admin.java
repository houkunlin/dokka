package com.test;

import java.io.Serializable;

/**
 * @author houkunlin
 */
public class Admin implements Serializable {
    /**
     * 主键
     */
    public Long id;
    /**
     * 类型
     */
    public static String type;
    /**
     * 参数
     */
    public final static int CODE = 1;

    /**
     * 获取主键
     *
     * @return 主键
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
