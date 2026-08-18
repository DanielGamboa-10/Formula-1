package com.formula1.data;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestor centralizado de persistencia en disco para el DataStore de Fórmula 1.
 * Implementa serialización Java SE segura, atómica y con soporte de respaldo.
 */
public class DataPersistenceManager {
    public static final String DEFAULT_DATA_DIR = "data";
    public static final String DEFAULT_FILE_NAME = "f1_datastore.dat";
    public static final String DEFAULT_FILE_PATH = DEFAULT_DATA_DIR + File.separator + DEFAULT_FILE_NAME;

    private static String activeFilePath = DEFAULT_FILE_PATH;

    /**
     * Define una ruta personalizada para el archivo de almacenamiento.
     */
    public static void setRutaArchivo(String path) {
        if (path != null && !path.trim().isEmpty()) {
            activeFilePath = path;
        }
    }

    public static String getRutaArchivo() {
        return activeFilePath;
    }

    public static File getArchivoPersistencia() {
        return new File(activeFilePath);
    }

    public static boolean existeArchivoPersistencia() {
        File file = getArchivoPersistencia();
        return file.exists() && file.isFile() && file.length() > 0;
    }

    /**
     * Guarda atómicamente el estado actual del DataStore en el archivo predeterminado.
     */
    public static synchronized boolean guardar(DataStore store) {
        return guardarEnRuta(store, activeFilePath);
    }

    /**
     * Guarda el estado del DataStore en una ruta específica.
     */
    public static synchronized boolean guardarEnRuta(DataStore store, String filePath) {
        if (store == null || filePath == null) {
            return false;
        }

        File targetFile = new File(filePath);
        File parentDir = targetFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created && !parentDir.exists()) {
                System.err.println("❌ Error: No se pudo crear el directorio de persistencia: " + parentDir.getAbsolutePath());
                return false;
            }
        }

        File tempFile = new File(filePath + ".tmp");

        try {
            DataSnapshot snapshot = new DataSnapshot(
                    store.getPilotos(),
                    store.getEquipos(),
                    store.getCircuitos(),
                    store.getVehiculos(),
                    store.getConfiguraciones(),
                    store.getHistorialClasificaciones()
            );

            try (FileOutputStream fos = new FileOutputStream(tempFile);
                 BufferedOutputStream bos = new BufferedOutputStream(fos);
                 ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                oos.writeObject(snapshot);
                oos.flush();
            }

            // Reemplazo atómico seguro del archivo final
            Path source = tempFile.toPath();
            Path target = targetFile.toPath();
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);

            return true;
        } catch (Exception e) {
            // Si el movimiento atómico falla, intentar reemplazo estándar
            try {
                if (tempFile.exists()) {
                    Files.move(tempFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    return true;
                }
            } catch (Exception ignored) {}

            System.err.println("❌ Error guardando datos en disco: " + e.getMessage());
            if (tempFile.exists()) {
                tempFile.delete();
            }
            return false;
        }
    }

    /**
     * Carga los datos desde el archivo persistente hacia el DataStore.
     */
    public static synchronized boolean cargar(DataStore store) {
        return cargarDesdeRuta(store, activeFilePath);
    }

    /**
     * Carga los datos desde una ruta específica.
     */
    public static synchronized boolean cargarDesdeRuta(DataStore store, String filePath) {
        if (store == null || filePath == null) {
            return false;
        }

        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            return false;
        }

        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis);
             ObjectInputStream ois = new ObjectInputStream(bis)) {

            Object obj = ois.readObject();
            if (obj instanceof DataSnapshot) {
                DataSnapshot snapshot = (DataSnapshot) obj;

                // Actualizar las colecciones del DataStore
                store.getPilotos().clear();
                if (snapshot.getPilotos() != null) {
                    store.getPilotos().putAll(snapshot.getPilotos());
                }

                store.getEquipos().clear();
                if (snapshot.getEquipos() != null) {
                    store.getEquipos().putAll(snapshot.getEquipos());
                }

                store.getCircuitos().clear();
                if (snapshot.getCircuitos() != null) {
                    store.getCircuitos().putAll(snapshot.getCircuitos());
                }

                store.getVehiculos().clear();
                if (snapshot.getVehiculos() != null) {
                    store.getVehiculos().putAll(snapshot.getVehiculos());
                }

                store.getConfiguraciones().clear();
                if (snapshot.getConfiguraciones() != null) {
                    store.getConfiguraciones().putAll(snapshot.getConfiguraciones());
                }

                store.getHistorialClasificaciones().clear();
                if (snapshot.getHistorialClasificaciones() != null) {
                    store.getHistorialClasificaciones().addAll(snapshot.getHistorialClasificaciones());
                }

                return true;
            } else {
                System.err.println("⚠️ El formato del archivo de persistencia no es compatible.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("❌ Error al cargar datos persistidos: " + e.getMessage());
            return false;
        }
    }

    /**
     * Restaura los datos originales de fábrica (DataLoader) y actualiza el archivo persistente.
     */
    public static synchronized boolean restaurarPorDefecto(DataStore store) {
        if (store == null) return false;

        store.getPilotos().clear();
        store.getEquipos().clear();
        store.getCircuitos().clear();
        store.getVehiculos().clear();
        store.getConfiguraciones().clear();
        store.getHistorialClasificaciones().clear();

        DataLoader.cargarDatosIniciales();
        return guardar(store);
    }

    /**
     * Elimina el archivo persistido del disco.
     */
    public static synchronized boolean eliminarArchivoPersistencia() {
        File file = getArchivoPersistencia();
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }

    /**
     * Retorna estadísticas y metadatos de persistencia formateados.
     */
    public static Map<String, String> obtenerMetadatos() {
        Map<String, String> meta = new HashMap<>();
        File file = getArchivoPersistencia();

        meta.put("ruta", file.getAbsolutePath());
        meta.put("existe", String.valueOf(file.exists()));

        if (file.exists()) {
            long bytes = file.length();
            String tamano = bytes < 1024 ? bytes + " B" : String.format("%.2f KB", bytes / 1024.0);
            meta.put("tamano", tamano);

            LocalDateTime fechaMod = LocalDateTime.ofInstant(
                    java.time.Instant.ofEpochMilli(file.lastModified()),
                    java.time.ZoneId.systemDefault()
            );
            meta.put("ultimaModificacion", fechaMod.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        } else {
            meta.put("tamano", "0 B");
            meta.put("ultimaModificacion", "Sin archivo guardado");
        }

        DataStore store = DataStore.getInstance();
        meta.put("pilotos", String.valueOf(store.getPilotos().size()));
        meta.put("equipos", String.valueOf(store.getEquipos().size()));
        meta.put("circuitos", String.valueOf(store.getCircuitos().size()));
        meta.put("vehiculos", String.valueOf(store.getVehiculos().size()));
        meta.put("configuraciones", String.valueOf(store.getConfiguraciones().size()));
        meta.put("historial", String.valueOf(store.getHistorialClasificaciones().size()));

        return meta;
    }
}
