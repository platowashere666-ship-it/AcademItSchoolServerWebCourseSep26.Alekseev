package academits.ru.countries;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        try (InputStream inputStream = Main.class.getResourceAsStream("/countries.json")) {
            if (inputStream == null) {
                System.out.println("Файл countries.json не найден");
                return;
            }

            ObjectMapper mapper = new ObjectMapper();

            List<Country> countries = mapper.readValue(
                    inputStream,
                    new TypeReference<>() {
                    }
            );

            long totalPopulation = countries.stream()
                    .mapToLong(Country::getPopulation)
                    .sum();

            System.out.println("Суммарная численность населения по странам: " + totalPopulation);

            Map<String, String> uniqueCurrencies = new TreeMap<>();

            for (Country country : countries) {
                List<Currency> currencies = country.getCurrencies();

                if (currencies == null) {
                    continue;
                }

                for (Currency currency : currencies) {
                    String code = currency.getCode();
                    String name = currency.getName();

                    if (code == null || code.isBlank() || code.equals("(none)") || name == null || name.isBlank()) {
                        continue;
                    }

                    uniqueCurrencies.putIfAbsent(code, name);
                }
            }

            System.out.println("Перечень уникальных валют из файла:");
            uniqueCurrencies.forEach((code, name) ->
                    System.out.println(code + " - " + name));

            List<Country> bigCountries = countries.stream()
                    .filter(c -> c.getPopulation() >= 1000000)
                    .toList();

            mapper.writeValue(new File("result.json"), bigCountries);
        } catch (IOException e) {
            System.out.println("Ошибка при работе с файлом: " + e.getMessage());
        }
    }
}
