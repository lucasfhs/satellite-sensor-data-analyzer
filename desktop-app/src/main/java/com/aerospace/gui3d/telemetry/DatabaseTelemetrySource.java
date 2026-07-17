package com.aerospace.gui3d.telemetry;

import com.aerospace.gui3d.jpa.Dados;
import com.aerospace.gui3d.jpa.DadosDAO;

public final class DatabaseTelemetrySource implements TelemetrySource {

    private final DadosDAO dao = new DadosDAO();

    @Override
    public Dados readLatest() throws Exception {
        return dao.buscarDadoMaisRecente();
    }

    @Override
    public void close() {
        dao.fechar();
    }
}
