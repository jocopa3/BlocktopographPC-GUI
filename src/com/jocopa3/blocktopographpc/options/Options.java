/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.options;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Matt
 */
public class Options {

    private final ConcurrentHashMap<String, String> options;
    private final File optionsFile;

    public Options() {
        options = new ConcurrentHashMap<>();
        File temp;
        try {
            File optionsFolder = new File(Options.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile();
            temp = new File(optionsFolder.getPath() + File.separator + "options.txt");
            System.out.println(temp.getPath());

            if (!temp.exists()) {
                try {
                    temp.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(Options.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } catch (URISyntaxException ex) {
            Logger.getLogger(Options.class.getName()).log(Level.SEVERE, null, ex);
            temp = null;
        }

        optionsFile = temp;
        readOptions();
    }

    public Options(String optionsFileLocation) {
        options = new ConcurrentHashMap<>();
        optionsFile = new File(optionsFileLocation);

        readOptions();
    }

    public Options(File optionsFile) {
        options = new ConcurrentHashMap<>();
        this.optionsFile = optionsFile;

        readOptions();
    }

    public void readOptions() {
        if (!optionsFile.exists()) {
            return;
        }

        BufferedReader in = null;

        options.clear();

        try {
            in = new BufferedReader(new FileReader(optionsFile));
            String line;

            while ((line = in.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }

                String[] optionsData = line.split(":", 2);

                switch (optionsData.length) {
                    default:
                        continue;
                    case 2:
                        // Ignore empty keys
                        if (!optionsData[0].replaceAll("\\s", "").isEmpty()) {
                            options.put(optionsData[0].trim(), optionsData[1]);
                        }
                }
            }

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (options.isEmpty()) {
            loadDefaultOptions();
        }
    }

    public void saveOptions() {
        if (options.isEmpty()) {
            return;
        }

        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(optionsFile));

            Set<Entry<String, String>> pairs = options.entrySet();
            for (Entry<String, String> option : pairs) {
                out.write(option.getKey() + ":" + option.getValue());
                out.newLine();
            }

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Options set(OptionEnum option) {
        options.put(option.getKeyName(), option.getDefaultValue());
        
        return this;
    }
    
    public Options set(String key, String value) {
        options.put(key, value);

        return this;
    }

    public String get(String key) {
        return options.get(key);
    }

    public void loadDefaultOptions() {
        set(OptionEnum.WORLD_FOLDER);
        //set("cache_policy", Integer.toString(com.protolambda.blocktopograph.UsageLevel.STRICT.id));
        //set("cpu_use_policy", Integer.toString(com.protolambda.blocktopograph.UsageLevel.STRICT.id));
    }
}
