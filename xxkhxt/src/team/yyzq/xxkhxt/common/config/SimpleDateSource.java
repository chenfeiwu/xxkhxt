package team.yyzq.xxkhxt.common.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**JDBC数据源
 * @author feiwu
 *
 * 2017年3月13日下午3:27:45 
 */
public class SimpleDateSource implements DataSource{
	private static  Log log = LogFactory.getLog(SimpleDateSource.class);
    //连接池 
    private static LinkedList<Connection> pool = new LinkedList<Connection>();
    private static SimpleDateSource instance = new SimpleDateSource();
    private static Properties pro = null;
    static {
    	pro = new Properties();
		try {
			File file = new File(SimpleDateSource.class.getResource("/").getPath() + File.separator + "initConfig.properties");
			InputStream inputStrem = new FileInputStream(file);
			pro.load(inputStrem );
			inputStrem.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
		}
    	try {
	        Class.forName(pro.getProperty("driverClass"));
    	}catch (ClassNotFoundException e) {
    	        log.error("找不到驱动类！", e);
    	}
    }

    public SimpleDateSource() {
    	
    }

    /** 
     * 获取数据源单例 
     * 
     * @return 数据源单例 
     */ 
    public SimpleDateSource instance() {
    	if (instance == null) instance = new SimpleDateSource();
    	return instance;
    }

    /** 
     * 获取一个数据库连接 
     * 
     * @return 一个数据库连接 
     * @throws SQLException 
     */ 
    public Connection getConnection() throws SQLException {
    	synchronized (pool) {
    	        if (pool.size() > 0) return pool.removeFirst();
    	        else return makeConnection();
    	}
    }

    /** 
     * 连接归池 
     * 
     * @param conn 
     */ 
    public static void freeConnection(Connection conn) {
    	pool.addLast(conn);
    }

    private Connection makeConnection() throws SQLException {
    	return DriverManager.getConnection(pro.getProperty("jdbcUrl"), pro.getProperty("user"), pro.getProperty("password"));
    }

    public Connection getConnection(String username, String password) throws SQLException {
    	return DriverManager.getConnection(pro.getProperty("jdbcUrl"), username, password);
    }

    public PrintWriter getLogWriter() throws SQLException {
    	return null;
    }

    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    public void setLoginTimeout(int seconds) throws SQLException {

    }

    public int getLoginTimeout() throws SQLException {
    	return 0;
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
    	return null;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
    	return false;
    }

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}
}
