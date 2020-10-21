package com.kanq.demo.controller;

import com.kanq.demo.config.aspect.SystemLog;
import com.kanq.demo.entity.Weather;
import com.kanq.demo.service.WeatherService;
import org.springframework.util.StopWatch;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author yyc
 */
@RequestMapping("/weather")
@RestController
public class WeatherController {

    @Resource
    private WeatherService weatherService;

    /**
     * create database and table
     * @return
     */
    @GetMapping("/init")
    @SystemLog(description="创建数据库和表",system="数据解析模块")
    public boolean init(){
        return weatherService.init();
    }

    /**
     * Pagination Query
     * @param limit
     * @param offset
     * @return
     */
    @GetMapping("/{limit}/{offset}")
    public List<Weather> queryWeather(@PathVariable Long limit, @PathVariable Long offset){
        StopWatch  stopWatch = new StopWatch();
        stopWatch.start();

        //此处业务逻辑
        List<Weather> list = weatherService.query(limit, offset);

        stopWatch.stop();

        System.out.println("执行时长："+stopWatch.getTotalTimeSeconds()+"秒");

        org.apache.commons.lang3.time.StopWatch stopWatch1 = new org.apache.commons.lang3.time.StopWatch();

        stopWatch1.start();
        // 执行时间（1s）

        // 结束时间
        stopWatch1.stop();
        System.out.println("执行时长：" + stopWatch1.getTime(TimeUnit.SECONDS) + " 秒.");
        return list;
    }

    /**
     * upload single weather info
     * @param temperature
     * @param humidity
     * @return
     */
    @PostMapping("/{temperature}/{humidity}")
    public int saveWeather(@PathVariable int temperature, @PathVariable float humidity){

        return weatherService.save(temperature, humidity);
    }

    /**
     * upload multi weather info
     * @param weatherList
     * @return
     */
    @PostMapping("/batch")
    @SystemLog(description="数据入库",system="数据批量入库")
    public int batchSaveWeather(@Validated  @RequestBody List<Weather> weatherList){
        return weatherService.save(weatherList);
    }

}
