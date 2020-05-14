package com.dkarlov.dweather.core.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

import static com.dkarlov.dweather.core.domain.TableNameConstants.CREATED_DATE;
import static com.dkarlov.dweather.core.domain.TableNameConstants.DESIRED_WEATHER;
import static com.dkarlov.dweather.core.domain.TableNameConstants.SUITABLE_DATES;
import static com.dkarlov.dweather.core.domain.TableNameConstants.USER_ID;
import static com.dkarlov.dweather.core.domain.TableNameConstants.USER_NAME;
import static com.dkarlov.dweather.core.domain.TableNameConstants._ID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @BsonProperty(_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @BsonProperty(USER_ID)
    private Integer userId;

    @BsonProperty(USER_NAME)
    private String userName;

    @BsonProperty(DESIRED_WEATHER)
    private DesiredWeather desiredWeather;

    @BsonProperty(SUITABLE_DATES)
    private List<WeatherPerDay> suitableDates;

    @BsonProperty(CREATED_DATE)
    private LocalDateTime createdDate;
}
