package academits.ru.countries;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        try (InputStream inputStream = Main.class.getResourceAsStream("/countries.json")) {
            if (inputStream == null) {
                throw new FileNotFoundException("Файл countries.json не найден");
            }

            List<Country> countries = mapper.readValue(
                    inputStream,
                    new TypeReference<>() {
                    }
            );

            long totalPopulation = countries.stream()
                    .mapToLong(c -> c.population)
                    .sum();

            System.out.println("Суммарная численность населения по странам: " + totalPopulation);

            List<Currency> allCurrencies = countries.stream()
                    .flatMap(c -> c.currencies.stream())
                    .toList();

            System.out.println("Перечень всех валют из файла:");
            allCurrencies.forEach(c -> System.out.println(c.code + " - " + c.name));

            List<Country> bigCountries = countries.stream()
                    .filter(c -> c.population >= 1000000)
                    .toList();

            mapper.writeValue(new File("result.json"), bigCountries);
        }
    }
}
