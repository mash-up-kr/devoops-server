package com.devoops.service.webhook;


import jakarta.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class DatabaseInfoLogger {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void logDatabaseInfo() {
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();

            System.out.println("🔍 Database Info:");
            System.out.println("   URL      : " + metaData.getURL());
            System.out.println("   User     : " + metaData.getUserName());
            System.out.println("   Product  : " + metaData.getDatabaseProductName());
            System.out.println("   Version  : " + metaData.getDatabaseProductVersion());
            System.out.println("   Driver   : " + metaData.getDriverName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
