package myapp.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.Connection;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("devel")
public class TestRowSetSpring {

    @Autowired
    DataSource dataSource;

    @Test
    public void testRowSetWithSpring() throws Exception {
        RowSetFactory factory = RowSetProvider.newFactory();
        CachedRowSet crs = factory.createCachedRowSet();

        try (Connection conn = dataSource.getConnection()) {
            crs.setCommand("SELECT * FROM NAME");
            crs.setConcurrency(ResultSet.CONCUR_UPDATABLE);
            crs.execute(conn);
        }

        boolean hasData = false;
        while (crs.next()) {
            hasData = true;
            System.out.printf("RowSet Data -> ID: %d, Name: %s\n",
                    crs.getInt("id"),
                    crs.getString("name"));
        }

        assertTrue(hasData);
        crs.close();
    }
}