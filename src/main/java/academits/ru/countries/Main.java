package academits.ru.countries;

import academits.ru.countries.data.Country;
import academits.ru.countries.data.Currency;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

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

            List<Currency> uniqueCurrencies = countries.stream()
                    .map(Country::getCurrencies)
                    .filter(Objects::nonNull)
                    .flatMap(List::stream)
                    .distinct()
                    .toList();

            System.out.println("Перечень уникальных валют из файла:");
            uniqueCurrencies.forEach(currency ->
                    System.out.println(currency.getCode() + " - " + currency.getName()));

            List<Country> bigCountries = countries.stream()
                    .filter(c -> c.getPopulation() >= 1000000)
                    .toList();

            mapper.writeValue(new File("result.json"), bigCountries);
        } catch (IOException e) {
            System.out.println("Ошибка при работе с файлом: " + e.getMessage());
        }
    }
}
