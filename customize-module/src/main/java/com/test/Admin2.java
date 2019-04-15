package com.test;

import java.io.Serializable;

/**
 * @author houkunlin
 */
public class Admin2 implements Serializable {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static String getType() {
        return type;
    }

    public static void setType(String type) {
        Admin2.type = type;
    }

    public static int getCODE() {
        return CODE;
    }
}
