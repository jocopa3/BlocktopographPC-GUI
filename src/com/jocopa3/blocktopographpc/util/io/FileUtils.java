/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.util.io;

import com.jocopa3.blocktopographpc.gui.Main;
import com.jocopa3.blocktopographpc.options.OptionEnum;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.JFileChooser;

/**
 *
 * @author Matt
 */
public class FileUtils {

    public static Path[] getFilesInDirectory(Path dir) {
        ArrayList<Path> files = new ArrayList<>();

        try {
            Files.walk(dir)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        System.out.println(path.toString());
                        files.add(path);
                    });
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }

        return files.toArray(new Path[0]);
    }

    public static void zipDirectory(Path source, Path destination) throws IOException {
        Path p = Files.createFile(destination);

        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(p))) {
            Files.walk(source)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(source.relativize(path).toString().replace("\\", "/"));

                        try {
                            zos.putNextEntry(zipEntry);
                            zos.write(Files.readAllBytes(path));
                            zos.closeEntry();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        }
    }
    
    public static void zipDirectory(String source, String destination) throws IOException {
        Path src = Paths.get(source);
        Path dest = Paths.get(destination);
        Path p = Files.createFile(dest);
        

        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(p))) {
            Files.walk(src)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(src.relativize(path).toString().replace("\\", "/"));

                        try {
                            zos.putNextEntry(zipEntry);
                            zos.write(Files.readAllBytes(path));
                            zos.closeEntry();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    public static void main(String[] args) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(Main.Options.get(OptionEnum.WORLD_FOLDER.getKeyName())));
        chooser.setDialogTitle("Select Folder to Zip");

        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                Path selected = new File(chooser.getSelectedFile().getPath()).toPath();
                zipDirectory(selected, selected.getParent().resolve(selected.getFileName() + ".zip"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
