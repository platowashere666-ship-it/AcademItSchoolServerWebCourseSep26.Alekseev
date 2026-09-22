package academits.ru.countries;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        try (InputStream inputStream = Main.class.getResourceAsStream("/countries.json")) {
            if (inputStream == null) {
                throw new IOException("Файл countries.json не найден");
            }

            ObjectMapper mapper = new ObjectMapper();

            ArrayNode countries = (ArrayNode) mapper.readTree(inputStream);

            long totalPopulation = 0;

            for (JsonNode country : countries) {
                totalPopulation += country.get("population").asLong();
            }

            System.out.println("Суммарная численность населения по странам: " + totalPopulation);

            Map<String, String> uniqueCurrencies = new TreeMap<>();

            for (JsonNode country : countries) {
                JsonNode currencies = country.get("currencies");

                if (currencies != null) {
                    for (JsonNode currency : currencies) {
                        JsonNode code = currency.get("code");
                        JsonNode name = currency.get("name");

                        if (code == null || code.isNull() || name == null || name.isNull()) {
                            continue;
                        }

                        String codeValue = code.stringValue();
                        String nameValue = name.stringValue();

                        if (codeValue.isBlank() || codeValue.equals("(none)") || nameValue.isBlank()) {
                            continue;
                        }

                        uniqueCurrencies.putIfAbsent(codeValue, nameValue);
                    }
                }
            }

            System.out.println("Перечень уникальных валют из файла:");
            uniqueCurrencies.forEach((code, name) ->
                    System.out.println(code + " - " + name));

            ArrayNode bigCountries = mapper.createArrayNode();

            for (JsonNode country : countries) {
                if (country.get("population").asLong() >= 1000000) {
                    bigCountries.add(country);
                }
            }

            mapper.writeValue(new File("result.json"), bigCountries);
        } catch (IOException e) {
            System.out.println("Ошибка при работе с файлом: " + e.getMessage());
        }
    }
}
