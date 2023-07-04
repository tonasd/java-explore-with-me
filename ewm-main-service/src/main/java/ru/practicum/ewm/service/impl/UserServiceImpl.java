package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.user.NewUserRequest;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.exception.UserNotFoundException;
import ru.practicum.ewm.mapper.UserMapper;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.RatingRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.repository.projection.UserRatingView;
import ru.practicum.ewm.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;

    @Override
    public List<UserDto> find(@Nullable List<Long> ids, int from, int size) {
        PageRequest page = PageRequest.of(from / size, size);

        List<User> users;
        if (ids != null) {
            users = userRepository.findAllByIdIn(ids, page);
        } else {
            users = userRepository.findAll(page).getContent();
        }

        setRating(users);

        return users.stream().map(UserMapper::mapUserToUserDto).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public UserDto create(NewUserRequest dto) {
        User newUser = UserMapper.mapNewUserReuestToUser(dto);
        newUser = userRepository.save(newUser);

        return UserMapper.mapUserToUserDto(newUser);
    }

    @Override
    @Transactional
    public void delete(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        userRepository.deleteById(userId);
    }

    private void setRating(List<User> users) {
        List<UserRatingView> ratingForUsers = ratingRepository.getRatingForUsers(users.stream()
                .map(User::getId).collect(Collectors.toUnmodifiableList()));
        //key = user id, value = rating
        Map<Long, UserRatingView> rating = ratingForUsers.stream()
                .collect(Collectors.toMap(UserRatingView::getUserId, Function.identity()));

        users.forEach(u -> u.setRating(rating.get(u.getId())));
    }
}
