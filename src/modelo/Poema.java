package modelo;

// Clases de Java para leer el archivo
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

// Excepciones propias del proyecto
import excepciones.Excepciones.ErrorCargaPoemaException;
import excepciones.Excepciones.PalabraMuyCortaException;
import excepciones.Excepciones.PalabraNoEncontradaException;
import excepciones.Excepciones.PosicionPoemaInvalidaException;


public class Poema {

    // Arreglo unidimensional/vector (empieza vacio)
    private String[] palabras = new String[0];

    public void cargar(String nombreArchivo) throws ErrorCargaPoemaException {
        try {
            int total = contarPalabras(nombreArchivo);   // 1. contar
            String[] vector = new String[total];         // 2. arreglo del tamano exacto
            llenarVector(nombreArchivo, vector);         // 3. llenar en orden
            palabras = vector;
        } catch (FileNotFoundException e) {
            throw new ErrorCargaPoemaException("No se pudo cargar el poema: el archivo '"
                    + nombreArchivo + "' no existe.", e);
        } catch (IOException e) {
            throw new ErrorCargaPoemaException("Error leyendo el archivo '" + nombreArchivo + "'.", e);
        }
    }

    // Empieza aquí para leer el archivo, cuenta las palabras, quita los puntos y comas
    private int contarPalabras(String nombreArchivo) throws IOException {
        int cont = 0;
        // UTF-8 para que las tildes en las palabras se lean bien en Windows
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = limpiar(linea);
                if (!linea.isEmpty()) {
                    String[] partes = linea.split("\\s+"); // separa por uno o mas espacios
                    cont = cont + partes.length;
                }
            }
        }
        return cont; // se devuelve una vez al final
    }

    // Segunda lectura para guardar cada palabra en el arreglo en su orden original
    private void llenarVector(String nombreArchivo, String[] vector) throws IOException {
        int pos = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = limpiar(linea);
                if (!linea.isEmpty()) {
                    String[] partes = linea.split("\\s+");
                    for (int i = 0; i < partes.length; i++) {
                        vector[pos] = partes[i];
                        pos++;
                    }
                }
            }
        }
    }

    // Quita los signos de puntuacion y los espacios de los extremos
    private String limpiar(String linea) {
        linea = linea.replace(",", "");
        linea = linea.replace(".", "");
        linea = linea.replace(";", "");
        linea = linea.replace(":", "");
        return linea.trim();
    }


    //trabaja sobre el arreglo
    // PUNTO C ENCADENADA: ArrayIndexOutOfBoundsException - PosicionPoemaInvalidaException
    public String obtenerPalabra(int pos) throws PosicionPoemaInvalidaException {
        try {
            return palabras[pos];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new PosicionPoemaInvalidaException("La posicion " + pos
                    + " no existe. Debe estar entre 0 y " + (palabras.length - 1) + ".", e);
        }
    }

    // PUNTO C ENCADENADA: ArrayIndexOutOfBoundsException - PosicionPoemaInvalidaException
    public void modificarPalabra(int pos, String nueva) throws PosicionPoemaInvalidaException {
        try {
            palabras[pos] = nueva;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new PosicionPoemaInvalidaException("No se puede modificar la posicion " + pos
                    + " porque no existe en el vector.", e);
        }
    }

    // PUNTO B PERSONALIZADA: si la palabra no esta en el arreglo se lanza PalabraNoEncontradaException
    public int buscarPalabra(String buscada) throws PalabraNoEncontradaException {
        for (int i = 0; i < palabras.length; i++) {
            if (palabras[i].equalsIgnoreCase(buscada)) {
                return i;
            }
        }
        throw new PalabraNoEncontradaException("La palabra '" + buscada + "' no se encuentra en el poema.");
    }

    // PUNTO B PERSONALIZADA: si la palabra tiene menos de 4 letras se lanza PalabraMuyCortaException
    public String obtenerPalabraLarga(int pos) throws PosicionPoemaInvalidaException, PalabraMuyCortaException {
        String palabra = obtenerPalabra(pos);
        if (palabra.length() < 4) {
            throw new PalabraMuyCortaException("La palabra '" + palabra + "' (posicion " + pos
                    + ") tiene " + palabra.length() + " letras. Debe tener minimo 4.");
        }
        return palabra;
    }

    public String[] getPalabras() {
        return palabras;
    }
}
