# Satellite Sensor Data Analyzer

Aplicação desktop JavaFX para visualizar telemetria de um CubeSat, incluindo sensores, gráficos em tempo real e um modelo 3D animado.

## O que mudou

- migração de Java 11 para **Java 21 LTS** e JavaFX 21;
- modo demonstração sem banco de dados;
- modo PostgreSQL sem credenciais gravadas no projeto;
- carregamento dos recursos 3D compatível com JAR/EXE;
- pipeline GitHub Actions para gerar instalador `.exe` autocontido para Windows x64.

## Executar a demonstração

Pré-requisitos para desenvolvimento: JDK 21 e Maven 3.9+.

```powershell
cd gui3d
mvn clean javafx:run
```

Na tela inicial, clique em **INICIAR DEMONSTRAÇÃO**. A mesma interface de telemetria será aberta com dados locais plausíveis, atualização do gráfico e rotação do modelo 3D. Nenhum PostgreSQL é necessário.

Para abrir diretamente nesse modo, passe o argumento `--demo` ao launcher.

## Usar PostgreSQL

Restaure `databaseSample.sql` em uma base PostgreSQL e configure as variáveis antes de iniciar:

```powershell
$env:SATELLITE_DB_URL = "jdbc:postgresql://localhost:5432/tccaerospace"
$env:SATELLITE_DB_USER = "postgres"
$env:SATELLITE_DB_PASSWORD = "sua-senha"
cd gui3d
mvn clean javafx:run
```

Depois clique em **CONECTAR AO POSTGRESQL**. Também podem ser usadas as propriedades Java `satellite.db.url`, `satellite.db.user` e `satellite.db.password`.

## Gerar o instalador Win64

O workflow [windows-exe.yml](.github/workflows/windows-exe.yml) roda em `windows-latest`, compila com JDK 21 x64 e usa `jpackage`/WiX para criar um instalador que já inclui o runtime Java.

Ele é executado em pushes para `main`/`master`, tags `v*`, pull requests e manualmente por **Actions > Build Windows x64 EXE > Run workflow**. Ao terminar, baixe o artefato `SatelliteSensorAnalyzer-windows-x64`.

O instalador também pode ser gerado localmente no Windows depois de `mvn clean verify`, usando o mesmo comando `jpackage` presente no workflow.

## Publicar um GitHub Release

Todo push de uma tag iniciada por `v` (por exemplo, `v2.0.0`) compila o instalador Win64, cria um GitHub Release com notas geradas automaticamente e anexa o arquivo `.exe` ao release.

```powershell
git tag v2.0.0
git push origin v2.0.0
```

Pushes e pull requests para `main`/`master`, assim como execuções manuais, continuam gerando somente o artefato temporário da execução. Isso permite validar o instalador sem publicar um novo release.
