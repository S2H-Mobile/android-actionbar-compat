package de.s2hmobile.tools;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class InputOutput {
	
	private InputOutput() {}
	
	/**
	 * Reads an entire input stream as a string. Closes the input stream.
	 * @param in the input stream
	 * @return the decoded string
	 * @throws UnsupportedEncodingException
	 */
	public static String readStreamAsString(InputStream in)
    		throws UnsupportedEncodingException {
    	try {
    		return new String(readStream(in), "UTF-8");
        } catch (IOException e) {
            return null;
        } finally {
        	closeStream(in);
        }
    }
    
    /**
     * Writes the input stream to a byte array.
     * @param inStream the input stream
     * @return the byte array
     * @throws IOException
     */
	private static byte[] readStream(InputStream inStream) throws IOException {
    	// initialize buffer and count
        final byte[] buffer = new byte[1024];
        int byteCount = 0;
        // create the out stream
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(1024);
        // write to the buffer
        while ((byteCount = inStream.read(buffer)) >= 0) {
            outStream.write(buffer, 0, byteCount);
        }
        return outStream.toByteArray();
    }
    
	/**
	 * Closes the stream. 
	 * @param stream
	 */
	private static void closeStream(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException ignored) {}
		}
	}
}   
