package com.jpa;

public class Persistence {

    private static EntityManagerFactory entityManagerFactory;

    public static EntityManagerFactory createEntityManagerFactory(String name) {
        if (entityManagerFactory == null) {
            entityManagerFactory = new EntityManagerFactory();
        }

        return entityManagerFactory;
}

}
