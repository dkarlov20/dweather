package com.dkarlov.dweather.core.repository.impl;

import com.dkarlov.dweather.core.domain.Event;
import com.dkarlov.dweather.core.domain.exception.DWeatherException;
import com.dkarlov.dweather.core.repository.EventRepository;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.dkarlov.dweather.core.domain.TableNameConstants.CREATED_DATE;
import static com.dkarlov.dweather.core.domain.TableNameConstants.USER_ID;
import static com.dkarlov.dweather.core.domain.TableNameConstants._ID;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.descending;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Repository
@Slf4j
public class EventRepositoryImpl implements EventRepository {
    private final MongoDatabase mongoDatabase;

    private MongoCollection<Event> eventCollection;

    @Value("${dweather.mongodb.collection.event}")
    private String eventCollectionName;

    public EventRepositoryImpl(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    @PostConstruct
    public void init() {
        this.eventCollection = this.mongoDatabase.getCollection(eventCollectionName, Event.class).withCodecRegistry(fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()))
        );
    }

    @Override
    public Event saveEvent(Event event) {
        try {
            eventCollection.insertOne(event);
            log.info("Event for User {} was saved ", event.getUserId());
        } catch (MongoException e) {
            log.error("Error occurred while inserting event", e);
            throw new DWeatherException("Error occurred while inserting event", e);
        }

        return event;
    }

    @Override
    public List<Event> getEventsByUserId(Integer userId) {
        final List<Event> userEvents = new ArrayList<>();

        eventCollection
                .find(eq(USER_ID, userId))
                .sort(descending(CREATED_DATE))
                .iterator()
                .forEachRemaining(userEvents::add);

        log.info("Found {} events for User {}", userEvents.size(), userId);
        return userEvents;
    }

    @Override
    public Optional<Event> getEventById(ObjectId eventId) {
        return Optional.ofNullable(
                eventCollection
                        .find(eq(_ID, eventId))
                        .first())
                .or(() -> {
                    log.info("Event with id {} was not found", eventId);
                    return Optional.empty();
                });
    }
}
