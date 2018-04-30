package task.lt.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.ParametersAreNonnullByDefault;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@ParametersAreNonnullByDefault
public class DbH2Test {

    @DataProvider(name = "db-urls")
    public static Object[][] dbUrls() {
        return new Object[][] {
                {"jdbc:h2:./target/db/test.db"},    // persistent db
                {"jdbc:h2:mem:test.db"},            // in-memory db
        };
    }

    @Test(dataProvider = "db-urls")
    public void testDatabase(String url) throws SQLException {
        Connection connection = DriverManager.getConnection(url);
        Statement s = connection.createStatement();
        try {
            s.execute("drop table test");
        } catch (SQLException sqlEx) {
            System.out.println("Table not found, not dropping");
        }
        s.execute("create table test(id bigint primary key, name varchar(64))");
        PreparedStatement ps = connection.prepareStatement("select * from test");
        ResultSet r = ps.executeQuery();
        if (r.next()) {
            System.out.println("data?");
        }
        r.close();
        ps.close();
        s.close();
        connection.close();
    }
}
