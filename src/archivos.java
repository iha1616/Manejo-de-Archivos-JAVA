import java.io.*;
import java.util.Scanner;

public class archivos {
    public static void main(String[] args) {

        String nombreArchivo = "./poema.txt";
        Scanner leer = new Scanner(System.in);

        System.out.println("\n---Contenido del archivo con Filewriter---");
        try(FileReader fr = new FileReader("./poema.txt")){
            int i;
            int cont = 0;
            while ((i = fr.read()) != -1){
                cont += 1;
                System.out.println(cont);

            }
        }catch (IOException e){
            e.printStackTrace();
        }

        System.out.println("\n--- Contenido del Archivo---");
        try(BufferedReader br = new BufferedReader(new FileReader("./poema.txt"))){
            String linea;
            while ((linea = br.readLine()) !=null){
                System.out.println(linea);
            }
        }catch (IOException e) {
            System.out.println("Error al leer: " + e.getMessage());
        }

    }
}