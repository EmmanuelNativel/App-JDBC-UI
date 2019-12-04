package jdbcUI;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

/**
 * ECRAN DE CONNEXION A LA BASE DE DONNEES
 */

public class LoginView extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    protected final String URL_PLACEHOLDER = "url to database";
    protected final String ID_PLACEHOLDER = "id user";
    protected final String PWD_PLACEHOLDER = "password";
    protected final String DRIVER_PLACEHOLDER = "driver name";

    protected CustomTextField urlField, idField, pwdField, driverField;
    protected JButton loginBtn;

    protected JDBCManager manager;

    public LoginView(JDBCManager manager) {

        this.manager = manager;

        this.setTitle("SGBD UI - Connection to DataBase");
        this.setSize(300, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        urlField = new CustomTextField().setPlaceholder(URL_PLACEHOLDER);
        idField = new CustomTextField().setPlaceholder(ID_PLACEHOLDER);
        pwdField = new CustomTextField().setPlaceholder(PWD_PLACEHOLDER);
        driverField = new CustomTextField().setPlaceholder(DRIVER_PLACEHOLDER);

        loginBtn = new JButton("Log In");
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.addActionListener(this);

        Container contener = this.getContentPane();
        contener.setLayout(new BoxLayout(contener, BoxLayout.Y_AXIS));
        contener.add(Box.createVerticalGlue());
        contener.add(urlField);
        contener.add(Box.createVerticalGlue());
        contener.add(idField);
        contener.add(Box.createVerticalGlue());
        contener.add(pwdField);
        contener.add(Box.createVerticalGlue());
        contener.add(driverField);
        contener.add(Box.createVerticalGlue());
        contener.add(loginBtn);
        contener.add(Box.createVerticalGlue());

        //Affichage
        setVisible(true);
        loginBtn.requestFocusInWindow();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton target = (JButton) e.getSource();

        if(target == loginBtn){

            String url = urlField.getText();
            String login = loginBtn.getText();
            String password = pwdField.getText();
            String driverName = driverField.getText();

            try {
                if( urlField.getText().equals(URL_PLACEHOLDER) && idField.getText().equals(ID_PLACEHOLDER) 
                    && pwdField.getText().equals(PWD_PLACEHOLDER) && driverField.getText().equals(DRIVER_PLACEHOLDER) ){
                        manager.connect();
                    } else {
                        manager.connect(url, login, password, driverName);
                    }
            }
            catch(SQLException e1) {System.out.println(e1.getMessage());}
            catch(ClassNotFoundException e2) {e2.printStackTrace();}

            if(manager.isConnected()){
                new QueryView(manager);
                this.dispose();
            }
        }

    }


}