package com.online.elctronic.store.serivces;

import com.online.elctronic.store.dtos.PageableResponse;
import com.online.elctronic.store.dtos.UserDto;
import com.online.elctronic.store.entities.User;

import java.io.IOException;
import java.util.List;

public interface UserService {
    //Create User
    UserDto createUser(UserDto userDto);

    //Update
    UserDto updatedUser(UserDto userDto, String userId);

    //Delete
     void deleteUser(String userId) throws IOException;

    //get all users
    PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);

    //get single user by id
    UserDto getUserById(String UserId);

    //get user by email
    UserDto getUserByEmail(String userEmail);

    //Search User
    List<UserDto> searchUser(String keywords);
}
