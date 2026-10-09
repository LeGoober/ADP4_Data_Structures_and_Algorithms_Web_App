package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.app;

import javax.swing.Timer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Replays a recorded list of steps, one per timer tick, on the Event Dispatch Thread.
 * The algorithm has already finished running; this only animates what it recorded.
 *
 * @param <S> the step type (CallEvent, SortStep, Probe, Integer, GraphStep, ...)
 */
public class StepPlayer<S> {

    private static final int DEFAULT_DELAY_MS = 300;

    private List<S> steps = new ArrayList<>();
    private int index;
    private final Timer timer;
    private final Consumer<S> onStep;
    private final Runnable onFinished;

    public StepPlayer(Consumer<S> onStep, Runnable onFinished) {
        this.onStep = onStep;
        this.onFinished = onFinished;
        this.timer = new Timer(DEFAULT_DELAY_MS, e -> advance());
    }

    /** Replaces the steps and rewinds to the start without playing. */
    public void load(List<S> newSteps) {
        timer.stop();
        steps = newSteps == null ? new ArrayList<>() : new ArrayList<>(newSteps);
        index = 0;
    }

    public void play() {
        if (!isFinished()) {
            timer.start();
        }
    }

    public void pause() {
        timer.stop();
    }

    /** Pauses and applies exactly one step. */
    public void stepForward() {
        timer.stop();
        advance();
    }

    /** Stops and rewinds. The owning panel restores its own display state. */
    public void reset() {
        timer.stop();
        index = 0;
    }

    public void setDelayMs(int ms) {
        timer.setDelay(ms);
        timer.setInitialDelay(ms);
    }

    public boolean isFinished() {
        return index >= steps.size();
    }

    public boolean isPlaying() {
        return timer.isRunning();
    }

    private void advance() {
        if (isFinished()) {
            timer.stop();
            return;
        }
        onStep.accept(steps.get(index++));
        if (isFinished()) {
            timer.stop();
            if (onFinished != null) {
                onFinished.run();
            }
        }
    }
}
