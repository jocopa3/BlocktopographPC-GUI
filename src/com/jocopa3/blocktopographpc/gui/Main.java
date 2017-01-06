/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.gui;

import com.jocopa3.blocktopographpc.gui.windows.AboutWindow;
import com.sun.jna.Platform;
import com.jocopa3.blocktopographpc.gui.windows.WorldSelectWindow;
import javax.swing.JFrame;
import static com.jocopa3.blocktopographpc.gui.windows.WorldSelectWindow.MainWindow;
import com.jocopa3.blocktopographpc.options.Options;
import com.sun.jna.NativeLibrary;
import java.io.File;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

/**
 *
 * @author Matt
 */
public class Main {

    public static final ConcurrentHashMap<String, JFrame> OpenWindows = new ConcurrentHashMap<>();

    public static final Options Options = new Options();

    public static boolean checkIsOSSupported() {
        if (!Platform.isWindows()) {
            return false;
        }

        return true;
    }

    public static String getArchType() {
        if (Platform.is64Bit()) {
            return "64-bit";
        } else {
            return "32-bit";
        }
    }

    public static void setupNatives() {
        if (!checkIsOSSupported()) {
            JOptionPane.showMessageDialog(null, "Sorry, " + getArchType() + " " + System.getProperty("os.name") + " is not supported!", "Unsupported OS", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        String nativesFolder = File.separator + "natives" + File.separator;
        String suffix = Platform.is64Bit() ? "x64" : "x86";

        if (Platform.isWindows()) {
            nativesFolder += "windows" + File.separator + suffix + File.separator;
        }
        
        System.out.println((new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile()).getPath() + nativesFolder);
        
        NativeLibrary.addSearchPath("leveldb", (new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile()).getPath() + nativesFolder);
    }

    public static void main(String[] args) {
        setupNatives();

        /* Set the Nimbus look and feel
        try {
            //javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(WorldSelectWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(WorldSelectWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(WorldSelectWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(WorldSelectWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        */

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainWindow.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                MainWindow.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                        if (OpenWindows.size() > 1 && JOptionPane.showConfirmDialog(windowEvent.getWindow(),
                                "Closing this window closes all other open windows. Are you sure you want to close this window?", "Close All Windows?",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                            for (Map.Entry<String, JFrame> frame : OpenWindows.entrySet()) {
                                if (frame.getValue() instanceof CleanableComponent) {
                                    ((CleanableComponent) frame.getValue()).cleanUp();
                                }

                                frame.getValue().dispose();
                            }

                            MainWindow.dispose();
                        } else if (OpenWindows.size() <= 1) {
                            MainWindow.dispose();
                        }
                        
                        AboutWindow.disposeWindow();
                        
                        Options.saveOptions();
                        
                        System.exit(0);
                    }
                });

                MainWindow.pack();
                MainWindow.setLocationRelativeTo(null);
                MainWindow.setVisible(true);

                // There will only ever be one instance of WorldSelectWindow so give it any name
                OpenWindows.put("MainWindow", MainWindow);
            }
        });
    }
}
