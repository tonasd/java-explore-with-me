package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.event.*;
import ru.practicum.ewm.model.*;

import java.time.LocalDateTime;

public class EventMapper {
    public static EventShortDto mapToEventShortDto(Event event, long confirmedRequests, long views) {
       return EventShortDto.builder()
               .id(event.getId())
               .eventDate(event.getEventDate())
               .confirmedRequests(confirmedRequests)
               .annotation(event.getAnnotation())
               .category(CategoryMapper.mapToCategoryDto(event.getCategory()))
               .initiator(UserMapper.mapUserToUserShortDto(event.getInitiator()))
               .paid(event.isPaid())
               .title(event.getTitle())
               .views(views)
               .build();
    }

    public static Event mapToEvent(User initiator, NewEventDto dto, Category category) {
        Event event = new Event();
        event.setTitle(dto.getTitle());
        event.setAnnotation(dto.getAnnotation());
        event.setCategory(category);
        event.setDescription(dto.getDescription());
        event.setCreatedOn(LocalDateTime.now());
        event.setEventDate(dto.getEventDate());
        event.setInitiator(initiator);
        event.setPaid(dto.isPaid());
        event.setParticipantLimit(dto.getParticipantLimit());
        event.setRequestModeration(dto.isRequestModeration());
        event.setState(EventState.PENDING);
        event.setLocationLat(dto.getLocation().getLat());
        event.setLocationLon(dto.getLocation().getLon());
        return event;
    }

    public static EventFullDto mapToEventFullDto(Event event, long confirmedRequests, long views) {
        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.mapToCategoryDto(event.getCategory()))
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.isRequestModeration())
                .confirmedRequests(confirmedRequests)
                .createdOn(event.getCreatedOn())
                .initiator(UserMapper.mapUserToUserShortDto(event.getInitiator()))
                .publishedOn(event.getPublishedOn())
                .state(event.getState())
                .location(new Location(event.getLocationLat(), event.getLocationLon()))
                .views(views)
                .build();
    }

    public static Event mapToEvent(Event event, UpdateEventUserRequest dto, Category category) {
        if (dto.getAnnotation() != null) event.setAnnotation(dto.getAnnotation());
        if (dto.getTitle() != null) event.setTitle(dto.getTitle());
        if(category != null) event.setCategory(category);
        if(dto.getDescription() != null) event.setDescription(dto.getDescription());
        if(dto.getEventDate() != null) event.setEventDate(dto.getEventDate());
        if(dto.getPaid() != null) event.setPaid(dto.getPaid());
        if(dto.getParticipantLimit() != null) event.setParticipantLimit(dto.getParticipantLimit());
        if(dto.getRequestModeration() != null) event.setRequestModeration(dto.getRequestModeration());
        if(dto.getLocation() != null){
            event.setLocationLon(dto.getLocation().getLon());
            event.setLocationLat(dto.getLocation().getLat());
        }
        if (UpdateEventUserRequest.StateAction.CANCEL_REVIEW.equals(dto.getStateAction())) {
            event.setState(EventState.CANCELED);
        }
        if (UpdateEventUserRequest.StateAction.SEND_TO_REVIEW.equals(dto.getStateAction())) {
            event.setState(EventState.PENDING);
        }

        return event;
    }

    public static Event mapToEvent(Event event, UpdateEventAdminRequest dto, Category category) {
        if (dto.getAnnotation() != null) event.setAnnotation(dto.getAnnotation());
        if (dto.getTitle() != null) event.setTitle(dto.getTitle());
        if(category != null) event.setCategory(category);
        if(dto.getDescription() != null) event.setDescription(dto.getDescription());
        if(dto.getEventDate() != null) event.setEventDate(dto.getEventDate());
        if(dto.getPaid() != null) event.setPaid(dto.getPaid());
        if(dto.getParticipantLimit() != null) event.setParticipantLimit(dto.getParticipantLimit());
        if(dto.getRequestModeration() != null) event.setRequestModeration(dto.getRequestModeration());
        if(dto.getLocation() != null){
            event.setLocationLon(dto.getLocation().getLon());
            event.setLocationLat(dto.getLocation().getLat());
        }
        if (UpdateEventAdminRequest.StateAction.REJECT_EVENT.equals(dto.getStateAction())) {
            event.setState(EventState.CANCELED);
        }
        if (UpdateEventAdminRequest.StateAction.PUBLISH_EVENT.equals(dto.getStateAction())) {
            event.setState(EventState.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
        }

        return event;
    }
}
