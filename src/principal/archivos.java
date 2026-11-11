package principal;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.InputMismatchException;
import java.util.Scanner;
// llama el modelo
import modelo.Poema;

// excepciones propias del proyecto
import excepciones.Excepciones.ErrorCargaPoemaException;
import excepciones.Excepciones.PalabraMuyCortaException;
import excepciones.Excepciones.PalabraNoEncontradaException;
import excepciones.Excepciones.PosicionPoemaInvalidaException;


//este es el archivo del menu por consola

public class archivos {
    public static void main(String[] args) {

        String nombreArchivo = "./poema.txt";
        Scanner leer = new Scanner(System.in);
        Poema poema = new Poema();

        // Carga inicial del poema
        try {
            poema.cargar(nombreArchivo);
            System.out.println("Poema cargado. Total de palabras: " + poema.getPalabras().length);
        } catch (ErrorCargaPoemaException e) {
            System.out.println("ERROR: " + e.getMessage());
            System.out.println("Causa real: " + e.getCause());
        }

        //menu
        int op = -1;

        while (op != 0) {
            System.out.println("\n=== MENU POEMA ===");
            System.out.println("1. Mostrar el poema y el vector de palabras");
            System.out.println("2. Mostrar palabra por posicion ");
            System.out.println("3. Abrir un archivo por su nombre");
            System.out.println("4. Buscar una palabra");
            System.out.println("5. Mostrar palabra de 4 letras o mas ");
            System.out.println("6. Cargar poema desde otro archivo");
            System.out.println("7. Modificar palabra de una posicion");
            System.out.println("0. Salir");
            System.out.print("Elija una opcion: ");

            // Si escribe letras en el menu, atrapa el error
            try {
                op = leer.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Debe escribir un numero del menu.");
                op = -1;
            }
            leer.nextLine(); // limpiar el buffer del Scanner

            switch (op) {

                case 1: // Mostrar contenido del archivo y el arreglo
                    System.out.println("\n- Contenido del Archivo -");
                    try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo, StandardCharsets.UTF_8))) {
                        String linea;
                        while ((linea = br.readLine()) != null) {
                            System.out.println(linea);
                        }
                    } catch (IOException e) {
                        System.out.println("Error al leer: " + e.getMessage());
                    }

                    String[] palabras = poema.getPalabras();
                    System.out.println("\n--- Arreglo de palabras (" + palabras.length + ") ---");
                    for (int i = 0; i < palabras.length; i++) {
                        System.out.println("[" + i + "] " + palabras[i]);
                    }
                    break;

                case 2: // PUNTO A: consultar al arreglo
                    try {
                        System.out.print("Digite la posicion (0 a " + (poema.getPalabras().length - 1) + "): ");
                        int pos = leer.nextInt();


                        System.out.println("Palabra en la posicion " + pos + ": " + poema.getPalabras()[pos]);

                    } catch (ArrayIndexOutOfBoundsException e) {
                        // si la posicion no existe, Java lanza la excepcion ArrayIndexOutOfBoundsException
                        System.out.println("Error en el sistema: posicion fuera del rango -> " + e.getMessage());

                    } catch (InputMismatchException e) {
                        //si se ingresa texto, lanza la excepcion InputMismatchException
                        System.out.println("Error en el sistema: debe escribir un numero, no texto.");

                    } finally {
                        leer.nextLine();
                        System.out.println("Fin de la consulta por posicion.");
                    }
                    break;

                case 3: // PUNTO A: al leer el archivo
                    System.out.print("Escriba el nombre del archivo: ");
                    String nombre = leer.nextLine();
                    FileReader fr = null;
                    try {
                        fr = new FileReader(nombre); // si no existe, Java lanza FileNotFoundException
                        System.out.println("El archivo '" + nombre + "' existe y se abrio correctamente.");
                    } catch (FileNotFoundException e) {
                        System.out.println("ERROR (sistema): no se encontro el archivo -> " + e.getMessage());
                    } finally {
                        try {
                            if (fr != null) {
                                fr.close();
                            }
                        } catch (IOException e) {
                            System.out.println("No se pudo cerrar el archivo.");
                        }
                        System.out.println("Fin del intento de apertura.");
                    }
                    break;

                case 4: // PUNTO B: personalizada PalabraNoEncontradaException
                    System.out.print("Escriba la palabra a buscar: ");
                    String buscada = leer.nextLine().trim();
                    try {
                        int pos = poema.buscarPalabra(buscada);
                        System.out.println("La palabra '" + buscada + "' esta en la posicion " + pos + ".");
                    } catch (PalabraNoEncontradaException e) {
                        System.out.println("ERROR : " + e.getMessage());
                    } finally {
                        System.out.println("Fin de la busqueda.");
                    }
                    break;

                case 5: // PUNTO B: personalizada PalabraMuyCortaException
                    try {
                        System.out.print("Digite la posicion de la palabra: ");
                        int pos = leer.nextInt();
                        String palabra = poema.obtenerPalabraLarga(pos);
                        System.out.println("Palabra valida: " + palabra + " (" + palabra.length() + " letras)");
                    } catch (PalabraMuyCortaException e) {
                        System.out.println("ERROR : " + e.getMessage());
                    } catch (PosicionPoemaInvalidaException e) {
                        System.out.println("ERROR: " + e.getMessage());
                    } catch (InputMismatchException e) {
                        System.out.println("ERROR: debe escribir un numero.");
                    } finally {
                        leer.nextLine();
                        System.out.println("Fin de la validacion de longitud.");
                    }
                    break;

                case 6: // PUNTO C: Encadenada ErrorCargaPoemaException (por: FileNotFoundException)
                    System.out.print("Escriba el nombre del archivo del poema: ");
                    String otroArchivo = leer.nextLine();
                    try {
                        poema.cargar(otroArchivo);
                        nombreArchivo = otroArchivo;
                        System.out.println("Poema cargado. Total de palabras: " + poema.getPalabras().length);
                    } catch (ErrorCargaPoemaException e) {
                        System.out.println("ERROR (encadenada): " + e.getMessage());
                        // getCause() devuelve la excepcion original del sistema
                        System.out.println("Causa original: " + e.getCause().getClass().getSimpleName()
                                + " -> " + e.getCause().getMessage());
                    } finally {
                        System.out.println("Fin de la carga del poema.");
                    }
                    break;

                case 7: // PUNTO C: Encadenada PosicionPoemaInvalidaException (por: ArrayIndexOutOfBoundsException)
                    try {
                        System.out.print("Digite la posicion a modificar: ");
                        int pos = leer.nextInt();
                        leer.nextLine();
                        System.out.print("Escriba la nueva palabra: ");
                        String nueva = leer.nextLine().trim();
                        poema.modificarPalabra(pos, nueva);
                        System.out.println("Posicion " + pos + " modificada. Ahora dice: " + poema.obtenerPalabra(pos));
                    } catch (PosicionPoemaInvalidaException e) {
                        System.out.println("ERROR (encadenada): " + e.getMessage());
                        System.out.println("Causa original: " + e.getCause().getClass().getSimpleName()
                                + " -> " + e.getCause().getMessage());
                    } catch (InputMismatchException e) {
                        System.out.println("ERROR (sistema): debe escribir un numero.");
                        leer.nextLine();
                    } finally {
                        System.out.println("Fin de la modificacion.");
                    }
                    break;

                case 0:
                    System.out.println("Fin del programa.");
                    break;

                default:
                    System.out.println("Opcion no valida.");
            }
        }
        leer.close();
    }
}
