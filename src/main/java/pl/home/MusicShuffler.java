package pl.home;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class MusicShuffler {
    private List<String> fileNamesWithoutShufflePrefix = new LinkedList<>();
    private Queue<Path> shuffledFileNames = new ArrayDeque<>();
    private Path folderWithMusic;

    private FileNameCleaner fileNameCleaner;
    private PrefixShuffler prefixShuffler;
    private UserInputChecker userInputChecker;

    MusicShuffler() {
        this.fileNameCleaner = new FileNameCleaner();
        this.prefixShuffler = new PrefixShuffler();
        this.userInputChecker = new UserInputChecker();
    }

    void shuffle(String[] args) {
        folderWithMusic = userInputChecker.checkIfUserInputIsCorrect(args);
        System.out.println("Provided path: " + folderWithMusic.toAbsolutePath());

        cleanNamesAndInitializeShuffle();
        shuffleFileNames();
        System.out.println("Files shuffled successfully.");
    }

    private void cleanNamesAndInitializeShuffle() {
        checkIfMusicWasAlreadyShuffled(folderWithMusic);
        prefixShuffler.initializeShuffle(fileNamesWithoutShufflePrefix.size());
    }

    private void shuffleFileNames() {
        fileNamesWithoutShufflePrefix.forEach(fileName -> {
            String newFileName = folderWithMusic.toString() + "\\" + prefixShuffler.getShuffle() + "." + fileName;
            File file = shuffledFileNames.poll().toFile();
            file.setLastModified(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            file.renameTo(new File(newFileName));
        });
    }

    private void checkIfMusicWasAlreadyShuffled(Path folder) {
        try {
            List<Path> dirtyFileNames = Files.walk(folder, 1)
                    .filter(path -> Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS))
                    .collect(Collectors.toList());

            dirtyFileNames.forEach(data -> fileNameCleaner.cleanAndGetFileNames(data,
                    fileNamesWithoutShufflePrefix, shuffledFileNames));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}