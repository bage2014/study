package com.bage.study.es;

import com.bage.study.es.crud.DefaultEsService;
import com.bage.study.es.crud.EsCrudService;
import com.bage.study.es.model.Person;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class EsCrudServiceTest {

    private static EsCrudService esService = new DefaultEsService();

    @Test
    public void crud() throws IOException {
        // CRUD
        long id = System.currentTimeMillis();
        System.out.println("id: " + id);

        // 1736691762857

        insert(id);
        query(id);

        // update
        update(id);
        query(id);

//        delete(id);
//        query(id);

    }

    private static void update(long id) throws IOException {
        Person person = new Person(id, 25, "Janette Doe [new]", new Date());
        int update = esService.update(person);
        System.out.println(update);
    }

    private static void delete(long id) throws IOException {
        int delete = esService.delete(id);
        System.out.println(delete);
    }

    private static void query(long id) throws IOException {
        List<Person> query = esService.query(id);
        System.out.println(query);
    }

    private static void insert(long id) throws IOException {
        Person person = new Person(id, 25, "Janette Doe", new Date());
        int insert = esService.insert(person);
        System.out.println(insert);
    }

}
