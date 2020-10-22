package com.kanq.demo.mapper;

import com.kanq.demo.config.aspect.DataSourceAnnotation;
import com.kanq.demo.config.aspect.DataSourceEnum;
import com.kanq.demo.entity.Weather;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yyc
 */
@Repository
public interface WeatherMapper {
    @DataSourceAnnotation(DataSourceEnum.SDE)
    int insert1(Weather weather);
    @DataSourceAnnotation(DataSourceEnum.SDE)
    int batchInsert(List<Weather> weatherList);
    @DataSourceAnnotation(DataSourceEnum.SDE)
    List<Weather> select(@Param("limit") Long limit, @Param("offset")Long offset);
    @DataSourceAnnotation(DataSourceEnum.SDE)
    void createDB();
    @DataSourceAnnotation(DataSourceEnum.SDE)
    void createTable();
}
