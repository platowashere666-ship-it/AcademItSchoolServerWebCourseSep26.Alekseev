package academits.ru.countries.data;

import java.util.Objects;

public class Currency {
    private String code;
    private String name;
    private String symbol;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Currency currency = (Currency) o;

        return code.equals(currency.code) && name.equals(currency.name) && symbol.equals(currency.symbol);
    }

    @Override
    public int hashCode() {
        final int prime = 37;
        int hash = 1;

        hash = prime * hash + Objects.hashCode(code);
        hash = prime * hash + Objects.hashCode(name);
        hash = prime * hash + Objects.hashCode(symbol);

        return hash;
    }
}
