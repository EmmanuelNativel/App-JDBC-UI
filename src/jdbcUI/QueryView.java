package jdbcUI;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import javax.swing.*;
import javax.swing.table.*;

/**
 * Home
 */
public class QueryView extends JFrame implements ActionListener, ItemListener {

    protected JTextArea queryArea;
    protected JButton queryValidationBtn, rollbackBtn, commitBtn, resetBtn;
    protected JTable table;
    protected JDBCManager manager;
    protected JScrollPane tableScroll;
    protected JCheckBox autocommitCheck;
    protected JLabel nbRowsLabel;

    public QueryView(JDBCManager manager) {
        this.manager = manager;
        this.setTitle("SGBD UI - Query managment");
        this.setSize(950, 650);
        this.setLocationRelativeTo(null);
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                System.out.println("Fermeture demandée");
                manager.quit();
                System.exit(-1);
            }
        });

        rollbackBtn = new JButton("Rollback");
        rollbackBtn.addActionListener(this);
        rollbackBtn.setEnabled(false);
        commitBtn = new JButton("Commit");
        commitBtn.addActionListener(this);
        commitBtn.setEnabled(false);

        resetBtn = new JButton("Reset");
        resetBtn.addActionListener(this);

        autocommitCheck = new JCheckBox("Transaction mode");
        autocommitCheck.addItemListener(this);

        queryArea = new JTextArea();
        queryArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        queryArea.setMinimumSize(new Dimension(880, 60));
        queryArea.setPreferredSize(new Dimension(880, 60));
        queryArea.setMaximumSize(new Dimension(880, 60));

        queryValidationBtn = new JButton("GO");
        queryValidationBtn.addActionListener(this);
        queryValidationBtn.setAlignmentX(Component.RIGHT_ALIGNMENT);
        queryValidationBtn.setMinimumSize(new Dimension(60, 60));
        queryValidationBtn.setPreferredSize(new Dimension(60, 60));
        queryValidationBtn.setMaximumSize(new Dimension(60, 60));

        JPanel northPanelActions = new JPanel();
        northPanelActions.setLayout(new BoxLayout(northPanelActions, BoxLayout.X_AXIS));
        northPanelActions.add(autocommitCheck);
        northPanelActions.add(commitBtn);
        northPanelActions.add(rollbackBtn);
        northPanelActions.add(Box.createHorizontalGlue());
        northPanelActions.add(resetBtn);

        JPanel northPanelQuery = new JPanel();
        northPanelQuery.setLayout(new BoxLayout(northPanelQuery, BoxLayout.X_AXIS));
        northPanelQuery.add(Box.createHorizontalGlue());
        northPanelQuery.add(queryArea);
        northPanelQuery.add(Box.createHorizontalGlue());
        northPanelQuery.add(queryValidationBtn);
        northPanelQuery.add(Box.createHorizontalGlue());

        Container contener = this.getContentPane();
        contener.setLayout(new BorderLayout());

        JPanel northPanelMain = new JPanel();
        northPanelMain.setLayout(new BoxLayout(northPanelMain, BoxLayout.Y_AXIS));
        northPanelMain.add(northPanelActions);
        northPanelMain.add(northPanelQuery);

        table = new JTable(){
            public boolean isCellEditable(int row, int column) {                
                return false;               
            };
        };
        tableScroll = new JScrollPane(table);
        nbRowsLabel = new JLabel();

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(nbRowsLabel);
        centerPanel.add(tableScroll);
        
        contener.add(northPanelMain, BorderLayout.NORTH);
        contener.add(centerPanel, BorderLayout.CENTER);

        //Affichage
        setVisible(true);
    }

    public void resultSetToTableModel(ResultSet rs, JTable table) throws SQLException{
        //Création du model du tableau
        DefaultTableModel tableModel = new DefaultTableModel();

        //Récupération des méta données du résulSet
        ResultSetMetaData metaData = rs.getMetaData();

        //Nombre de colonnes 
        int columnCount = metaData.getColumnCount();

        //Récupération des noms des colonnes et ajout dans le tableau
        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++){
            tableModel.addColumn(metaData.getColumnLabel(columnIndex));
        }

        //Création de l'objet qui va représenter une ligne du tableau
        Object[] row = new Object[columnCount];

        //Parcours du resulSet
        while (rs.next()){
            // Récupération des objets par colonne et insertion dans l'objet row
            for (int i = 0; i < columnCount; i++){
                row[i] = rs.getObject(i+1);
            }
            //Ajout de la ligne dans le tableau
            tableModel.addRow(row);
        }

        //Ajout du model du tableau dans le tableau lui même.
        table.setModel(tableModel);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        Object target = e.getItemSelectable();

        if( target == autocommitCheck ) {
            manager.autoCommit();
        }

        if( e.getStateChange() == ItemEvent.DESELECTED ) {
            commitBtn.setEnabled(false);
            rollbackBtn.setEnabled(false);

        } else if ( e.getStateChange() == ItemEvent.SELECTED ) {
            commitBtn.setEnabled(true);
            rollbackBtn.setEnabled(true);
        }

    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JButton target = (JButton) event.getSource();

        String query = queryArea.getText();
        ResultSet result = null;

        if(target == queryValidationBtn){
            try {
                result = manager.executeStatement(query);
                if (result != null) {
                    resultSetToTableModel(result, table);
                    nbRowsLabel.setText("Number of rows : " + table.getRowCount());
                }
                else nbRowsLabel.setText("Number of rows modified : " + manager.getNbRowsUpdated() );
			}
			catch(SQLException e) {
				System.out.println(e.getMessage());
			}
            finally { 
                // To do after query
                nbRowsLabel.setText(result==null ? "Number of rows modified : " + manager.getNbRowsUpdated() : "Number of rows : " + table.getRowCount());
            }
        } else if (target == rollbackBtn ) {
            manager.rollback();
        } else if (target == commitBtn ) {
            manager.commit();
        } else if (target == resetBtn ) {
            DefaultTableModel modele = (DefaultTableModel)table.getModel(); 
            modele.setRowCount(0); 
            modele.setColumnCount(0);
            table.setModel(modele); 
            table.repaint();
            nbRowsLabel.setText("");
        }
    }


}