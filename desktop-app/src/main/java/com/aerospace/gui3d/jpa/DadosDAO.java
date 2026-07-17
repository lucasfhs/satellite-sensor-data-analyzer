package com.aerospace.gui3d.jpa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Lightweight JDBC access used by the connected application mode. */
public final class DadosDAO {

    private static final String LATEST_SQL = "SELECT * FROM dados ORDER BY id DESC LIMIT 1";
    private final String url;
    private final String user;
    private final String password;

    public DadosDAO() {
        this.url = setting("SATELLITE_DB_URL", "satellite.db.url", "jdbc:postgresql://localhost:5432/tccaerospace");
        this.user = setting("SATELLITE_DB_USER", "satellite.db.user", "postgres");
        this.password = setting("SATELLITE_DB_PASSWORD", "satellite.db.password", "");
        DriverManager.setLoginTimeout(5);
    }

    public Dados buscarDadoMaisRecente() throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(LATEST_SQL);
             ResultSet result = statement.executeQuery()) {
            return result.next() ? map(result) : null;
        }
    }

    public void fechar() {
        // Connections are short-lived and closed after every read.
    }

    private static Dados map(ResultSet result) throws SQLException {
        Dados data = new Dados();
        data.setId(result.getInt("id"));
        data.setCubesat(result.getInt("cubesat"));
        data.setAcelerometroX(result.getFloat("acelerometrox"));
        data.setAcelerometroY(result.getFloat("acelerometroy"));
        data.setAcelerometroZ(result.getFloat("acelerometroz"));
        data.setAnguloX(result.getFloat("angulox"));
        data.setAnguloY(result.getFloat("anguloy"));
        data.setAnguloZ(result.getFloat("anguloz"));
        data.setAltitude(result.getFloat("altitude"));
        data.setBateria(result.getFloat("bateria"));
        data.setCorrenteBateria(result.getFloat("correntebateria"));
        data.setCorrentePlacaSolar(result.getFloat("correnteplacasolar"));
        data.setGas1(result.getFloat("gas1"));
        data.setGas2(result.getFloat("gas2"));
        data.setLuz1(result.getFloat("luz1"));
        data.setLuz2(result.getFloat("luz2"));
        data.setPontoOrvalho(result.getFloat("pontoorvalho"));
        data.setPressao(result.getFloat("pressao"));
        data.setSensorUV(result.getFloat("sensoruv"));
        data.setTemperaturaExterna(result.getFloat("temperaturaexterna"));
        data.setTemperaturaInterna(result.getFloat("temperaturainterna"));
        data.setTensaoBateria(result.getFloat("tensaobateria"));
        data.setTensaoPlacaSolar(result.getFloat("tensaoplacasolar"));
        data.setUmidade(result.getFloat("umidade"));
        data.setVelocidade(result.getFloat("velocidade"));
        data.setVelocidadeAngularX(result.getFloat("velocidadeangularx"));
        data.setVelocidadeAngularY(result.getFloat("velocidadeangulary"));
        data.setVelocidadeAngularZ(result.getFloat("velocidadeangularz"));
        data.setDataObtencao(result.getString("dataobtencao"));
        return data;
    }

    private static String setting(String environmentName, String propertyName, String defaultValue) {
        String property = System.getProperty(propertyName);
        if (property != null && !property.isBlank()) {
            return property;
        }
        String environment = System.getenv(environmentName);
        return environment == null || environment.isBlank() ? defaultValue : environment;
    }
}
