# Argumente für das Programm

Das Programm akzeptiert verschiedene Befehlszeilenargumente für die Konfiguration. Hier sind die unterstützten Argumente und ihre Verwendung:

1. **TOTAL_ITERATIONS=\<Anzahl\>**
    - Beschreibung: Legt die Gesamtanzahl der Iterationen fest.
    - Beispiel: `TOTAL_ITERATIONS=1000`

2. **WHALE_POPULATION=\<Anzahl\>**
    - Beschreibung: Legt die Population von Walen für den Algorithmus fest.
    - Beispiel: `WHALE_POPULATION=50`

3. **GENERATE_HEURISTIC=\<Verzeichnispfad\>**
    - Beschreibung: Legt den Verzeichnispfad zu den zu generierenden Heuristiken fest.
    - Beispiel: `GENERATE_HEURISTIC=/pfad/zum/verzeichnis`

4. **DYNAMIC_ITERATIONS=true**
    - Beschreibung: Aktiviert die dynamische Anpassung der Iterationen.
    - Beispiel: `DYNAMIC_ITERATIONS=true`

## Hinweise zur Verwendung

- Die Argumente sind case-insensitive, d.h., `Total_Iterations=1000` wird genauso akzeptiert wie `TOTAL_ITERATIONS=1000`.

## Beispielverwendung

### Java
```bash
java WOA.jar src/main/resources/tsp/a280.tsp TOTAL_ITERATIONS=1000 WHALE_POPULATION=50 DYNAMIC_ITERATIONS=true
```
### Docker
`docker build -t woa .`

`docker run -v $(pwd)/heuristic.csv:/heuristic.csv -v $(pwd)/src/main/resources/tsp:/tsp -v $(pwd)/src/main/resources/sop:/sop woa`

### docker-compose.yml
```
version: "3.0"
services:
  worker:
    build: .
    # Examples
    command: ["/tsp/a280.tsp", "TOTAL_ITERATIONS=50"]
#    command: ["GENERATE_HEURISTIC=/tsp"]
    volumes:
      - ./heuristic.csv:/heuristic.csv
      - ./src/main/resources/tsp:/tsp
      - ./src/main/resources/sop:/sop
```