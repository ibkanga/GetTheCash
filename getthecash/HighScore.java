package getthecash;
// @author IgorBarašin2681

import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.Map;


public class HighScore {
    
    TreeMultimap<Integer, String> HSMap = TreeMultimap.create(Ordering.natural().reverse(), Ordering.natural());
    
    public HighScore() {
        
    }

    public void createBaseAndTable() throws Exception{
	try {
            String createBase = "CREATE DATABASE IF NOT EXISTS highscoresDB;";
            String useBase = "USE highscoresDB;";
            String createTable = "CREATE TABLE IF NOT EXISTS highscores(ID int NOT NULL PRIMARY KEY, name text NOT NULL, score int NOT NULL)";
			
            Statement statement = getConnection().createStatement();
            statement.executeUpdate(createBase);
            statement.executeUpdate(useBase);
            statement.executeUpdate(createTable);
            
            for(int i = 0; i < 10; i++) {
                String writeToBase = "INSERT INTO highscores (ID, name, score) VALUES (" + i + ",'" + i + "AAA" + "'," + i * 10 + ");";
                statement.executeUpdate(writeToBase);
            }
	} catch (Exception e) {			            
	}
    }
	
    public Connection getConnection() throws Exception{
        try {
            String url = "jdbc:mysql://localhost:3306?autoReconnect=true&useSSL=false";
            String username = "root";
            String password = "root";
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            return connection;
	} catch (Exception e) {
            System.out.println(e);
	}
	return null;
    }
    
    public TreeMultimap<Integer, String> getScores() {
        try {
            String useBase = "USE highscoresDB;";
            String query = "SELECT * FROM highscores";
            Statement st = getConnection().createStatement();
            st.executeUpdate(useBase);
      
            ResultSet rs = st.executeQuery(query);
            int i = 0;
            while (rs.next()) {
                String name = rs.getString("name");      
                int score = rs.getInt("score");
                HSMap.put(score, name);
                i++;
                
                if(i == 10)
                    break;
            }           
            
            st.close();
             
        } catch (Exception e) {
            System.out.println(e);
        }
        
        return HSMap;
    }
    
    public void writeScores(TreeMultimap<Integer, String> scoreList) {
        try {
            String useBase = "USE highscoresDB;";
            String deleteFromHS = "DELETE FROM highscores;";
           
            Statement st = getConnection().createStatement();
            st.executeUpdate(useBase);
            st.executeUpdate(deleteFromHS);
            
            int i = 1;
            Map<Integer, Collection<String>> map = scoreList.asMap();          
            for(Map.Entry<Integer, Collection<String>> entry: map.entrySet()) {
                if(i == 11)
                    break;
                Integer key = entry.getKey();
                Collection<String> value = scoreList.get(key);
                for(String nick : value) {
                    String writeToBase = "INSERT INTO highscores (ID, name, score) VALUES (" + i + ",'" + nick + "'," + key + ");";
                    st.executeUpdate(writeToBase);
                    i++;
                }
            }
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    
}

