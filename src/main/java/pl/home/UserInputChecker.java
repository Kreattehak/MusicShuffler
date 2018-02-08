package pl.home;

import java.io.File;
import java.nio.file.Path;

class UserInputChecker {

    Path checkIfUserInputIsCorrect(String[] args) throws IllegalArgumentException {
        if (inputOnlyContainsOneArgument(args)) {
            return checkIfGivenPathLeadsToAFolder(args);
        } else {
            throw new IllegalArgumentException("Please provide only one cmd arg" +
                    " which is a path to folder with your music!");
        }
    }

    private boolean inputOnlyContainsOneArgument(String[] args) {
        return args.length == 1;
    }

    private Path checkIfGivenPathLeadsToAFolder(String[] args) throws IllegalArgumentException {
        int indexOfPathArgument = 0;
        File file = new File(args[indexOfPathArgument]);
        if (file.isDirectory()) {
            return file.toPath();
        }
        throw new IllegalArgumentException("Please provide a proper path to folder with your music!");
    }
}