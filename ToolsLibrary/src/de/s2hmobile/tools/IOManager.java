package de.s2hmobile.tools;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class IOManager {

	private IOManager() {
	}

	/**
	 * Reads an InputStream and converts it to a String. Closes the input
	 * stream.
	 * 
	 * @param in
	 *            the input stream
	 * @return the decoded string
	 * @throws UnsupportedEncodingException
	 */
	public static String readStreamAsString(InputStream in) throws IOException {
		String result = null;
		if (in != null) {
			try {
				result = new String(readStream(in), "UTF-8");

				// BufferedReader reader = new BufferedReader(
				// new InputStreamReader(in, "UTF-8"), 8);
				// StringBuilder sb = new StringBuilder();
				//
				// String line = null;
				// while ((line = reader.readLine()) != null) {
				// sb.append(line + "\n");
				// }
				// result = sb.toString();

			} catch (UnsupportedEncodingException e) {
			} finally {
				closeStream(in);
			}
		}
		return result;
	}

	/**
	 * Writes the input stream to a byte array.
	 * 
	 * @param inStream
	 *            the input stream
	 * @return the byte array
	 * @throws IOException
	 */
	private static byte[] readStream(InputStream inStream) throws IOException {

		// initialize buffer and create output stream
		final int size = 1024; // FIXME Unexpected end of file when set to 4096
		final byte[] buffer = new byte[size];
		int byteCount = 0;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream(size);

		// write to the buffer
		while ((byteCount = inStream.read(buffer)) >= 0) {
			outStream.write(buffer, 0, byteCount);
		}
		return outStream.toByteArray();
	}

	/**
	 * Closes the stream.
	 * 
	 * @param stream
	 */
	private static void closeStream(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException ignored) {
			}
		}
	}
}