package spammi.foorumi.database;

import java.sql.*;
import java.util.*;

public interface Dao<T, K> {

    T findOne(K key) throws SQLException;

    List<T> findAll() throws SQLException;

    void delete(K key) throws SQLException;
    
    //T create() throws SQLException;
    
}
