package pl.home;

/**
 * Remember to provide path to folder with music as command line argument!
 * RadioMixer was tested only on Windows
 * File names can't begin with a number, all music files must be in specified folder, no subfolders allowed
 */
public class MainApp {
    public static void main(String[] args) {
        MusicShuffler musicShuffler = new MusicShuffler();
        musicShuffler.shuffle(args);
    }
}
