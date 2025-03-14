package dev.mevi.api.objects;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Folder {
    private final String directory;

    private Folder(String directory) {
        this.directory = directory;
    }

    public static Folder from(String directory) {
        return new Folder(directory);
    }
    public File newFile(String name, String extension) throws IOException {
        File file = new File(this.directory, name + "." + extension);
        if (contains(file)) throw new FileAlreadyExistsException(file.getName());
        if (file.createNewFile()) System.out.println(file.getName() + " has been created");
        return file;
    }
    public Folder newDirectory(String directoryName) {
        File file = new File(this.directory, directoryName);
        if (contains(file.getName())) {
            System.out.println("This directory already exists");
            return null;
        }

        file.mkdirs();
        System.out.println(file.getName() + " has been created");
        return Folder.from(this.directory + "/" + directoryName);
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
    public void delete(String name, String extension) {
        Optional<File> fileFiltered = filteredByName(name).stream().filter(file -> Files.getFileExtension(name).equals(extension)).findFirst();
        if (fileFiltered.isEmpty()) {
            System.out.println(name + "." + extension + "could not be deleted, because it does not exists");
            return;
        }
        fileFiltered.get().delete();
    }
    private List<File> files() {
        File file = new File(this.directory);
        return file.listFiles() != null ? Arrays.stream(file.listFiles()).toList() : new ArrayList<>();
    }
    public String directory() {
        return this.directory;
    }
}
