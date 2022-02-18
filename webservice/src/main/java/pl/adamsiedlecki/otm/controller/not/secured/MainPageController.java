package pl.adamsiedlecki.otm.controller.not.secured;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.adamsiedlecki.otm.tools.files.MyFilesystem;

import java.io.File;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MainPageController {

    private final MyFilesystem myFilesystem;

    @GetMapping("/main")
    public String getIndex(Model model) {
        List<String> overnightChartsFilenameList = Arrays
                .stream(myFilesystem.getAllFilesInDirectory(new File(MyFilesystem.getOvernightChartsPath())))
                .sorted((file1, file2) -> {
                    Optional<BasicFileAttributes> optionalBasicFileAttributes1 = myFilesystem.getFileAttributes(file1);
                    Optional<BasicFileAttributes> optionalBasicFileAttributes2 = myFilesystem.getFileAttributes(file2);
                    if (optionalBasicFileAttributes1.isPresent() && optionalBasicFileAttributes2.isPresent()) {
                        if (optionalBasicFileAttributes1.get().lastModifiedTime().toMillis() > optionalBasicFileAttributes2.get().lastModifiedTime().toMillis()) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                    return 0;
                })
                .map(File::getName)
                .collect(Collectors.toList());
        model.addAttribute("imageNames", overnightChartsFilenameList);
        return "main";
    }
}
