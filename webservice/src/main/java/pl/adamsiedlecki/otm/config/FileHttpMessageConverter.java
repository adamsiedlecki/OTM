package pl.adamsiedlecki.otm.config;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import pl.adamsiedlecki.otm.tools.files.MyFilesystem;
import pl.adamsiedlecki.otm.tools.uuid.UuidTool;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileHttpMessageConverter extends AbstractHttpMessageConverter<File> {

    public FileHttpMessageConverter() {
        super(MediaType.IMAGE_JPEG, MediaType.IMAGE_PNG, MediaType.IMAGE_GIF);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return File.class.isAssignableFrom(clazz);
    }

    @Override
    protected File readInternal(Class<? extends File> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return Files.write(new File(MyFilesystem.getImageStoragePath() + UuidTool.getRandom()+".jpg").toPath(), inputMessage.getBody().readAllBytes()).toFile();
    }

    @Override
    protected void writeInternal(File file, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        outputMessage.getBody().write(Files.readAllBytes(file.toPath()));
        outputMessage.getBody().flush();
    }
}
