package com.tobe.fishking.v2.config.datasource;

import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class CustomMysqlDialect extends MySQL5Dialect
{
    public CustomMysqlDialect()
    {
        super();

        // register custom/inner function here
        this.registerFunction("group_concat"
                , new SQLFunctionTemplate(StandardBasicTypes.STRING, "group_concat(?1)"));


        this.registerFunction("group_concat_distinct"
                , new SQLFunctionTemplate(StandardBasicTypes.STRING, "group_concat(distinct ?1)"));
    }
}
