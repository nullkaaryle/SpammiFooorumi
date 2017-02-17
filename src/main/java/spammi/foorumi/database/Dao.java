package spammi.foorumi.database;

import java.sql.*;
import java.util.*;

public interface Dao<T, K> {

    T findOne(K key) throws SQLException;

    List<T> findAll() throws SQLException;
    
    T create(T t) throws SQLException;
    
}
