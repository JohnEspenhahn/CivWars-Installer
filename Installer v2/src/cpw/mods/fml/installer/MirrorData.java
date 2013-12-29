package cpw.mods.fml.installer;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public enum MirrorData {
    INSTANCE;

    private final List<Mirror> mirrors;
    private int                chosenMirror;

    private MirrorData() {
        if (VersionInfo.hasMirrors()) {
            this.mirrors = buildMirrorList();
            if (!this.mirrors.isEmpty()) {
                this.chosenMirror = new Random().nextInt(getAllMirrors().size());
            }
        } else {
            this.mirrors = Collections.emptyList();
        }
    }

    private List<Mirror> buildMirrorList() {
        String url = VersionInfo.getMirrorListURL();
        List<Mirror> results = Lists.newArrayList();
        List<String> mirrorList = DownloadUtils.downloadList(url);
        Splitter splitter = Splitter.on('!').trimResults();
        for (String mirror : mirrorList) {
            String[] strings = Iterables.toArray(splitter.split(mirror), String.class);
            Mirror m = new Mirror(strings[0], strings[1], strings[2], strings[3]);
            results.add(m);
        }
        return results;
    }

    public boolean hasMirrors() {
        return (VersionInfo.hasMirrors()) && (this.mirrors != null) && (!this.mirrors.isEmpty());
    }

    private List<Mirror> getAllMirrors() {
        return this.mirrors;
    }

    private Mirror getChosen() {
        return getAllMirrors().get(this.chosenMirror);
    }

    public String getMirrorURL() {
        return getChosen().url;
    }

    public String getSponsorName() {
        return getChosen().name;
    }

    public String getSponsorURL() {
        return getChosen().clickURL;
    }

    public Icon getImageIcon() {
        return getChosen().getImage();
    }

    private static class Mirror {
        final String name;
        final String imageURL;
        final String clickURL;
        final String url;
        boolean      triedImage;
        Icon         image;

        public Mirror(String name, String imageURL, String clickURL, String url) {
            this.name = name;
            this.imageURL = imageURL;
            this.clickURL = clickURL;
            this.url = url;
        }

        Icon getImage() {
            if (!this.triedImage) {
                try {
                    this.image = new ImageIcon(ImageIO.read(new URL(this.imageURL)));
                } catch (Exception e) {
                    this.image = null;
                } finally {
                    this.triedImage = true;
                }
            }
            return this.image;
        }
    }
}

/*
 * Location: C:\Users\John\Desktop\forge-1.6.4-9.11.1.953-installer (1).jar Qualified Name: cpw.mods.fml.installer.MirrorData JD-Core Version: 0.6.2
 */