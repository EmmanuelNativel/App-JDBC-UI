package jdbcUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * CustomTextField
 * 
 * Classe permettant de générer des text fields personnalisés
 * Possibilité d'ajouter un TextField
 * 
 */

public class CustomTextField extends JTextField {

    CustomTextField(){
        super();

        // dimensions
        setMinimumSize(new Dimension(250, 30));
        setPreferredSize(new Dimension(250, 30));
        setMaximumSize(new Dimension(250, 30));

        setHorizontalAlignment(JTextField.CENTER);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));

        setOpaque(false);
    }


    // Ajout d'un placholder au textField
    // Le placeholder disparait quand on clique sur le text field et réapparait quand le focus est perdu si le champs est vide
    public CustomTextField setPlaceholder(String placeholder){
        setText(placeholder);
        setForeground(Color.GRAY);

        addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                if(getText().equals(placeholder)) {
                    setText("");
                    setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if( getText().isEmpty() ) {
                    setForeground(Color.GRAY);
                    setText(placeholder);
                }
            }

        });

        return this;
    }
}