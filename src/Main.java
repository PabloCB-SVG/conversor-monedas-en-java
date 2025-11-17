import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class Main {

    private static final String API_KEY = System.getenv("EXCHANGE_API_KEY");
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n=== Conversor de Monedas ===");
            System.out.println("1. Convertir USD a ARS");
            System.out.println("2. Convertir USD a DOP");
            System.out.println("3. Convertir EUR a MXN");
            System.out.println("4. Salir");
            System.out.print("Elige una opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    convertir("USD", "ARS", 25);
                    break;
                case 2:
                    convertir("USD", "DOP", 64.4);
                    break;
                case 3:
                    convertir("EUR", "MXN", 100);
                    break;
                case 4:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 4);

        scanner.close();
    }

    private static void convertir(String monedaBase, String monedaDestino, double cantidad) {
        try {
            String urlStr = BASE_URL + API_KEY + "/latest/" + monedaBase;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder respuesta = new StringBuilder();
            String linea;
            while ((linea = reader.readLine()) != null) {
                respuesta.append(linea);
            }
            reader.close();

            JSONObject json = new JSONObject(respuesta.toString());
            double tasa = json.getJSONObject("conversion_rates").getDouble(monedaDestino);

            double resultado = cantidad * tasa;
            System.out.printf("%.2f %s = %.2f %s%n", cantidad, monedaBase, resultado, monedaDestino);

        } catch (Exception e) {
            System.out.println("Error al convertir: " + e.getMessage());
        }
    }
}
