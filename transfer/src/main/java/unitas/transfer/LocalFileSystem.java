package unitas.transfer;

import grool.transfer.FileSystem;
import java.io.File;
import java.net.URI;
import unitas.util.transfer.CioFileSystem;

/**
 *
 * @author javier
 */
public class LocalFileSystem implements FileSystem {

    @Override
    public void copy(URI source, URI destination)
            throws
            grool.transfer.FileSystemException {

        try {
            CioFileSystem.copy(source, destination);
        } catch (java.io.IOException ex) {
            throw new grool.transfer.FileSystemException(ex);
        }
    }

    @Override
    public void copyTo(URI source, URI destination)
            throws
            grool.transfer.FileSystemException {

        try {
            CioFileSystem.copyTo(source, destination);
        } catch (java.io.IOException ex) {
            throw new grool.transfer.FileSystemException(ex);
        }
    }

    @Override
    public void copyDirectory(URI source, URI destination)
            throws
            grool.transfer.FileSystemException {

        try {
            CioFileSystem.copyDirectory(source, destination);
        } catch (java.io.IOException ex) {
            throw new grool.transfer.FileSystemException(ex);
        }
    }

    @Override
    public void copyDirectoryTo(URI source, URI destination)
            throws
            grool.transfer.FileSystemException {

        try {
            CioFileSystem.copyDirectoryTo(source, destination);
        } catch (java.io.IOException ex) {
            throw new grool.transfer.FileSystemException(ex);
        }
    }

    @Override
    public boolean exists(URI file)
            throws
            grool.transfer.FileSystemException {

        File _file = new File(file);

        return _file.exists();
    }

    @Override
    public long getModificationTime(URI file)
            throws
            grool.transfer.FileSystemException {

        File _file = new File(file);

        return _file.lastModified();
    }
}
