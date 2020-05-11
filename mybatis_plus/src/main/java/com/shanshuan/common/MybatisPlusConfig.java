package com.shanshuan.common;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;

@EnableTransactionManagement
@Configuration
@MapperScan(basePackages = {"com.shanshuan.mapper"}, sqlSessionFactoryRef = "mybatisSqlSessionFactoryBean")
public class MybatisPlusConfig {

    @Autowired
    private DataSource dataSource;
 
     @Autowired
     private MybatisPlusProperties properties;
 
     @Autowired
     private ResourceLoader resourceLoader = new DefaultResourceLoader();
 
     @Autowired(required = false)
     private Interceptor[] interceptors;

     @Autowired(required = false)
     private DatabaseIdProvider databaseIdProvider;

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        // paginationInterceptor.setLimit(500);
        // 开启 count 的 join 优化,只针对部分 left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        return paginationInterceptor;
    }

    @Bean
    public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean() throws IOException {
          MybatisSqlSessionFactoryBean mybatisPlus = new MybatisSqlSessionFactoryBean();
          mybatisPlus.setDataSource(dataSource);
          mybatisPlus.setVfs(SpringBootVFS.class);
          String configLocation = this.properties.getConfigLocation();
          if (StringUtils.isNotBlank(configLocation)) {
           mybatisPlus.setConfigLocation(this.resourceLoader.getResource(configLocation));
          }
          mybatisPlus.setConfiguration(properties.getConfiguration());
          mybatisPlus.setPlugins(this.interceptors);
          MybatisConfiguration mc = new MybatisConfiguration();
          mc.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
          mc.setMapUnderscoreToCamelCase(false);// 数据库和java都是驼峰，就不需要
          mybatisPlus.setConfiguration(mc);
          if (this.databaseIdProvider != null) {
           mybatisPlus.setDatabaseIdProvider(this.databaseIdProvider);
          }
          mybatisPlus.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
          mybatisPlus.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
          mybatisPlus.setMapperLocations(this.properties.resolveMapperLocations());
          return mybatisPlus;
    }



}
