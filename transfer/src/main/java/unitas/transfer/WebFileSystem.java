package unitas.transfer;

import grool.transfer.FileSystem;
import java.net.URI;
import unitas.util.transfer.VfsFileSystem;

/**
 *
 * @author javier
 */
public class WebFileSystem implements FileSystem {

    private String login;
    private String password;

    public WebFileSystem() {
        this(null, null);
    }

    public WebFileSystem(String login, String password) {

        this.login = login;
        this.password = password;
    }

    @Override
    public void copy(URI source, URI destination)
            throws
            grool.transfer.FileSystemException {

        WebFileSystem.validateWriteAccessOfDestinationThrowsProtocol(destination);
        try {
            if (login == null) {
                VfsFileSystem.copy(source, destination);
            } else {
                VfsFileSystem.copy(source, destination, login, password);
            }
        } catch (org.apache.commons.vfs.FileSystemException ex) {
            throw new grool.transfer.FileSystemException(ex.getMessage(), ex);
        }
    }

    @Override
    public void copyTo(URI source, URI destination)
            throws
            grool.transfer.FileSystemException {

        WebFileSystem.validateWriteAccessOfDestinationThrowsProtocol(destination);
        try {
            if (login == null) {
                VfsFileSystem.copyTo(source, destination);
            } else {
                VfsFileSystem.copyTo(source, destination, login, password);
            }
        } catch (org.apache.commons.vfs.FileSystemException ex) {
            throw new grool.transfer.FileSystemException(ex.getMessage(), ex);
        }
    }

    @Override
    public void copyDirectory(URI source, URI destination)
            throws
            grool.transfer.FileSystemException {

        WebFileSystem.validateWriteAccessOfDestinationThrowsProtocol(destination);
        try {
            if (login == null) {
                VfsFileSystem.copyDirectory(source, destination);
            } else {
                VfsFileSystem.copyDirectory(source, destination, login, password);
            }
        } catch (org.apache.commons.vfs.FileSystemException ex) {
            throw new grool.transfer.FileSystemException(ex.getMessage(), ex);
        }
    }

    @Override
    public void copyDirectoryTo(URI source, URI destination)
            throws
            grool.transfer.FileSystemException {

        WebFileSystem.validateWriteAccessOfDestinationThrowsProtocol(destination);
        try {
            if (login == null) {
                VfsFileSystem.copyDirectoryTo(source, destination);
            } else {
                VfsFileSystem.copyDirectoryTo(source, destination, login, password);
            }
        } catch (org.apache.commons.vfs.FileSystemException ex) {
            throw new grool.transfer.FileSystemException(ex.getMessage(), ex);
        }
    }

    @Override
    public boolean exists(URI file)
            throws
            grool.transfer.FileSystemException {

        boolean exists = false;
        try {
            if (login == null) {
                exists = VfsFileSystem.exists(file);
            } else {
                exists = VfsFileSystem.exists(file, login, password);
            }
        } finally {
            return exists;
        }
    }

    @Override
    public long getModificationTime(URI file)
            throws
            grool.transfer.FileSystemException {

        long time = 0L;
        try {
            if (login == null) {
                time = VfsFileSystem.getModificationTime(file);
            } else {
                time = VfsFileSystem.getModificationTime(file, login, password);
            }
        } finally {
            return time;
        }
    }

    static void validateWriteAccessOfDestinationThrowsProtocol(URI destination)
            throws
            grool.transfer.FileSystemException {

        String scheme = destination.getScheme();
        if (scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https")) {
            throw new grool.transfer.FileSystemException("Destination defines a read-only protocol!");
        }
    }
}
