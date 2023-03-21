package com.electonic.store.ElectonicStore.controller;

import com.electonic.store.ElectonicStore.dtos.ApiResponceMessage;
import com.electonic.store.ElectonicStore.dtos.ImageResponse;
import com.electonic.store.ElectonicStore.dtos.PageableResponse;
import com.electonic.store.ElectonicStore.dtos.UserDto;
import com.electonic.store.ElectonicStore.services.FileService;
import com.electonic.store.ElectonicStore.services.UserService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
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
    @Value("${user.profile.image-path}")
    private String imageUploadPath;
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    //CREATE
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto userDto1 = userService.createUser(userDto);
        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }

    //UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @Valid @RequestBody UserDto userDto,
            @PathVariable ("id") String id) {
        UserDto userDto1 = userService.updateUser(userDto, id);
        return new ResponseEntity<>(userDto1, HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponceMessage> deleateUser(@PathVariable ("id") String id) throws IOException {

        userService.deleteUser(id);
        ApiResponceMessage apiResponceMessage = ApiResponceMessage.builder().message("Deleted User Successfully!!").success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(apiResponceMessage, HttpStatus.OK);

    }
    //GET ALL

    @GetMapping
    public ResponseEntity<PageableResponse<UserDto>> getAllUser (
            @RequestParam (value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
            @RequestParam (value = "pageSize", defaultValue = "5", required = false) int pageSize,
            @RequestParam (value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam (value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<UserDto> allUser = userService.getAllUser(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allUser, HttpStatus.OK);
    }
    //GET BY ID
    @GetMapping("/id/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable ("id") String id) {
        UserDto singleUserById = userService.getSingleUserById(id);
        return  new ResponseEntity<>(singleUserById, HttpStatus.OK);
    }
    //GET BY EMAIL
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable ("email") String email) {
        return new ResponseEntity<>(userService.getSingleUserByEmail(email), HttpStatus.OK);
    }

    //SEARCH BY USER
    @GetMapping("/keyword/{keyword}")
    public ResponseEntity<List<UserDto>> getUserByKeyword (@PathVariable ("keyword") String keyword) {
        return new ResponseEntity<>(userService.searchUser(keyword), HttpStatus.OK);
    }

    // Upload Image
    @PostMapping("/image/{id}")
    public ResponseEntity<ImageResponse> uploadUserImage (
            @PathVariable ("id") String id,
            @RequestParam ("userImage")MultipartFile image
            ) throws IOException {

        String uploadFile = fileService.uploadFile(image, imageUploadPath);
        ImageResponse status = ImageResponse.builder()
                .imageName(uploadFile)
                .message("Image upload Successfully !!")
                .success(true)
                .status(HttpStatus.ACCEPTED).build();

        UserDto dto = userService.getSingleUserById(id);
        dto.setImageName(uploadFile);
        userService.updateUser(dto, id);

        return new ResponseEntity<>(status, HttpStatus.CREATED);

    }


    // Serve Image

    @GetMapping(value = "/image/{userId}")
    public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
        UserDto user = userService.getSingleUserById(userId);
        logger.info("User image name : {} ", imageUploadPath+user.getImageName());
        InputStream resource = fileService.getResource(imageUploadPath, user.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());

    }



}
