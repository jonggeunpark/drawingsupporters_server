package com.drawing.drawing.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class GcsService {

    private final Storage storage;

    // 로컬에 파일 저장
    public void saveFile(MultipartFile file, String uuidFileName , String directoryPath) throws IOException {

        // parent directory를 찾는다.
        Path directory = Paths.get(directoryPath).toAbsolutePath().normalize();

        // directory 해당 경로까지 디렉토리를 모두 만든다.
        Files.createDirectories(directory);

        // 파일명 수정
        String fileName = StringUtils.cleanPath(uuidFileName);

        // 파일명에 '..' 문자가 들어 있다면 오류 발생
        Assert.state(!fileName.contains(".."), "Name of file cannot contain '..'");
        // 파일을 저장할 경로를 Path 객체로 받는다.
        Path targetPath = directory.resolve(fileName).normalize();

        // 저장
        Assert.state(!Files.exists(targetPath), fileName + " File alerdy exists.");
        file.transferTo(targetPath);
    }

    // 파일 다운로드
    public Blob downloadFileFromGCS(String uuid, String filename) {
        String ffilename = uuid + filename;
        Blob blob = storage.get("drawing-storage", ffilename);
        blob.downloadTo(Paths.get(ffilename));
        return blob;
    }


    // 파일 업로드
    public BlobInfo uploadFileToGCS(UUID uuid, MultipartFile file) throws IOException {

        String uuidFileName = uuid + file.getOriginalFilename();

        // 파일 로컬에 임시 저장
        saveFile(file, uuidFileName , "temp");

        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder("drawing-storage", uuidFileName)
                        .setAcl(new ArrayList<>(Arrays.asList(Acl.of(Acl.User.ofAllAuthenticatedUsers(), Acl.Role.READER))))
                        .build(),
                new FileInputStream("temp/" + uuidFileName));

        // 임시파일 삭제
        Path directory = Paths.get("temp").toAbsolutePath().normalize();
        File file1 = new File(directory + "/" + uuidFileName);
        System.out.println(directory + uuidFileName);
        if(file1.delete()){ System.out.println("파일삭제 성공"); }else{ System.out.println("파일삭제 실패"); }

        return blobInfo;
    }
}
