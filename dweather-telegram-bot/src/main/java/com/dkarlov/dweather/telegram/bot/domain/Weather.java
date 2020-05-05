package com.dkarlov.dweather.telegram.bot.domain;

import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Iterator;
import java.util.Set;

import static com.dkarlov.dweather.telegram.bot.domain.TableNameConstants.PRECIPITATION;
import static com.dkarlov.dweather.telegram.bot.domain.TableNameConstants.TEMPERATURE;
import static com.dkarlov.dweather.telegram.bot.domain.TableNameConstants.WIND;
import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.isNoneEmpty;

@Data
public class Weather {

    @BsonProperty(TEMPERATURE)
    private String temperature;

    @BsonProperty(WIND)
    private String wind;

    @BsonProperty(PRECIPITATION)
    private Set<Precipitation> precipitation;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (isNoneEmpty(temperature)) {
            stringBuilder.append("Temperature: ").append(temperature).append("\n");
        }
        if (isNoneEmpty(wind)) {
            stringBuilder.append("Wind: ").append(wind).append("\n");
        }
        if (precipitation != null) {
            stringBuilder.append("Precipitation: ");
            Iterator<Precipitation> iterator = precipitation.iterator();
            while (iterator.hasNext()) {
                stringBuilder.append(capitalize(iterator.next().name().toLowerCase()));
                if (iterator.hasNext()) {
                    stringBuilder.append(", ");
                }
            }
        }

        return stringBuilder.toString();
    }
}
