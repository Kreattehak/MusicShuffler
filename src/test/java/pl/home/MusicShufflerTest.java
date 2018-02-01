package pl.home;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

public class MusicShufflerTest {

    private Pattern pattern = Pattern.compile("^([0-9]+\\.)+");
    private List<String> fileNames;
    private int fileCount;

    @Before
    public void setUp() throws Exception {
        fileNames = getFileNames();
        fileCount = getFileCount();
    }

    @Test
    public void shouldNotChangeTheCountOfFiles() {
        MusicShuffler.main(new String[]{"E:\\Music"});

        assertEquals(fileCount, getFileNames().size());
    }

    @Test
    public void fileShouldNotBeRenamedWithOtherFileName() {
        Map<String, Long> oldFilesLength = getFilesLength();

        MusicShuffler.main(new String[]{"E:\\Music"});

        Map<String, Long> newFilesLength = getFilesLength();

        oldFilesLength.forEach((key, value) -> assertEquals(value, newFilesLength.get(key)));
    }

    private List<String> getFileNames() {
        List<String> fileNames = new ArrayList<>();
        try {
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get("E:\\Music"),
                    (data -> !data.toFile().isDirectory()));
            for (Path path : directoryStream) {
                fileNames.add(path.getFileName().toString());
            }
        } catch (IOException ignored) {
        }

        return fileNames;
    }

    private Map<String, Long> getFilesLength() {
        Map<String, Long> filesLength = new HashMap<>();
        try {
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get("E:\\Music"),
                    (data -> !data.toFile().isDirectory()));
            for (Path path : directoryStream) {
                filesLength.put(stripPrefix(path.getFileName().toString()), path.toFile().length());
            }
        } catch (IOException ignored) {
        }

        return filesLength;
    }

    private int getFileCount() {
        return fileNames.size();
    }

    private String stripPrefix(String fileName) {
        return pattern.matcher(fileName).replaceFirst("");
    }
}