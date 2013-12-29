package cpw.mods.fml.installer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpecBuilder;

public class SimpleInstaller {
    public static void main(String[] args) throws IOException {
        OptionParser parser = new OptionParser();
        OptionSpecBuilder helpOption = parser.acceptsAll(Arrays.asList(new String[] { "h", "help" }), "Help with this installer");
        OptionSet optionSet = parser.parse(args);
        if (optionSet.specs().size() > 0) {
            SimpleInstaller.handleOptions(parser, optionSet, helpOption);
        } else {
            SimpleInstaller.launchGui();
        }
    }

    private static void handleOptions(OptionParser parser, OptionSet optionSet, OptionSpecBuilder helpOption) throws IOException {
        parser.printHelpOn(System.err);
    }

    private static void launchGui() {
        String userHomeDir = System.getProperty("user.home", ".");
        String osType = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        File targetDir = null;
        String mcDir = ".minecraft";
        if ((osType.contains("win")) && (System.getenv("APPDATA") != null)) {
            targetDir = new File(System.getenv("APPDATA"), mcDir);
        } else if (osType.contains("mac")) {
            targetDir = new File(new File(new File(userHomeDir, "Library"), "Application Support"), "minecraft");
        } else {
            targetDir = new File(userHomeDir, mcDir);
        }

        try {
            VersionInfo.getVersionTarget();
        } catch (Throwable e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Corrupt download detected, cannot install", "Error", 0);
            return;
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}

        InstallerPanel panel = new InstallerPanel(targetDir);
        panel.run();
    }
}

/*
 * Location: C:\Users\John\Desktop\forge-1.6.4-9.11.1.953-installer (1).jar Qualified Name: cpw.mods.fml.installer.SimpleInstaller JD-Core Version:
 * 0.6.2
 */