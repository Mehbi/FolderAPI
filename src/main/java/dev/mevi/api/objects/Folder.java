package dev.mevi.api.objects;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Arrays;
import java.util.List;

public class Folder {
    private final String directory;


    private Folder(String directory) {
        this.directory = directory;
    }

    public static Folder from(String directory) {
        return new Folder(directory);
    }
    public File newFile(String name, String extension) throws IOException {
        File file = new File(directory, name + "." + extension);
        if (contains(file)) throw new FileAlreadyExistsException(file.getName());
        if (file.createNewFile()) System.out.println(file.getName() + " has been created");
        return file;
    }
    public Folder newDirectory(String directoryName) throws FileAlreadyExistsException {
        File file = new File(directory, directoryName);
        if (contains(file.getName())) throw new FileAlreadyExistsException(file.getName());

        if (file.mkdir()) System.out.println(file.getName() + " has been created");
        return Folder.from(directoryName);
    }

    public boolean contains(String fileName, String extension) {
        return files().stream().filter(File::isFile).anyMatch(file -> file.getName().equals(fileName) && Files.getFileExtension(file.getName()).equals(extension));
    }
    public boolean contains(String fileName) {
        return files().stream().anyMatch(file -> file.getName().equals(fileName));
    }
    public boolean contains(File file) {
        return files().contains(file);
    }

    public List<File> filteredByName(String name) {
        return files().stream().filter(file -> file.getName().equals(name)).toList();
    }
    public List<File> filteredByExtension(String extension) {
        return files().stream().filter(file -> Files.getFileExtension(file.getName()).equals(extension)).toList();
    }
    private List<File> files() {
        File file = new File(directory);
        return Arrays.stream(file.listFiles()).toList();
    }
    public String getDirectory() {
        return directory;
    }
}
