package unitas.util.transfer;

import java.net.URI;
import static org.apache.commons.vfs.Selectors.EXCLUDE_SELF;
import static org.apache.commons.vfs.Selectors.SELECT_SELF;
import org.apache.commons.vfs.*;
import org.apache.commons.vfs.auth.StaticUserAuthenticator;
import org.apache.commons.vfs.impl.DefaultFileSystemConfigBuilder;
import org.apache.commons.vfs.provider.ftp.FtpFileSystemConfigBuilder;
import org.apache.commons.vfs.provider.sftp.SftpFileSystemConfigBuilder;

/**
 *
 * @author javier
 */
public class VfsFileSystem {

    public static void copy(URI source, URI destination)
            throws
            org.apache.commons.vfs.FileSystemException {

        copy(source, destination, null, null);
    }

    public static void copyTo(URI source, URI destination)
            throws
            org.apache.commons.vfs.FileSystemException {

        copyTo(source, destination, null, null);
    }

    public static void copyDirectory(URI source, URI destination)
            throws
            org.apache.commons.vfs.FileSystemException {

        copyDirectory(source, destination, null, null);
    }

    public static void copyDirectoryTo(URI source, URI destination)
            throws
            org.apache.commons.vfs.FileSystemException {

        copyDirectoryTo(source, destination, null, null);
    }

    public static void copy(URI source, URI destination, String login, String password)
            throws
            org.apache.commons.vfs.FileSystemException {

        String _source = source.toString();
        String _destination = destination.toString();
        if (login == null) {
            VfsFileSystem.copyFrom(_source, _destination, SELECT_SELF);
        } else {
            VfsFileSystem.copyFrom(_source, _destination, SELECT_SELF, login, password);
        }
    }

    public static void copyTo(URI source, URI destination, String login, String password)
            throws
            org.apache.commons.vfs.FileSystemException {

        String _source = source.toString();
        String _destination = destination.toString();
        _destination += (_destination.endsWith("/") ? "" : "/")
                + _source.substring(_source.lastIndexOf("/"));
        if (login == null) {
            VfsFileSystem.copyFrom(_source, _destination, SELECT_SELF);
        } else {
            VfsFileSystem.copyFrom(_source, _destination, SELECT_SELF, login, password);
        }
    }

    public static void copyDirectory(URI source, URI destination, String login, String password)
            throws
            org.apache.commons.vfs.FileSystemException {

        String _source = source.toString();
        String _destination = destination.toString();
        // VfsFileSystem.copyFrom(_source, _destination, SELECT_ALL);
        if (login == null) {
            VfsFileSystem.copyFrom(_source, _destination, EXCLUDE_SELF);
        } else {
            VfsFileSystem.copyFrom(_source, _destination, EXCLUDE_SELF, login, password);
        }
    }

    public static void copyDirectoryTo(URI source, URI destination, String login, String password)
            throws
            org.apache.commons.vfs.FileSystemException {

        String _source = source.toString();
        String _destination = destination.toString();
        _destination += (_destination.endsWith("/") ? "" : "/")
                + _source.substring(_source.lastIndexOf("/"));
        if (login == null) {
            VfsFileSystem.copyFrom(_source, _destination, EXCLUDE_SELF);
        } else {
            VfsFileSystem.copyFrom(_source, _destination, EXCLUDE_SELF, login, password);
        }
    }

    public static boolean exists(URI file)
            throws
            org.apache.commons.vfs.FileSystemException {

        return VfsFileSystem.exists(file, null, null);
    }

    public static boolean exists(URI file, String login, String password)
            throws
            org.apache.commons.vfs.FileSystemException {

        boolean exists = false;
        String _file = file.toString();
        FileSystemManager manager = VFS.getManager();
        FileSystemOptions options = VfsFileSystem.resolveOptions(_file, login, password);
        FileObject oFile = manager.resolveFile(_file, options);
        try {
            exists = oFile.exists();
        } finally {
            return exists;
        }
    }

    public static long getModificationTime(URI file)
            throws
            org.apache.commons.vfs.FileSystemException {

        return VfsFileSystem.getModificationTime(file, null, null);
    }

    public static long getModificationTime(URI file, String login, String password)
            throws
            org.apache.commons.vfs.FileSystemException {

        long time = 0L;
        String _file = file.toString();
        FileSystemManager manager = VFS.getManager();
        FileSystemOptions options = VfsFileSystem.resolveOptions(_file, login, password);
        FileObject oFile = manager.resolveFile(_file, options);
        try {
            time = oFile.getContent().getLastModifiedTime();
        } finally {
            return time;
        }
    }

    private static void copyFrom(String source, String destination, FileSelector selector)
            throws
            org.apache.commons.vfs.FileSystemException {

        VfsFileSystem.copyFrom(source, destination, selector, null, null);
    }

    private static void copyFrom(String source, String destination, FileSelector selector, String login, String password)
            throws
            org.apache.commons.vfs.FileSystemException {

        if (destination.startsWith("http") || destination.startsWith("https")) {
            throw new org.apache.commons.vfs.FileSystemException("Destination file system is read-only!");
        }

        FileSystemManager manager = VFS.getManager();
        FileObject _source = source.startsWith("file")
                ? manager.resolveFile(source)
                : manager.resolveFile(source, VfsFileSystem.resolveOptions(source, login, password));
        FileObject _destination = destination.startsWith("file")
                ? manager.resolveFile(destination)
                : manager.resolveFile(destination, VfsFileSystem.resolveOptions(destination, login, password));
        if (_source.isReadable()) {
            _destination.copyFrom(_source, selector);
        }
    }

    private static FileSystemOptions resolveOptions(String resource, String login, String password)
            throws
            org.apache.commons.vfs.FileSystemException {

        FileSystemOptions options = new FileSystemOptions();
        if (resource.startsWith("ftp")) {

            FtpFileSystemConfigBuilder.getInstance().setPassiveMode(options, true);
            if (login != null && password != null) {

                URI domain = URI.create(resource);
                UserAuthenticator authenticator = new StaticUserAuthenticator(domain.getHost(), login, password);
                DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(options, authenticator);
            }
        }

        if (resource.startsWith("sftp")) {
            SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(options, "no");
        }

        if (resource.startsWith("https")) {
            // accept self-signed certificates
            OpenTrustProvider.setAlwaysTrust();
        }

        return options;
    }
}
