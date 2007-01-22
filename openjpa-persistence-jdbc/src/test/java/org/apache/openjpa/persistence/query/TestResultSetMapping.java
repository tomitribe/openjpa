/*
 * Copyright 2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.openjpa.persistence.query;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import junit.framework.TestCase;
import junit.textui.TestRunner;

public class TestResultSetMapping extends TestCase {

    private EntityManagerFactory emf;

    public void setUp() {
        Map props = new HashMap();
        props.put("openjpa.MetaDataFactory", "jpa(Types=" + 
            org.apache.openjpa.persistence.query.SimpleEntity.class.getName() + ")");
        emf = Persistence.createEntityManagerFactory("test", props);

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(new SimpleEntity("tName", "tValue"));
        em.getTransaction().commit();
        em.close();
    }

    public void tearDown() {
        if (emf == null)
            return;
        try {
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            em.createQuery("delete from simple").executeUpdate();
            em.getTransaction().commit();
            em.close();
            emf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testSimpleQuery() {
        EntityManager em = emf.createEntityManager();
        Query q = em.createNamedQuery("findSimpleEntitites");
        List res = q.getResultList();
        assertNotNull(res);
        for (Iterator resultIter = res.iterator(); resultIter.hasNext();) {
            assertSame(resultIter.next().getClass(), SimpleEntity.class);
        }
        em.close();
    }

    public static void main(String[] args) {
        TestRunner.run(TestResultSetMapping.class);
    }
}