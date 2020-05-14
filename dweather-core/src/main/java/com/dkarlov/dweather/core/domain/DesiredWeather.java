package com.dkarlov.dweather.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;

import static com.dkarlov.dweather.core.domain.TableNameConstants.RAIN;
import static com.dkarlov.dweather.core.domain.TableNameConstants.SNOW;
import static com.dkarlov.dweather.core.domain.TableNameConstants.TEMPERATURE;
import static com.dkarlov.dweather.core.domain.TableNameConstants.WIND;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DesiredWeather {

    @BsonProperty(TEMPERATURE)
    private Double temperature;

    @BsonProperty(WIND)
    private Double wind;

    @BsonProperty(RAIN)
    private Boolean raining;

    @BsonProperty(SNOW)
    private Boolean snowing;
}
