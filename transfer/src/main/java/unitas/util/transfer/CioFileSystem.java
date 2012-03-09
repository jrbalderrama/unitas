package unitas.util.transfer;

import java.io.File;
import java.net.URI;
import org.apache.commons.io.FileUtils;

/**
 * Class to copy files on local systems (file scheme)
 * This class is a wrapper of the org.apache.commons.io.FileUtils class.
 * The goal of this wrapping is provide standarized signatures of copying.
 * @author javier
 */
public class CioFileSystem {

    /**
     * Copies a file to a new location file. This method copies the contents of
     * the specified source file to the specified destination file. The
     * directory holding the destination file is created if it does not exist.
     * If the destination file exists, then this method will overwrite it.
     * @param source an existing file to copy
     * @param destination the new location file
     * @throws java.io.IOException if an IO error occurs during copying
     */
    public static void copy(URI source, URI destination)
            throws
            java.io.IOException {

        File _source = new File(source);
        File _destination = new File(destination);
        FileUtils.copyFile(_source, _destination);
    }

    /**
     * Copies a file to a directory. This method copies the contents of the
     * specified source file to a file of the same name in the specified
     * destination directory. The destination directory is created if it does
     * not exist. If the destination file exists, then this method will
     * overwrite it.
     * @param source an existing file to copy
     * @param destination the directory to place the copy in
     * @throws java.io.IOException if an IO error occurs during copying
     */
    public static void copyTo(URI source, URI destination)
            throws
            java.io.IOException {

        File _source = new File(source);
        File _destination = new File(destination);
        FileUtils.copyFileToDirectory(_source, _destination);
    }

    /**
     * Copies a whole directory contents to a new location. This method copies
     * the specified directory contents (child directories and files) to the
     * specified destination. The destination is the new location and name
     * of the directory contents. The destination directory is created if it
     * does not exist. If the destination directory did exist, then this method
     * merges the source with the destination, with the source taking precedence.
     * @param source an existing directory to copy
     * @param destination the new directory to copy in
     * @throws java.io.IOException if an IO error occurs during copying
     */
    public static void copyDirectory(URI source, URI destination)
            throws
            java.io.IOException {

        File _source = new File(source);
        File _destination = new File(destination);
        FileUtils.copyDirectory(_source, _destination);
    }

    /**
     * Copies a directory to within another directory. This method copies the
     * source directory and all its contents to a directory of the same name in
     * the specified destination directory. The destination directory is created
     * if it does not exist. If the destination directory did exist, then this
     * method merges the source with the destination, with the source taking
     * precedence.
     * @param source an existing directory to copy
     * @param destination  the directory to place the copy in
     * @throws java.io.IOException if an IO error occurs during copying
     */
    public static void copyDirectoryTo(URI source, URI destination)
            throws
            java.io.IOException {

        File _source = new File(source);
        File _destination = new File(destination);
        //copyDirectory(srcDir, new File(destDir, srcDir.getName()), true);
        FileUtils.copyDirectoryToDirectory(_source, _destination);
    }

    static void copyWeb(URI source, URI destination)
            throws
            java.io.IOException {

        FileUtils.copyURLToFile(source.toURL(), new File(destination));
    }
}
