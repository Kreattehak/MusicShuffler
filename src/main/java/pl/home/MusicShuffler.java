package pl.home;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Remember to provide path to folder with music as command line argument!
 * RadioMixer was tested only on Windows
 * File names can't begin with a number, all music files must be in specified folder, no subfolders allowed
 */
public class MusicShuffler {

    static List<String> cleaned = new LinkedList<>();
    static Queue<Path> dirtyFileNames = new ArrayDeque<>();
    static Stack<Integer> shuffle = new Stack<>();

    public static void main(String[] args) {
        Path folderWithMusic = checkForCommandLinePath(args);
        System.out.println("Provided path: " + folderWithMusic.toAbsolutePath());

        checkIfMusicWasAlreadyShuffled(folderWithMusic);
        initializeShuffle(cleaned.size());

        cleaned.forEach(fileName -> {
            System.out.println(dirtyFileNames.size());
            System.out.println(cleaned.size());
            String newFileName = folderWithMusic.toString() + "\\" + shuffle.pop() + "." + fileName;
            File file = dirtyFileNames.poll().toFile();
            file.setLastModified(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            file.renameTo(new File(newFileName));
        });

        System.out.println("Files shuffled successfully.");
    }

    private static Path checkForCommandLinePath(String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return new File(args[0]).toPath();
        }

        throw new IllegalArgumentException("Please provide path to folder with your music!");
    }

    private static void initializeShuffle(int fileCount) {
        List<Integer> rangeOfNumbers = IntStream.range(1, fileCount + 1)
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(rangeOfNumbers);
        rangeOfNumbers.forEach(shuffle::push);
    }

    private static void checkIfMusicWasAlreadyShuffled(Path folder) {
        try {
            List<Path> dirty = Files.walk(folder, 1)
                    .filter(path -> Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS))
                    .collect(Collectors.toList());

            Pattern pattern = Pattern.compile("^([0-9]+\\.)+");

            dirty.forEach(data -> {
                cleaned.add(pattern.matcher(data.getFileName().toString()).replaceFirst(""));
                dirtyFileNames.add(data);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
