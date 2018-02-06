package pl.home;

import java.nio.file.Path;
import java.util.List;
import java.util.Queue;
import java.util.regex.Pattern;

class FileNameCleaner {
    private final Pattern pattern;

    FileNameCleaner() {
        this.pattern = Pattern.compile("^([0-9]+\\.)+");
    }

    void cleanAndGetFileNames(Path pathToFile, List<String> cleanFileNames, Queue<Path> dirtyFileNames) {
        cleanFileNames.add(pattern.matcher(pathToFile.getFileName().toString()).replaceFirst(""));
        dirtyFileNames.add(pathToFile);
    }
}