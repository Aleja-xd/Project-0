import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Project0 {
    public static void main(String[] args) {
        Set<String> variablesDefinidas = new HashSet<>();
        Set<String> macrosDefinidas = new HashSet<>();
        // Variable booleana que da la respuesta de acuerdo al proceso de verificación
        boolean esValido = true; 
        StringBuilder contenido = new StringBuilder();

        // LÍNEA QUE SE MODIFICA PARA INGRESAR LA RUTA DEL DOCUMENTO A BUSCAR Y REVISAR
        File archivo = new File("C:\\Users\\Familia Sanabria\\Documents\\LyM\\code-examples1.txt");
        //Añadido para revisar que este buscando en el lugar correcto
        System.out.println("Buscando archivo en: " + archivo.getAbsolutePath());
        if (!archivo.exists()) {
            System.out.println("El archivo no existe.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();

                // Verifica paréntesis balanceados
                int openParens = 0;
                for (char c : linea.toCharArray()) {
                    if (c == '(') openParens++;
                    else if (c == ')') openParens--;
                    if (openParens < 0) {
                        esValido = false;
                        break;
                    }
                }
                if (openParens != 0) esValido = false;

                // Verifica punto y coma al final de cada instrucción además del código en si
                if (linea.startsWith("NEW VAR") && linea.contains("=")) {
                    String variable = linea.substring(8, linea.indexOf('=')).trim();
                    variablesDefinidas.add(variable);
                    if (!linea.endsWith(";")) {
                        esValido = false;
                        break;
                    }
                } else if (linea.startsWith("NEW MACRO") && linea.contains("(")) {
                    String macro = linea.substring(10, linea.indexOf('(')).trim();
                    macrosDefinidas.add(macro);
                } else if (linea.startsWith("EXEC") && linea.contains("{") && linea.contains("}")) {
                    String contenidoLinea = linea.substring(linea.indexOf('{') + 1, linea.lastIndexOf('}')).trim();
                    contenido.append(contenidoLinea).append(" ");
                    String[] instrucciones = contenidoLinea.split(";");
                    for (String instruccion : instrucciones) {
                        if (!instruccion.trim().isEmpty() && !instruccion.trim().endsWith(";")) {
                            esValido = false;
                            break;
                        }
                    }
                } else if (linea.startsWith("walk") || linea.startsWith("drop") || linea.startsWith("turnToMy") || linea.startsWith("jump") || linea.startsWith("go")) {
                    if (!linea.endsWith(";")) {
                        esValido = false;
                        break;
                    }
                }

                if (!esValido) break;
            }
        } catch (IOException e) {
            System.out.println("Ocurrió un error: " + e.getMessage());
        }

        if (esValido) {
            System.out.println("Sí, es válido");
        } else {
            System.out.println("No es válido");
        }
    }
}