package cpw.mods.fml.installer;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.google.common.base.Throwables;

public class InstallerPanel extends JPanel {
    private static final long serialVersionUID = 159357286385544209L;
    private static final String STR_INSTALL = "Install", STR_CANCEL = "Cancel";
    
    private File              targetDir;
    private final ButtonGroup choiceButtonGroup;
    private final JTextField  selectedDirText;
    private final JLabel      infoLabel;
    private JDialog           dialog;
    private final JButton     sponsorButton;
    private final JPanel      sponsorPanel;
    private final JPanel      fileEntryPanel;

    public InstallerPanel(File targetDir) {
        setLayout(new BoxLayout(this, 1));
        BufferedImage image;
        try {
            image = ImageIO.read(SimpleInstaller.class.getResourceAsStream(VersionInfo.getLogoFileName()));
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }

        JPanel logoSplash = new JPanel();
        logoSplash.setLayout(new BoxLayout(logoSplash, 1));
        ImageIcon icon = new ImageIcon(image);
        JLabel logoLabel = new JLabel(icon);
        logoLabel.setAlignmentX(0.5F);
        logoLabel.setAlignmentY(0.5F);
        logoLabel.setSize(image.getWidth(), image.getHeight());
        logoSplash.add(logoLabel);
        JLabel tag = new JLabel(VersionInfo.getWelcomeMessage());
        tag.setAlignmentX(0.5F);
        tag.setAlignmentY(0.5F);
        logoSplash.add(tag);
        tag = new JLabel(VersionInfo.getVersion());
        tag.setAlignmentX(0.5F);
        tag.setAlignmentY(0.5F);
        logoSplash.add(tag);

        logoSplash.setAlignmentX(0.5F);
        logoSplash.setAlignmentY(0.0F);
        add(logoSplash);

        this.sponsorPanel = new JPanel();
        this.sponsorPanel.setLayout(new BoxLayout(this.sponsorPanel, 0));
        this.sponsorPanel.setAlignmentX(0.5F);
        this.sponsorPanel.setAlignmentY(0.5F);

        this.sponsorButton = new JButton();
        this.sponsorButton.setAlignmentX(0.5F);
        this.sponsorButton.setAlignmentY(0.5F);
        this.sponsorButton.setBorderPainted(false);
        this.sponsorButton.setOpaque(false);
        this.sponsorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(InstallerPanel.this.sponsorButton.getToolTipText()));
                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            InstallerPanel.this.dialog.toFront();
                            InstallerPanel.this.dialog.requestFocus();
                        }
                    });
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(InstallerPanel.this, "An error occurred launching the browser", "Error launching browser", 0);
                }
            }
        });
        this.sponsorPanel.add(this.sponsorButton);

        add(this.sponsorPanel);

        this.choiceButtonGroup = new ButtonGroup();

        JPanel choicePanel = new JPanel();
        choicePanel.setLayout(new BoxLayout(choicePanel, 1));
        boolean first = true;
        SelectButtonAction sba = new SelectButtonAction();
        for (InstallerAction action : InstallerAction.values()) {
            JRadioButton radioButton = new JRadioButton();
            radioButton.setAction(sba);
            radioButton.setText(action.getButtonLabel());
            radioButton.setActionCommand(action.name());
            radioButton.setToolTipText(action.getTooltip());
            radioButton.setSelected(first);
            radioButton.setAlignmentX(0.0F);
            radioButton.setAlignmentY(0.5F);
            this.choiceButtonGroup.add(radioButton);
            choicePanel.add(radioButton);
            first = false;
        }

        choicePanel.setAlignmentX(1.0F);
        choicePanel.setAlignmentY(0.5F);
        // add(choicePanel);
        
        JPanel entryPanel = new JPanel();
        entryPanel.setLayout(new BoxLayout(entryPanel, 0));

        this.targetDir = targetDir;
        this.selectedDirText = new JTextField();
        this.selectedDirText.setEditable(false);
        this.selectedDirText.setToolTipText("Path to minecraft");
        this.selectedDirText.setColumns(30);

        entryPanel.add(this.selectedDirText);
        JButton dirSelect = new JButton();
        dirSelect.setAction(new FileSelectAction());
        dirSelect.setText("...");
        dirSelect.setToolTipText("Select an alternative minecraft directory");
        entryPanel.add(dirSelect);

        entryPanel.setAlignmentX(0.0F);
        entryPanel.setAlignmentY(0.0F);
        this.infoLabel = new JLabel();
        this.infoLabel.setHorizontalTextPosition(2);
        this.infoLabel.setVerticalTextPosition(1);
        this.infoLabel.setAlignmentX(0.0F);
        this.infoLabel.setAlignmentY(0.0F);
        this.infoLabel.setForeground(Color.RED);
        this.infoLabel.setVisible(false);

        this.fileEntryPanel = new JPanel();
        this.fileEntryPanel.setLayout(new BoxLayout(this.fileEntryPanel, 1));
        this.fileEntryPanel.add(this.infoLabel);
        this.fileEntryPanel.add(Box.createVerticalGlue());
        this.fileEntryPanel.add(entryPanel);
        this.fileEntryPanel.setAlignmentX(0.5F);
        this.fileEntryPanel.setAlignmentY(0.0F);
        add(this.fileEntryPanel);
        updateFilePath();
    }

    private void updateFilePath() {
        try {
            this.targetDir = this.targetDir.getCanonicalFile();
            this.selectedDirText.setText(this.targetDir.getPath());
        } catch (IOException e) {}

        InstallerAction action = InstallerAction.valueOf(this.choiceButtonGroup.getSelection().getActionCommand());
        boolean valid = action.isPathValid(this.targetDir);

        String sponsorMessage = action.getSponsorMessage();
        if (sponsorMessage != null) {
            this.sponsorButton.setText(sponsorMessage);
            this.sponsorButton.setToolTipText(action.getSponsorURL());
            if (action.getSponsorLogo() != null) {
                this.sponsorButton.setIcon(action.getSponsorLogo());
            } else {
                this.sponsorButton.setIcon(null);
            }
            this.sponsorPanel.setVisible(true);
        } else {
            this.sponsorPanel.setVisible(false);
        }
        if (valid) {
            this.selectedDirText.setForeground(Color.BLACK);
            this.infoLabel.setVisible(false);
            this.fileEntryPanel.setBorder(null);
        } else {
            this.selectedDirText.setForeground(Color.RED);
            this.fileEntryPanel.setBorder(new LineBorder(Color.RED));
            this.infoLabel.setText("<html>" + action.getFileError(this.targetDir) + "</html>");
            this.infoLabel.setVisible(true);
        }
        if (this.dialog != null) {
            this.dialog.invalidate();
            this.dialog.pack();
        }
    }

    public void run() {
        JOptionPane optionPane = new JOptionPane(this, -1, 2, null, new Object[] {STR_INSTALL, STR_CANCEL});

        Frame emptyFrame = new Frame("Mod system installer");
        emptyFrame.setUndecorated(true);
        emptyFrame.setVisible(true);
        emptyFrame.setLocationRelativeTo(null);
        this.dialog = optionPane.createDialog(emptyFrame, "Mod system installer");
        this.dialog.setDefaultCloseOperation(2);
        this.dialog.setVisible(true);
        
        String result = (String) (optionPane.getValue() != null ? optionPane.getValue() : STR_CANCEL);
        if (result.equals(STR_INSTALL)) {
            InstallerAction action = InstallerAction.valueOf(this.choiceButtonGroup.getSelection().getActionCommand());
            if (action.run(this.targetDir)) {
                JOptionPane.showMessageDialog(null, action.getSuccessMessage(), "Complete", 1);
            }
        }
        this.dialog.dispose();
        emptyFrame.dispose();
    }

    private class SelectButtonAction extends AbstractAction {
        /**
     * 
     */
        private static final long serialVersionUID = 4654943783153829781L;

        private SelectButtonAction() {}

        @Override
        public void actionPerformed(ActionEvent e) {
            InstallerPanel.this.updateFilePath();
        }
    }

    private class FileSelectAction extends AbstractAction {
        /**
     * 
     */
        private static final long serialVersionUID = -6813315289431296052L;

        private FileSelectAction() {}

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser dirChooser = new JFileChooser();
            dirChooser.setFileSelectionMode(1);
            dirChooser.setFileHidingEnabled(false);
            dirChooser.ensureFileIsVisible(InstallerPanel.this.targetDir);
            dirChooser.setSelectedFile(InstallerPanel.this.targetDir);
            int response = dirChooser.showOpenDialog(InstallerPanel.this);
            switch (response) {
            case 0:
                InstallerPanel.this.targetDir = dirChooser.getSelectedFile();
                InstallerPanel.this.updateFilePath();
                break;
            }
        }
    }
}

/*
 * Location: C:\Users\John\Desktop\forge-1.6.4-9.11.1.953-installer (1).jar Qualified Name: cpw.mods.fml.installer.InstallerPanel JD-Core Version:
 * 0.6.2
 */