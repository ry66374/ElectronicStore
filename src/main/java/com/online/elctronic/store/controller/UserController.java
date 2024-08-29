package com.online.elctronic.store.controller;

import com.online.elctronic.store.dtos.ApiResponseMessage;
import com.online.elctronic.store.dtos.ImageResponse;
import com.online.elctronic.store.dtos.PageableResponse;
import com.online.elctronic.store.dtos.UserDto;
import com.online.elctronic.store.serivces.FileService;
import com.online.elctronic.store.serivces.UserService;
import com.online.elctronic.store.validate.ImageValidator;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    private Logger logger = LoggerFactory.getLogger(ImageValidator.class);

    private Logger log = LoggerFactory.getLogger(UserController.class);
    //Create
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
         UserDto userDto1 = userService.createUser(userDto);
         return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }

    //Update
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @PathVariable("userId") String userId, @RequestBody UserDto userDto){
        UserDto updatedUserDto = userService.updatedUser(userDto, userId);
        return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
    }


    //Delete

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId) throws IOException {
        userService.deleteUser(userId);
        ApiResponseMessage message = ApiResponseMessage
                .builder()
                .message("User is deleted Successfully")
                .success(true)
                .status(HttpStatus.OK)
                .build();

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    //Get all
    @GetMapping
    public ResponseEntity<PageableResponse<UserDto>> getAllUser(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        return new ResponseEntity<>(userService.getAllUser(pageNumber,pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    //get Single
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable String userId){
        return new ResponseEntity<>(userService.getUserById(userId),HttpStatus.OK);
    }


    // get user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email){
        return new ResponseEntity<>(userService.getUserByEmail(email),HttpStatus.OK);
    }

    //Search user by keyword

    @GetMapping("/search/{keywords}")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keywords){
        return new ResponseEntity<>(userService.searchUser(keywords),HttpStatus.OK);
    }


    // Upload Image
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(
            @RequestParam("userImage")MultipartFile image, @PathVariable String userId
            ) throws IOException {

        logger.info("This is the file path", imageUploadPath);
       String imageName=  fileService.uploadFile(image,imageUploadPath);

       UserDto userDto = userService.getUserById(userId);
       userDto.setImageName(imageName);
       UserDto userDto1 = userService.updatedUser(userDto, userId);

       ImageResponse imageResponse = ImageResponse.builder()
               .imageName(imageName)
               .success(true)
               .status(HttpStatus.CREATED)
               .build();

       return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

    // Serve Image
    @GetMapping("/image/{userId}")
    public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {

        UserDto userDto = userService.getUserById(userId);
        log.info("User Image name: {}", userDto.getImageName());
        InputStream resource = fileService.getResource(imageUploadPath, userDto.getImageName());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }

}
