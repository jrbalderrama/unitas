package unitas.transfer;

import java.net.URI;

/**
 * Enumeration of the supported types associated to the file-data. It is not
 * associated directly to infrastructures because they are not directly
 * associated with protocols.
 *
 * @author javier
 */
public enum UnitasFileSystemType {

    LOCAL,
    WEB,
    GRID,
    HUB,
    UNDEFINED;

    public static UnitasFileSystemType createValueFromArguments(URI source, URI destination) {

        UnitasFileSystemType type = null;
        String sourceScheme = source.getScheme();
        String destinationScheme = destination.getScheme();
        sourceScheme = (sourceScheme == null) ? "file" : sourceScheme;
        destinationScheme = (destinationScheme == null) ? "file" : destinationScheme;
        SupportedTransferScheme sourceFileSystemScheme = SupportedTransferScheme.createValueFromName(sourceScheme);
        SupportedTransferScheme destinationFileSystemScheme = SupportedTransferScheme.createValueFromName(destinationScheme);
        switch (sourceFileSystemScheme) {

            case FILE:
                switch (destinationFileSystemScheme) {

                    case FILE:
                        type = LOCAL;
                        break;

                    case GSIFTP:
                    case LFN:
                        type = GRID;
                        break;

                    case FTP:
                    case HTTP:
                    case HTTPS:
                    case SFTP:
                        type = WEB;
                        break;

                    default:
                        type = UNDEFINED;
                }
                break;

            case GSIFTP:
            case LFN:
                switch (destinationFileSystemScheme) {

                    case FILE:
                        type = GRID;
                        break;

                    case GSIFTP:
                    case LFN:
                        type = GRID;
                        break;

                    case FTP:
                    case HTTP:
                    case SFTP:
                        type = HUB;
                        break;

                    default:
                        type = UNDEFINED;
                }
                break;

            case FTP:
            case HTTP:
            case HTTPS:
            case SFTP:
                switch (destinationFileSystemScheme) {

                    case FILE:
                        type = WEB;
                        break;

                    case GSIFTP:
                    case LFN:
                        type = HUB;
                        break;

                    case FTP:
                    case HTTP:
                    case SFTP:
                        type = WEB;
                        break;

                    default:
                        type = UNDEFINED;
                }
                break;

            default:
                type = UNDEFINED;
        }

        return type;
    }
}
