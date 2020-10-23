package tp2;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException {
        System.out.println("Bienvenue au deuxieme labo de INF2010!");
        URL path = Main.class.getResource("longFlippedNumber.txt");
        String longFlippedNumber = new String(Files.readAllBytes(Paths.get(path.toURI())), StandardCharsets.UTF_8);
        if (Interview.isValidFlippedWithPermutation(longFlippedNumber)) {
            System.out.println("Bravo! Roulez les tests :)");
        }
        else {
            System.out.println("Zut alors...");
        }
    }

}
