package unitas.transfer;

import grool.access.UserCredentials;
import grool.configuration.GroolDefaultValues;
import grool.transfer.FileSystem;
import java.io.File;
import java.net.URI;
import java.util.List;

/**
 * High level class to copy elements using all APIs depending the URI scheme.
 * The copy method assumes that either source or destination has to declares
 * a 'file' schema.
 * @author javier
 */
public class UnitasFileSystem implements FileSystem {

    private String login;
    private String password;
    private File proxy;
    private String virtualOrganization;
    private List<String> storageElementsForReplicas;
    
    /**
     * Constructor when proxy is not needed (for URI scheme: HTTP, HTTPS, FTP)
     */
    public UnitasFileSystem() {
        this(null);
    }

    public UnitasFileSystem(File proxy) {
        this(proxy, GroolDefaultValues.getDefaultVirtualOrganization());
    }

    public UnitasFileSystem(File proxy, String virtualOrganization) {
        this(proxy, virtualOrganization, null);
    }

    public UnitasFileSystem(File proxy, String virtualOrganization, UserCredentials credentials) {

        this.proxy = proxy;
        this.virtualOrganization = virtualOrganization;
        if (credentials != null) {

            login = credentials.getLogin();
            password = credentials.getPassword();
        }
    }

    public void setStorageElementsForReplicas(List<String> storageElementsForReplicas) {
        this.storageElementsForReplicas = storageElementsForReplicas;
    }    
    
    @Override
    public void copy(URI source, URI destination)
            throws
            grool.transfer.FileSystemException {

        FileSystem fileSystem = this.getFileSystem(source, destination);
        fileSystem.copy(source, destination);
    }

    @Override
    public void copyTo(URI source, URI destination)
            throws
            grool.transfer.FileSystemException {

        FileSystem fileSystem = this.getFileSystem(source, destination);
        fileSystem.copyTo(source, destination);
    }

    @Override
    public void copyDirectory(URI source, URI destination)
            throws
            grool.transfer.FileSystemException {

        FileSystem fileSystem = this.getFileSystem(source, destination);
        fileSystem.copyDirectory(source, destination);
    }

    @Override
    public void copyDirectoryTo(URI source, URI destination)
            throws
            grool.transfer.FileSystemException {

        FileSystem fileSystem = this.getFileSystem(source, destination);
        fileSystem.copyDirectoryTo(source, destination);
    }

    @Override
    public boolean exists(URI file)
            throws
            grool.transfer.FileSystemException {

        FileSystem fileSystem = this.getFileSystem(file, file);
        return fileSystem.exists(file);
    }

    @Override
    public long getModificationTime(URI file)
            throws
            grool.transfer.FileSystemException {

        FileSystem fileSystem = this.getFileSystem(file, file);
        return fileSystem.getModificationTime(file);
    }

    private FileSystem getFileSystem(URI source, URI destination)
            throws
            grool.transfer.FileSystemException {

        FileSystem fileSystem = null;

        switch (UnitasFileSystemType.createValueFromArguments(source, destination)) {

            case LOCAL:

                fileSystem = new LocalFileSystem();
                break;

            case WEB:

                fileSystem = new WebFileSystem(login, password);
                break;

            case GRID:

                fileSystem = new GridFileSystem(proxy, virtualOrganization, storageElementsForReplicas);
                break;

            case HUB:
                throw new grool.transfer.FileSystemException("A Hub is needed to copy source to destination, copy skipped!");

            default:
                throw new grool.transfer.FileSystemException("The protocol of source and/or destination file(s) is not supported!");
        }

        return fileSystem;
    }
}
