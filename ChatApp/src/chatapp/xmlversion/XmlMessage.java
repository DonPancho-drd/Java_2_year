package chatapp.xmlversion;

import chatapp.Constants;
import org.w3c.dom.Document;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public abstract class XmlMessage {

    public abstract Document toXml() throws Exception;

    public void sendXml(OutputStream out) throws Exception {
        byte[] xmlBytes = XmlHelper.documentToBytes(toXml());
        byte[] lengthBytes = ByteBuffer.allocate(Constants.XML_HEADER_SIZE).putInt(xmlBytes.length).array();
        out.write(lengthBytes);
        out.write(xmlBytes);
        out.flush();
    }

    public static Document readXml(InputStream in) throws Exception {
        byte[] lengthBytes = new byte[Constants.XML_HEADER_SIZE];
        int bytesRead = in.read(lengthBytes);
        if (bytesRead != Constants.XML_HEADER_SIZE) {
            throw new IOException("Failed to read length header");
        }

        int length = ByteBuffer.wrap(lengthBytes).getInt();
        byte[] xmlBytes = new byte[length];
        bytesRead = 0;
        while (bytesRead < length) {
            int result = in.read(xmlBytes, bytesRead, length - bytesRead);
            if (result == -1) break;
            bytesRead += result;
        }
        if (bytesRead < length) {
            throw new IOException("Expected " + length + " bytes, but read only " + bytesRead);
        }
        return XmlHelper.parseXml(xmlBytes);
    }
}