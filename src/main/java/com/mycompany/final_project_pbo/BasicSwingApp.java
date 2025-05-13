/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author c0delb08
 */
public class BasicSwingApp {
    
    public static void main(String[] args) {
        // Create the main application window (JFrame)
        JFrame frame = new JFrame("Basic Swing Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new BorderLayout());

        // Create UI components
        JLabel label = new JLabel("Enter product code:");
        JTextField textField = new JTextField();
        JButton button = new JButton("Submit");
        JTextArea output = new JTextArea();
        output.setEditable(false);

        // Add action listener to button
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = textField.getText();
                output.setText("You entered: " + input);
            }
        });

        // Organize components in layout
        JPanel inputPanel = new JPanel(new GridLayout(2, 1));
        inputPanel.add(label);
        inputPanel.add(textField);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(button, BorderLayout.CENTER);
        frame.add(new JScrollPane(output), BorderLayout.SOUTH);

        // Make the window visible
        frame.setVisible(true);
    }

}
