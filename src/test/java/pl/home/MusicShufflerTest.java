package pl.home;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class MusicShufflerTest {

    private Path pathToTestFolder = Paths.get("src/test/resources/Music/");
    private String nonResolvablePathToFolder = "C:\\test\\test\\test";
    private String[] locationOfTestFolder = new String[]{pathToTestFolder.toAbsolutePath().toString()};
    private Pattern pattern = Pattern.compile("^([0-9]+\\.)+");
    private List<String> fileNames;
    private int fileCount;

    private MusicShuffler musicShuffler;

    @Parameterized.Parameters // Just to be sure that file names aren't mixed-up
    public static List<Object[]> data() {
        return Arrays.asList(new Object[50][0]);
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        fileNames = getFileNames();
        fileCount = getFileCount();
        musicShuffler = new MusicShuffler();
    }

    @Test
    public void shouldNotChangeTheCountOfFiles() {
        musicShuffler.shuffle(locationOfTestFolder);

        assertEquals(fileCount, getFileNames().size());
    }

    @Test
    public void fileShouldNotBeRenamedWithOtherFileName() {
        Map<String, Long> oldFilesLength = getFilesLength();

        musicShuffler.shuffle(locationOfTestFolder);

        Map<String, Long> newFilesLength = getFilesLength();

        oldFilesLength.forEach((key, value) -> {
            System.out.println(key);
            assertEquals(value, newFilesLength.get(key));
        });
    }

    @Test
    public void shouldAddRandomShufflePrefixToFileName() {
        musicShuffler.shuffle(locationOfTestFolder);

        List<String> newFileNames = getFileNames();
        newFileNames.removeAll(fileNames); // remove file names with the same shuffle number

        boolean areFilesShuffledByPrefix = newFileNames.isEmpty();
        for (String fileName : newFileNames) {
            areFilesShuffledByPrefix = pattern.matcher(fileName).find();
        }

        assertTrue(areFilesShuffledByPrefix);
    }

    @Test
    public void filesShouldBeRenamed() {
        Map<String, Long> oldModificationTimes = getFilesModificationTime();

        musicShuffler.shuffle(locationOfTestFolder);

        Map<String, Long> newModificationTimes = getFilesModificationTime();

        List<String> newFileNames = getFileNames();
        newFileNames.retainAll(fileNames);

        newFileNames.forEach(fileName -> {
            String nameWithoutPrefix = stripPrefix(fileName);
            long oldFileModTime = oldModificationTimes.get(nameWithoutPrefix);
            long newFileModTime = newModificationTimes.get(nameWithoutPrefix);
            assertTrue(oldFileModTime < newFileModTime);
        });
    }

    @Test
    public void shouldNotShuffleMusicInSubfolder() {
        Path pathToSubFolder = pathToTestFolder.resolve("some subfolder");
        try {
            List<String> subFolderFileNames = getFileNamesForFilesInSubfolder(pathToSubFolder);

            musicShuffler.shuffle(locationOfTestFolder);

            List<String> newSubFolderFileNames = getFileNamesForFilesInSubfolder(pathToSubFolder);
            subFolderFileNames.removeAll(newSubFolderFileNames);

            int expectedSize = 0;
            assertEquals(subFolderFileNames.size(), expectedSize);
        } catch (IOException e) {
            assertTrue("IOException occurred!", false);
        }
    }

    @Test
    public void shouldExitAfterPathWasNotFound() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("provide a proper path");
        musicShuffler.shuffle(new String[]{nonResolvablePathToFolder});
    }

    @Test
    public void shouldExitAfterUserPassMoreThanOneCommandLineArgument() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("provide only one");
        musicShuffler.shuffle(new String[]{nonResolvablePathToFolder, "test"});
    }

    private List<String> getFileNames() {
        List<String> fileNames = new ArrayList<>();
        try {
            DirectoryStream<Path> directoryStream = getAllPathsThatLeadToFile();
            for (Path path : directoryStream) {
                fileNames.add(path.getFileName().toString());
            }
        } catch (IOException ignored) {
        }

        return fileNames;
    }

    private Map<String, Long> getFilesLength() {
        return getFilesAttributes(path -> stripPrefix(path.toString()), path -> path.toFile().length());
    }

    private Map<String, Long> getFilesModificationTime() {
        return getFilesAttributes(path -> stripPrefix(path.toString()), path -> path.toFile().lastModified());
    }

    private Map<String, Long> getFilesAttributes(Function<Path, String> fileName, Function<Path, Long> fileAttributes) {
        Map<String, Long> filesWithAttributes = new HashMap<>();
        try {
            DirectoryStream<Path> directoryStream = getAllPathsThatLeadToFile();
            for (Path path : directoryStream) {
                filesWithAttributes.put(fileName.apply(path.getFileName()), fileAttributes.apply(path));
            }
        } catch (IOException ignored) {
        }

        return filesWithAttributes;
    }

    private List<String> getFileNamesForFilesInSubfolder(Path pathToSubFolder) throws IOException {
        int subFolderDepth = 1;
        return Files.walk(pathToSubFolder, subFolderDepth)
                .filter(path -> Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS))
                .map(path -> path.getFileName().toString())
                .collect(Collectors.toList());
    }

    private DirectoryStream<Path> getAllPathsThatLeadToFile() throws IOException {
        return Files.newDirectoryStream(pathToTestFolder, (data -> !data.toFile().isDirectory()));
    }

    private int getFileCount() {
        return fileNames.size();
    }

    private String stripPrefix(String fileName) {
        return pattern.matcher(fileName).replaceFirst("");
    }
}