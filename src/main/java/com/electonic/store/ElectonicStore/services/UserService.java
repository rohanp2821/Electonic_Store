package com.electonic.store.ElectonicStore.services;

import com.electonic.store.ElectonicStore.dtos.PageableResponse;
import com.electonic.store.ElectonicStore.dtos.UserDto;

import java.io.IOException;
import java.util.List;

public interface UserService {

    //Create
    UserDto createUser (UserDto userDto);

    //Update
    UserDto updateUser (UserDto userDto, String id);

    //Delete
    void deleteUser (String id) throws IOException;

    //Get all user
    PageableResponse<UserDto> getAllUser (int pageNumber, int pageSize, String sortBy, String sortDir);

    //Get single user by id
    UserDto getSingleUserById (String id);

    //Get single user by Email
    UserDto getSingleUserByEmail(String email);

    //Search user
    List<UserDto> searchUser (String keyword);

}
