# Satellite Sensor Data Analyzer

A desktop application for visualizing CubeSat telemetry through real-time sensor charts and an animated 3D model. Built with Java 21, JavaFX, and PostgreSQL support, it can also run entirely offline in demonstration mode.

The project was originally developed as part of the CEFET-MG aerospace extension program to make satellite sensor behavior easier to inspect during demonstrations and CubeSat experiments.

[Download the latest Windows x64 installer](https://github.com/lucasfhs/satellite-sensor-data-analyzer/releases/latest)

## Preview

![Satellite Sensor Data Analyzer preview](desktop-app/preview.gif)

## Features

- Real-time CubeSat telemetry visualization
- Interactive charts for sensor data
- Animated 3D CubeSat model
- Offline demonstration mode with realistic simulated telemetry
- Optional PostgreSQL integration for stored telemetry
- Self-contained Windows x64 installer with the Java runtime included
- Automated builds and GitHub Releases through GitHub Actions

## Download the Windows application

You do not need to install Java or Maven to use the released application.

1. Open the [latest GitHub Release](https://github.com/lucasfhs/satellite-sensor-data-analyzer/releases/latest).
2. Expand the **Assets** section if it is collapsed.
3. Download the `SatelliteSensorAnalyzer-*.exe` file.
4. Run the installer and follow the Windows installation steps.
5. Open **Satellite Sensor Analyzer** from the Start menu or desktop shortcut.

On the welcome screen, select **INICIAR DEMONSTRAÇÃO** to explore the application without a database, or **CONECTAR AO POSTGRESQL** to use a configured PostgreSQL instance.

## Run from source

### Requirements

- JDK 21
- Maven 3.9 or newer

### Demonstration mode

```powershell
cd desktop-app
mvn clean javafx:run
```

Select **INICIAR DEMONSTRAÇÃO** on the welcome screen. The application will display locally generated telemetry, update the charts, and animate the 3D model without requiring PostgreSQL.

The application can also be launched directly in demonstration mode by passing the `--demo` argument to the launcher.

### PostgreSQL mode

Restore [`database/sample-database.sql`](database/sample-database.sql) into a PostgreSQL database, then configure the connection before starting the application:

```powershell
$env:SATELLITE_DB_URL = "jdbc:postgresql://localhost:5432/tccaerospace"
$env:SATELLITE_DB_USER = "postgres"
$env:SATELLITE_DB_PASSWORD = "your-password"

cd desktop-app
mvn clean javafx:run
```

Select **CONECTAR AO POSTGRESQL** on the welcome screen. The same values can be provided through the Java properties `satellite.db.url`, `satellite.db.user`, and `satellite.db.password`.

## Windows x64 builds and releases

The [GitHub Actions workflow](.github/workflows/windows-exe.yml) builds the project on `windows-latest` with JDK 21 x64 and uses `jpackage` and WiX to create a self-contained `.exe` installer.

Pushes and pull requests targeting `main` or `master`, as well as manual workflow runs, produce a temporary build artifact for validation. Pushing a tag that starts with `v` creates a GitHub Release and attaches the Windows installer automatically:

```powershell
git tag v2.0.0
git push origin v2.0.0
```

## Author

Created and maintained by [lucashfsdev](https://github.com/lucasfhs).
