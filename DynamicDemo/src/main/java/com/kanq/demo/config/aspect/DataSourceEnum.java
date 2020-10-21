package com.kanq.demo.config.aspect;

/**
 * @author yyc
 */

public enum DataSourceEnum {
    /** 主库 */
    MASTER("MASTER"),

    /** 从库 */
    SLAVE("SLAVE"),

    /** 空间数据库 * */
    SDE("SDE");
    private final String dataSourceType;

    DataSourceEnum(String dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public String getDataSourceType(){
        return dataSourceType;
    }
}
