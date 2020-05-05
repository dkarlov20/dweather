package com.dkarlov.dweather.telegram.bot.domain;

import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

import static com.dkarlov.dweather.telegram.bot.domain.TableNameConstants.CREATED_DATE;
import static com.dkarlov.dweather.telegram.bot.domain.TableNameConstants.USER_ID;
import static com.dkarlov.dweather.telegram.bot.domain.TableNameConstants.USER_NAME;
import static com.dkarlov.dweather.telegram.bot.domain.TableNameConstants.WEATHER;
import static com.dkarlov.dweather.telegram.bot.domain.TableNameConstants._ID;

@Data
public class Event {

    @BsonProperty(_ID)
    private ObjectId id;

    @BsonProperty(USER_ID)
    private int userId;

    @BsonProperty(USER_NAME)
    private String userName;

    @BsonProperty(WEATHER)
    private Weather weather;

    @BsonProperty(CREATED_DATE)
    private LocalDateTime createdDate;
}
