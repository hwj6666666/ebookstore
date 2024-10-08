package com.reins.bookstore.controller;

import com.reins.bookstore.constants.Messages;
import com.reins.bookstore.entity.Address;
import com.reins.bookstore.entity.User;
import com.reins.bookstore.models.*;
import com.reins.bookstore.service.UserService;
import com.reins.bookstore.utils.SessionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "用户相关 API")
public class UserController {
    @Value("${upload.directory}")
    private String uploadDirectory;

    @Autowired
    UserService userService;

    @GetMapping("/me")
    @Operation(summary = "获取我的信息")
    ResponseEntity<UserDTO> getMe() {
        Long userId = SessionUtils.getUserId();
        User user = userService.getUser(userId);
        return ResponseEntity.ok(UserDTO.fromEntity(user));
    }

    @GetMapping("/me/addresses")
    @Operation(summary = "获取我的地址")
    ResponseEntity<List<Address>> getMyAddresses() {
        Long userId = SessionUtils.getUserId();
        List<Address> addresses = userService.getUserAddresses(userId);
        return ResponseEntity.ok(addresses);
    }

    @PostMapping("/me/addresses")
    @Operation(summary = "添加常用地址")
    ResponseEntity<ApiResponseBase> addMyAddress(@RequestBody AddAddressRequest request) {
        Long userId = SessionUtils.getUserId();
        return ResponseEntity.ok(userService.addUserAddresses(userId, request));
    }

    @DeleteMapping("/me/addresses/{id}")
    @Operation(summary = "删除常用地址")
    ResponseEntity<ApiResponseBase> deleteMyAddress(@PathVariable Long id) {
        Long userId = SessionUtils.getUserId();
        return ResponseEntity.ok(userService.deleteUserAddress(userId, id));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取其他用户的信息")
    ResponseEntity<PartialUserDTO> getOthers(@PathVariable(name = "id") Long userId) {
        User user = userService.getUser(userId);
        return ResponseEntity.ok(PartialUserDTO.fromEntity(user));
    }

    @PostMapping("/me/avatar")
    @Operation(summary = "修改我的头像")
    public ResponseEntity<ApiResponseBase> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseBase.fail(Messages.PARAMS_SHOULD_NOT_BE_EMPTY));
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseBase.fail(Messages.PARAMS_SHOULD_INVALID));
        }

        try {
            File uploadDir = new File(uploadDirectory);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            Long userId = SessionUtils.getUserId();
            String saveName = "user_" + userId + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDirectory, saveName);
            Files.write(filePath, file.getBytes());
            String oldPath = userService.changeMyAvatar(userId, saveName);
            if (oldPath != null && !oldPath.equals(saveName)) {
                Path oldFilePath = Paths.get(uploadDirectory, oldPath);
                Files.deleteIfExists(oldFilePath);
            }
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponseBase.succeed(Messages.MODIFY_SUCCEED));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseBase.fail(Messages.UPLOAD_FAILED));
        }
    }

    @GetMapping("/avatars/{filename:.+}")
    @Operation(summary = "头像静态资源 api")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadDirectory).resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, Files.probeContentType(filePath));

                return ResponseEntity.ok()
                        .headers(headers)
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/me/introduction")
    @Operation(summary = "修改我的简介")
    ResponseEntity<ApiResponseBase> changeMyIntroduction(@RequestBody ChangeIntroductionRequest request) {
        Long userId = SessionUtils.getUserId();
        String introduction = request.getIntroduction();
        if (introduction == null || introduction.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponseBase.fail(Messages.PARAMS_SHOULD_NOT_BE_EMPTY));
        }
        return ResponseEntity.ok(userService.changeMyIntroduction(userId, introduction));
    }

    @PutMapping("/me/password")
    @Operation(summary = "修改我的密码")
    ResponseEntity<ApiResponseBase> changeMyPassword(@RequestBody ChangePasswordRequest request) {
        Long userId = SessionUtils.getUserId();
        String password = request.getPassword();
        if (password == null || password.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponseBase.fail(Messages.PARAMS_SHOULD_NOT_BE_EMPTY));
        }
        return ResponseEntity.ok(userService.changeMyPassword(userId, password));
    }
}
