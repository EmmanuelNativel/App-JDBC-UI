package jdbcUI;

/**
 * Main
 * 
 * First, start Mariadb : $mysql.server start
 * Then, start apache server for phpmyadmin : $apachectl start
 * Finally, go to : localhost/~username/phpmyadmin
 */

public class Main {


    public static void main(String[] args) {

        JDBCManager manager = new JDBCManager();

        if( !manager.isConnected() ) {
            new LoginView(manager); 
        }
    }
}