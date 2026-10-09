package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.app;

import com.data_structs_and_algos.makgana_rorisang.dsa_assignment.DsaAssignmentApplication;
import com.formdev.flatlaf.FlatLightLaf;
import org.springframework.boot.SpringApplication;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Entry point. Sets the FlatLaf look and feel before any Swing component exists, then starts
 * the Spring context. {@link UiLauncher} opens the window once the context is ready.
 */
public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("FlatLaf unavailable, falling back to the default look and feel: " + e);
        }
        SpringApplication.run(DsaAssignmentApplication.class, args);
    }
}
