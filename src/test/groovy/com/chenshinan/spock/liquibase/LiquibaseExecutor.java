package com.chenshinan.spock.liquibase;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.parser.ChangeLogParserFactory;
import liquibase.parser.ext.GroovyLiquibaseChangeLogParser;
import liquibase.resource.ResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author shinan.chen
 * @date 2018/8/25
 */
@Component
public class LiquibaseExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LiquibaseExecutor.class);
    private static final String SUFFIX_GROOVY = ".groovy";
    private static final String SUFFIX_XML = ".xml";
    private static final String SUFFIX_SQL = ".sql";

    @Value("${liquibase.dir:#{null}}")
    private String lqDir;

    @Value("${liquibase.drop:false}")
    private Boolean drop;

    @Value("${spring.datasource.url:#{null}}")
    private String dsUrl;

    @Value("${spring.datasource.username:#{null}}")
    private String dsUserName;

    @Value("${spring.datasource.password:#{null}}")
    private String dsPassword;

    public void execute() throws IOException, SQLException, LiquibaseException {
        LOGGER.info("开始初始化数据库");
        ChangeLogParserFactory.getInstance().register(new GroovyLiquibaseChangeLogParser());
        ResourceAccessor accessor = new MyFileSystemResourceAccessor(lqDir);
        Set<String> fileNameSet = accessor.list(null, File.separator, true, false, true);
        List<String> fileNameList = new ArrayList<>(fileNameSet);
        Collections.sort(fileNameList);
        //建立数据库连接
        DataSource dataSource = new DriverManagerDataSource(dsUrl, dsUserName, dsPassword);

        //是否清空数据库
        if (drop) {
            Liquibase liquibase = new Liquibase(null, accessor, new JdbcConnection(dataSource.getConnection()));
            liquibase.dropAll();
        }

        //执行xml脚本
        for (String file : fileNameList) {
            if (file.endsWith(SUFFIX_XML)) {
                Liquibase liquibase = new Liquibase(file, accessor, new JdbcConnection(dataSource.getConnection()));
                liquibase.update(new Contexts());
            }
        }

        //执行groovy脚本
        for (String file : fileNameList) {
            if (file.endsWith(SUFFIX_GROOVY)) {
                Liquibase liquibase = new Liquibase(file, accessor, new JdbcConnection(dataSource.getConnection()));
                liquibase.update(new Contexts());
            }
        }

        //执行sql语句，可以初始化数据
        for(String file : fileNameList){
            if (file.endsWith(SUFFIX_SQL)) {
                Liquibase liquibase = new Liquibase(file, accessor, new JdbcConnection(dataSource.getConnection()));
                liquibase.update(new Contexts());
            }
        }
        LOGGER.info("初始化数据库成功");
    }
}
