package myapp.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class SpringNameDao {

    @Autowired
    private JdbcTemplate jt;

    private static Name nameMapper(ResultSet rs, int i) throws SQLException {
        var n = new Name();
        n.setId(rs.getInt("id"));
        n.setName(rs.getString("name"));
        return n;
    }

    public void addName(Name n) {
        String sql = "INSERT INTO NAME (id, name) VALUES (?, ?)";
        jt.update(sql, n.getId(), n.getName());
    }

    public void deleteName(int id)  {
        String sql = "DELETE FROM NAME WHERE id = ?";
        jt.update(sql, id);
    }

    public Name findName(int id) {
        String sql = "SELECT * FROM NAME WHERE id = ?";
        List<Name> results = jt.query(sql, SpringNameDao::nameMapper, id);
        return results.isEmpty() ? null : results.get(0);
    }

    public List<Name> findNames() {
        String sql = "SELECT * FROM NAME ORDER BY name";
        return jt.query(sql, SpringNameDao::nameMapper);
    }

    public void updateName(String oldName, String newName) {
        String sql = "UPDATE NAME SET name = ? WHERE name = ?";
        jt.update(sql, newName, oldName);
    }

    public int countNames(String pattern) {
        String sql = "SELECT COUNT(*) FROM NAME WHERE name LIKE ?";
        return jt.queryForObject(sql, Integer.class, pattern);
    }

    public void addNameTwoTimes(Name n) {
        addName(n);
        addName(n);
    }

    public void longWork() {
        try {
            jt.execute((Connection conn) -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return null;
            });
        } catch (Exception e) {
        }
    }
}