package com.onejli;

import ij.ImagePlus;
import ij.io.FileSaver;
import ij.plugin.ContrastEnhancer;
import ij.plugin.ImageCalculator;

import java.io.IOException;

import static ij.io.FileSaver.setJpegQuality;

/*
 * ELA
 * http://staff.science.uva.nl/~delaat/rp/2011-2012/p24/report.pdf
 * http://fotoforensics.com/tutorial-ela.php
 * http://fotoforensics.com/sample-ela.php
 * http://blackhat.com/presentations/bh-dc-08/Krawetz/Whitepaper/bh-dc-08-krawetz-WP.pdf
 *
 * ImageJ
 * http://rsbweb.nih.gov/ij/
 * http://www.rierol.net/imagej_programming_tutorials.html
 *
 */
public class ela {
    public ela(String imageLocation, int quality) throws IOException {
        ImagePlus orig = new ImagePlus(imageLocation);

        String basePath = System.getProperty("user.dir") + "/" + orig.getTitle();
        String origPath = basePath + "-original.jpg";
        String resavedPath = basePath + "-resaved.jpg";
        String elaPath = basePath + "-ELA.png";

        FileSaver fs = new FileSaver(orig);
        setJpegQuality(100);
        fs.saveAsJpeg(origPath);

        setJpegQuality(quality);
        fs.saveAsJpeg(resavedPath);
        ImagePlus resaved = new ImagePlus(resavedPath);

        //http://rsbweb.nih.gov/ij/docs/guide/146-29.html#toc-Subsection-29.13
        ImageCalculator calc = new ImageCalculator();
        ImagePlus diff = calc.run("create difference", orig, resaved);
        diff.setTitle("ELA @ " + quality + "%");

        //http://rsbweb.nih.gov/ij/docs/guide/146-29.html#toc-Subsection-29.5
        ContrastEnhancer c = new ContrastEnhancer();
        c.stretchHistogram(diff, 0.2);

        fs = new FileSaver(diff);
        fs.saveAsPng(elaPath);

        orig.show();
        diff.show();
    }

    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            new ela(args[0], 95);
        } else {
            new ela("http://fotoforensics.com/img/books-orig.jpg", 95);
        }
    }
}