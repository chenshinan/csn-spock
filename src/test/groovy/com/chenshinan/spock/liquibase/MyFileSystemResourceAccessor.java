package com.chenshinan.spock.liquibase;

import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;

import java.io.File;
import java.io.IOException;

/**
 * @author shinan.chen
 * @date 2018/8/26
 */
public class MyFileSystemResourceAccessor extends FileSystemResourceAccessor {

    private File baseDirectory;

    /**
     * 没有创建根文件夹
     */
    public MyFileSystemResourceAccessor() {
        super();
    }

    /**
     * 根据相对路径创建根文件夹
     */
    public MyFileSystemResourceAccessor(String dir) {
        super(dir);
    }

    @Override
    protected String convertToPath(String path) {
        if (this.baseDirectory == null) {
            return path;
        } else {
            try {
                return new File(path).getCanonicalPath();
            } catch (IOException e) {
                throw new UnexpectedLiquibaseException(e);
            }
        }
    }
}
