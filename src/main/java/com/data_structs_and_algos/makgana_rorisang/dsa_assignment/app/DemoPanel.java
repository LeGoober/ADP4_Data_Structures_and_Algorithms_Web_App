package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.app;

import javax.swing.JPanel;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.function.Consumer;

/**
 * Base class for every demo. The panel itself is the canvas; {@link #buildControls()} supplies the
 * panel-specific inputs that MainFrame puts in the shared control strip next to Play / Pause /
 * Step / Reset.
 *
 * Beyond the UML contract (getTitle, getPartLabel, buildControls, onShow, onHide) this class adds
 * a little app-shell plumbing: playback hooks for the shared strip, a status message channel, and
 * helpers that turn an unimplemented algorithm stub into a friendly message instead of a crash.
 */
public abstract class DemoPanel extends JPanel {

    /** Property fired by {@link #showStatus(String)}; MainFrame shows it in the control strip. */
    public static final String STATUS_PROPERTY = "demoStatus";

    protected DemoPanel() {
        super(new BorderLayout());
    }

    public abstract String getTitle();

    public abstract String getPartLabel();

    protected abstract JPanel buildControls();

    /** Called when this panel becomes the visible demo. */
    public void onShow() {
    }

    /** Called when the user switches away; stops any running animation. */
    public void onHide() {
        pause();
    }

    // ---- Playback hooks used by the shared control strip -------------------------------------

    /** The panel's StepPlayer, or null when the demo has nothing to replay. */
    protected StepPlayer<?> getPlayer() {
        return null;
    }

    public boolean hasPlayback() {
        return getPlayer() != null;
    }

    public void play() {
        if (getPlayer() != null) getPlayer().play();
    }

    public void pause() {
        if (getPlayer() != null) getPlayer().pause();
    }

    public void stepForward() {
        if (getPlayer() != null) getPlayer().stepForward();
    }

    /** Rewinds the replay. Subclasses override to also restore what they draw. */
    public void reset() {
        if (getPlayer() != null) getPlayer().reset();
        repaint();
    }

    public void setDelayMs(int ms) {
        if (getPlayer() != null) getPlayer().setDelayMs(ms);
    }

    // ---- Helpers -----------------------------------------------------------------------------

    protected void showStatus(String message) {
        firePropertyChange(STATUS_PROPERTY, null, message);
    }

    /**
     * Runs an action that calls into algorithm code. A stub that still throws
     * UnsupportedOperationException is reported in the status bar instead of crashing the UI.
     *
     * @return true if the action completed
     */
    protected boolean attempt(String what, Runnable action) {
        try {
            action.run();
            return true;
        } catch (UnsupportedOperationException e) {
            showStatus(what + ": not implemented yet (" + e.getMessage() + ")");
        } catch (RuntimeException e) {
            showStatus(what + " failed: " + e);
        }
        repaint();
        return false;
    }

    /** Antialiased painting that shows a message in place of the drawing if algorithm code throws. */
    protected void paintSafely(Graphics g, Consumer<Graphics2D> painter) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            painter.accept(g2);
        } catch (UnsupportedOperationException e) {
            paintMessage(g2, "Not implemented yet: " + e.getMessage());
        } catch (RuntimeException e) {
            paintMessage(g2, "Cannot draw yet: " + e);
        } finally {
            g2.dispose();
        }
    }

    protected void paintMessage(Graphics2D g, String message) {
        g.setColor(UIManager.getColor("Label.disabledForeground"));
        FontMetrics fm = g.getFontMetrics();
        int x = Math.max(12, (getWidth() - fm.stringWidth(message)) / 2);
        g.drawString(message, x, getHeight() / 2);
    }

    protected static Color uiColor(String key, Color fallback) {
        Color c = UIManager.getColor(key);
        return c != null ? c : fallback;
    }
}
