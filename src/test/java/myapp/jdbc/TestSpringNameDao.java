package myapp.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("devel")
public class TestSpringNameDao {

    @Autowired
    SpringNameDao dao;

    private Name createName(int id, String val) {
        Name n = new Name();
        n.setId(id);
        n.setName(val);
        return n;
    }

    @Test
    public void testNames() {
        dao.deleteName(100);
        dao.deleteName(200);

        dao.addName(createName(100, "Hello"));
        dao.addName(createName(200, "Salut"));

        assertEquals("Hello", dao.findName(100).getName());
        assertEquals("Salut", dao.findName(200).getName());

        dao.findNames().forEach(n -> System.out.println(n.getId() + " : " + n.getName()));
    }

    @Test
    public void testErrors() {
        dao.deleteName(300);

        assertThrows(DuplicateKeyException.class, () -> {
            dao.addName(createName(300, "Bye"));
            dao.addName(createName(300, "Au revoir"));
        });

        assertEquals("Bye", dao.findName(300).getName());
    }

    @Test
    public void testUpdate() {
        dao.deleteName(400);
        dao.addName(createName(400, "OldName"));

        String newName = "Change1";
        dao.updateName("OldName", newName);

        Name result = dao.findName(400);
        assertEquals(newName, result.getName());

        dao.updateName(newName, "OldName");
        assertEquals("OldName", dao.findName(400).getName());
    }

    @Test
    public void testCount() {
        dao.deleteName(500);
        dao.addName(createName(500, "SpringUser"));

        int count = dao.countNames("Spring%");
        assertTrue(count >= 1);
    }

    @Test
    public void testWorks() throws Exception {
        long debut = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 1; i < 5; i++) {
            executor.execute(dao::longWork);
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.HOURS);
        long fin = System.currentTimeMillis();

        System.out.println("duree = " + (fin - debut) + "ms");
    }

    @Test
    public void testTransactionRollback() {
        int testId = 999;
        Name n = createName(testId, "RollbackTest");

        dao.deleteName(testId);

        assertThrows(DuplicateKeyException.class, () -> {
            dao.addNameTwoTimes(n);
        });

        Name result = dao.findName(testId);
        assertNull(result);
    }
}