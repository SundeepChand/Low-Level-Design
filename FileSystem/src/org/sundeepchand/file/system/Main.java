package org.sundeepchand.file.system;

import java.util.ArrayList;
import java.util.List;

interface FileSystem {
    void ls();
}

class File implements FileSystem {
    private String name;

    public File(String name) {
        this.name = name;
    }

    public void ls() {
        System.out.println("F: " + name);
    }
}

class Directory implements FileSystem {
    private String name;
    private List<FileSystem> contents;

    public Directory(String name, List<FileSystem> contents) {
        this.name = name;
        this.contents = contents;
    }

    public void ls() {
        System.out.println("D: " + name);
        for (FileSystem content: contents) {
            content.ls();
        }
    }
}

public class Main {

    public static void main(String[] args) {
        List<FileSystem> comedyMovies = new ArrayList<>();
        comedyMovies.add(new File("movie2"));
        comedyMovies.add(new File("movie3"));

        List<FileSystem> moviesContents = new ArrayList<>();
        moviesContents.add(new File("movie1"));
        moviesContents.add(new Directory("comedy", comedyMovies));

        FileSystem root = new Directory("movies", moviesContents);
        root.ls();
    }

}
