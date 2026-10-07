package com.arigs.rms.background;

/**
 * Pluggable background job handler.
 */
public interface BackgroundJobHandler {

    String handlerName();

    void execute();
}
