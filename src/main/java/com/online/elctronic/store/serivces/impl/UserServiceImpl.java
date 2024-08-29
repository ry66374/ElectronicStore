package com.online.elctronic.store.serivces.impl;

import com.online.elctronic.store.dtos.PageableResponse;
import com.online.elctronic.store.dtos.UserDto;
import com.online.elctronic.store.entities.User;
import com.online.elctronic.store.exceptions.ResourceNotFoundException;
import com.online.elctronic.store.helper.Helper;
import com.online.elctronic.store.repositories.UserRepository;
import com.online.elctronic.store.serivces.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${user.profile.image.path}")
    private String imagePath;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserDto createUser(UserDto userDto) {
        //generate unique id in string format
        String userId  = UUID.randomUUID().toString();
        userDto.setUserId(userId);
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        //dto->entity
        User user = dtoToEntity(userDto);
        User savedUser = userRepository.save(user);
        //entity->dto
        UserDto newDto = entityToDto(savedUser);
        return newDto;
    }


    @Override
    public UserDto updatedUser(UserDto userDto, String userId) {

        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found with the given id !!"));
        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setPassword((userDto.getPassword()));
        user.setImageName(userDto.getImageName());

        //save new updated data of user
        User updatedUser = userRepository.save(user);
        UserDto updatedDto = entityToDto(updatedUser);
        return updatedDto;
    }

    @Override
    public void deleteUser(String userId) throws IOException {

        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found with the given id !!"));
        // Delete User's profile image
        String fullPath = imagePath+user.getImageName();
        try{
            Path path = Paths.get(fullPath);
            Files.delete(path);
        }catch (NoSuchFileException ex){
            logger.info("UserImage is not found in the folder");
            ex.printStackTrace();
        }catch (IOException exception){
            exception.printStackTrace();
        }

        // delete the user
        userRepository.delete(user);
    }

    @Override
    public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc"))? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

        Pageable pageable= PageRequest.of(pageNumber,pageSize, sort);
        Page<User> page = userRepository.findAll(pageable);
        PageableResponse<UserDto> response = Helper.getPageableResponse(page, UserDto.class);

        return response;
    }

    @Override
    public UserDto getUserById(String userId) {

        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found with the given id"));
        UserDto userDto = entityToDto(user);
        return userDto;
    }

    @Override
    public UserDto getUserByEmail(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new ResourceNotFoundException("Email is not found"));
        return entityToDto(user);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> users = userRepository.findByNameContaining(keyword);
        List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        return dtoList;
    }





    private UserDto entityToDto(User savedUser) {

        // Manual Mapping
//        UserDto userDto = UserDto.builder()
//                .userId(savedUser.getUserId())
//                .name((savedUser.getName()))
//                .email((savedUser.getEmail()))
//                .password(savedUser.getPassword())
//                .about(savedUser.getAbout())
//                .gender(savedUser.getGender())
//                .imageName(savedUser.getImageName())
//                .build();

        //Automatic Mapping by ModelMapper
        return modelMapper.map(savedUser, UserDto.class);
    }

    private User dtoToEntity(UserDto userDto) {
        // Manual Mapping
//        User user = User.builder()
//                .userId(userDto.getUserId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .about(userDto.getAbout())
//                .gender(userDto.getGender())
//                .imageName(userDto.getImageName())
//                .build();

        //Automatic Mapping by ModelMapper
        return modelMapper.map(userDto, User.class);
    }




}
