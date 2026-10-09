package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.app;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.swing.SwingUtilities;

/**
 * Shows the main window once the Spring context has started.
 *
 * MainFrame and every DemoPanel are {@code @Lazy} beans, so asking the provider for the frame
 * inside {@code invokeLater} means the whole Swing component tree is built on the Event
 * Dispatch Thread, not on Spring's startup thread.
 *
 * Disabled under the "test" profile so {@code @SpringBootTest} never opens a window.
 */
@Component
@Profile("!test")
public class UiLauncher implements CommandLineRunner {

    private final ObjectProvider<MainFrame> mainFrame;

    public UiLauncher(ObjectProvider<MainFrame> mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void run(String... args) {
        SwingUtilities.invokeLater(() -> mainFrame.getObject().setVisible(true));
    }
}
