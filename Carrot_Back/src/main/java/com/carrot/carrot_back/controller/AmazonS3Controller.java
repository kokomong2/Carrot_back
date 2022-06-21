package com.carrot.carrot_back.controller;

import com.carrot.carrot_back.dto.requestDto.PostRequestDto;
import com.carrot.carrot_back.dto.responseDto.PostResponseDto;
import com.carrot.carrot_back.model.Post;
import io.swagger.annotations.ApiOperation;
import com.carrot.carrot_back.service.AwsS3Service;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class AmazonS3Controller {

    private final AwsS3Service awsS3Service;

    /**
     * Amazon S3에 파일 업로드
     *
     * @return 성공 시 200 Success와 함께 업로드 된 파일의 파일명 리스트 반환
     */
    @ApiOperation(value = "Amazon S3에 파일 업로드", notes = "Amazon S3에 파일 업로드 ")
    @PostMapping("/file")
    public ResponseEntity<List<String>> uploadFile(@ApiParam(value = "파일들(여러 파일 업로드 가능)", required = true) @RequestPart List<MultipartFile> multipartFile) {
        return ResponseEntity.ok().body(awsS3Service.uploadFile(multipartFile));
    }

//    @GetMapping("/file")
//    public ResponseEntity<List<String>> getUrls (@RequestPart List<MultipartFile> multipartFile, String bucket, String fileName) {
//        return ResponseEntity.ok().body(awsS3Service.fileUrls(multipartFile, bucket, fileName));
//    }

    /**
     * Amazon S3에 업로드 된 파일을 삭제
     *
     * @return 성공 시 200 Success
     */
    @ApiOperation(value = "Amazon S3에 업로드 된 파일을 삭제", notes = "Amazon S3에 업로드된 파일 삭제")
    @DeleteMapping("/file")
    public ResponseEntity<Void> deleteFile(@ApiParam(value = "파일 하나 삭제", required = true) @RequestParam String fileName) {
        awsS3Service.deleteFile(fileName);
        return ResponseEntity.ok().build();
    }
}
