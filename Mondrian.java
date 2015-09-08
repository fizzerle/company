package com.company;

import java.awt.*;
import javax.swing.*;

import static javax.swing.JOptionPane.showInternalOptionDialog;

public class Mondrian extends JFrame {

    public Mondrian() {
        Board board = new Board();
        add(board);

        setResizable(false);
        pack();

        setTitle("Mondrian");

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Object[] options = {"Leicht", "Mittel","Schwierig"};
        int answer = JOptionPane.showOptionDialog(this, "Bitte waehle den Schwierigkeitsgrad aus! ", "Schwierigkeitsgrad", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,options[1]);

        if(answer == 0) board.setdifficulty(80);
        if(answer == 1) board.setdifficulty(50);
        if(answer == 2) board.setdifficulty(10);
   }

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                Mondrian window = new Mondrian();
                window.setLocationRelativeTo(null);
                window.setVisible(true);
            }
        });
    }

}
