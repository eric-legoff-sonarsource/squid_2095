package org.sonarsource.support;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Reproducer {

  private static final Logger LOGGER = Logger.getLogger( Reproducer.class.getName() );
  private static final String SOME_SQL_STATEMENT ="SELECT * from DUMMY";

  private static final String foo ="SELECT * from DUMMY";

  private Connection getConnection(String user, String password) throws SQLException, ClassNotFoundException {



     Class.forName("com.mysql.jdbc.Driver");


    Connection con =DriverManager.getConnection("jdbc:mysql://localhost:3306/icms_employees?autoReconnect=true&useSSL=false",user, password);


    return con;
  }

  public void someMethod() {
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet resultset = null;


    try {
      con = getConnection("root", "admin");
      pstmt = con.prepareStatement(SOME_SQL_STATEMENT);

      pstmt.executeUpdate();

      pstmt = con.prepareStatement(SOME_SQL_STATEMENT);
      
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, "Error In Class " + Reproducer.class.getName() + ": " + e.getMessage(), e);

    } catch (ClassNotFoundException e) {
      LOGGER.log(Level.SEVERE, "Class not found " + e.getMessage());
    } finally {
      releaseDBResources(resultset, pstmt, con);
    }

}

  protected void releaseDBResources(ResultSet resultset, Statement statement, Connection connection) {
    try {
      if (resultset != null) {
        resultset.close();
      }
      if (statement != null) {
        statement.close();
      }
      if (connection != null) {
        connection.close();
      }
    } catch (SQLException sqlexception) {
      LOGGER.log(Level.SEVERE, "<----------- Error in Closing DB Resources ---------" + sqlexception.getMessage());
      LOGGER.log(Level.SEVERE, sqlexception.getMessage(), sqlexception);
    }
  }

}
