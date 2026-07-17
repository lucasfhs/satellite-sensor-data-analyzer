package com.aerospace.gui3d.telemetry;

import com.aerospace.gui3d.jpa.Dados;

public interface TelemetrySource extends AutoCloseable {

    Dados readLatest() throws Exception;

    @Override
    default void close() throws Exception {
    }
}
