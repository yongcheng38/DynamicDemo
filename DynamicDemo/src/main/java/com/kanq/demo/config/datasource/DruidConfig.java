package com.kanq.demo.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties;
import com.alibaba.druid.util.Utils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;
import javax.servlet.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * druid 配置多数据源
 * 
 * @author mi
 */
@Configuration
public class DruidConfig {
    @Resource
    private  DynamicDataSourceConfig dynamicDataSourceConfig;


    @Bean(name = "dynamicDataSource")
    @Primary
    public DynamicDataSource dataSource(DruidProperties druidProperties) {
        Map<Object, Object> targetDataSources = new HashMap<>(16);

        List<Map<String, Object>> list = dynamicDataSourceConfig.getSourceList();
        for(Map map: list) {
            DruidDataSource datasource = new DruidDataSource();
            datasource.setUrl(map.get("url").toString());
            datasource.setUsername(map.get("username").toString());
            datasource.setPassword(map.get("password").toString());
            datasource.setDriverClassName(map.get("driverClassName").toString());
            datasource.setValidationQuery(map.get("validationQuery").toString());
            druidProperties.dataSource(datasource);
            targetDataSources.put(map.get("name").toString(), datasource);
        }
        return new DynamicDataSource((DruidDataSource)targetDataSources.get(list.get(0).get("name")), targetDataSources);
    }

    /**
     * 去除监控页面底部的广告
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    @ConditionalOnProperty(name = "spring.datasource.druid.statViewServlet.enabled", havingValue = "true")
    public FilterRegistrationBean removeDruidFilterRegistrationBean(DruidStatProperties properties) {
        // 获取web监控页面的参数
        DruidStatProperties.StatViewServlet config = properties.getStatViewServlet();
        // 提取common.js的配置路径
        String pattern = config.getUrlPattern() != null ? config.getUrlPattern() : "/druid/*";
        String commonJsPattern = pattern.replaceAll("\\*", "js/common.js");
        final String filePath = "support/http/resources/js/common.js";
        // 创建filter进行过滤
        Filter filter = new Filter() {
            @Override
            public void init(FilterConfig filterConfig){ }
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                    throws IOException, ServletException {
                chain.doFilter(request, response);
                // 重置缓冲区，响应头不会被重置
                response.resetBuffer();
                // 获取common.js
                String text = Utils.readFromResource(filePath);
                // 正则替换banner, 除去底部的广告信息
                text = text.replaceAll("<a.*?banner\"></a><br/>", "");
                text = text.replaceAll("powered.*?shrek.wang</a>", "");
                response.getWriter().write(text);
            }
            @Override
            public void destroy() { }
        };
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns(commonJsPattern);
        return registrationBean;
    }
}
