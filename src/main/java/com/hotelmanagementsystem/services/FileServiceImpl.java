package com.hotelmanagementsystem.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public class FileServiceImpl implements FileService {

	@Override
	public String uploadImage( MultipartFile file) throws IOException {
		String fileName=file.getOriginalFilename();
		String path="C:\\Users\\naeem\\OneDrive\\Desktop\\JavaNotes\\JavaNotes\\JavaProgramFile\\ProjectFolder\\HotelManagementSystem\\images";
		
		String random=UUID.randomUUID().toString();
	      String fileName1=random.concat(fileName.substring(fileName.lastIndexOf(".")));
		
		String fullPath=path+File.separator+fileName1;
		
		
//		File f=new File(path);
//		if(!f.exists()) {
//			f.mkdir();
//		}
		
		
		Files.copy(file.getInputStream(), Paths.get(fullPath));
		
		
		
		return fileName1;
	}

	@Override
	public InputStream getResource(String path, String fileName) throws FileNotFoundException {
		
		String fullPath=path+File.separator+fileName;
		
		InputStream is=new FileInputStream(fullPath);
		
		return is;
	}

}
