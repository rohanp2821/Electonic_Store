package com.electonic.store.ElectonicStore.services.impl;

import com.electonic.store.ElectonicStore.dtos.PageableResponse;
import com.electonic.store.ElectonicStore.dtos.UserDto;
import com.electonic.store.ElectonicStore.entities.User;
import com.electonic.store.ElectonicStore.exception.ResourceNotFoundException;
import com.electonic.store.ElectonicStore.helper.Helper;
import com.electonic.store.ElectonicStore.repositories.UserRepository;
import com.electonic.store.ElectonicStore.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;
    @Value("${user.profile.image-path}")
    private String imagePath;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Override
    public UserDto createUser(UserDto userDto) {
        // Generate userId in random format

        String userId = UUID.randomUUID().toString();
        userDto.setId(userId);

        // When we want to add data in DB then we convert dto to entity

        User user = dtoToEntity(userDto);

        // Then we saved that entity in DB

        User save = userRepository.save(user);

        // Convert entity to dto for security reasons

        UserDto newDto = entityToDtoUser(user);

        return newDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String id) {

        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with this ID"));

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setGender(userDto.getGender());
        user.setAbout(userDto.getAbout());
        user.setImageName(userDto.getImageName());

        User save = userRepository.save(user);

        // Convert Entity to Dto

        UserDto newDto = entityToDtoUser(user);


        return newDto;
    }

    @Override
    public void deleteUser(String id)  {

        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with this ID"));

        String fullPath = imagePath + user.getImageName();

        try {
            Path path = Paths.get(fullPath);
            Files.delete(path);
        } catch (NoSuchFileException exception) {
              logger.info("No such user image found by this id");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        userRepository.delete(user);
    }

    @Override
    public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());

        Pageable pageable = PageRequest.of(pageNumber-1,pageSize, sort);

        Page<User> userPage = userRepository.findAll(pageable);

        PageableResponse<UserDto> pageableResponse = Helper.getPageableResponse(userPage, UserDto.class);


        return pageableResponse;
    }

    @Override
    public UserDto getSingleUserById(String id) {

        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with this ID"));

        UserDto userDto = entityToDtoUser(user);

        return userDto;
    }

    @Override
    public UserDto getSingleUserByEmail(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with this Email"));

        UserDto userDto = entityToDtoUser(user);
        return userDto;
    }

    @Override
    public List<UserDto> searchUser(String keyword) {

        List<User> users = userRepository.findByNameContaining(keyword);

        List<UserDto> userDtos = users.stream().map(user -> entityToDtoUser(user)).collect(Collectors.toList());

        return userDtos;
    }

    //This is for convert Entity to Dto
    private UserDto entityToDtoUser(User savedUser) {

//        UserDto userDto = UserDto.builder()
//                .id(savedUser.getId())
//                .name(savedUser.getName())
//                .email(savedUser.getEmail())
//                .password(savedUser.getPassword())
//                .gender(savedUser.getGender())
//                .about(savedUser.getAbout())
//                .imageName(savedUser.getImageName()).build();

        return mapper.map(savedUser, UserDto.class);

    }

    //This is for convert dto to Entity
    private User dtoToEntity(UserDto userDto) {

//        User user = User.builder()
//                .id(userDto.getId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .gender(userDto.getGender())
//                .about(userDto.getAbout())
//                .imageName(userDto.getImageName()).build();

        return mapper.map(userDto, User.class);
    }

}
