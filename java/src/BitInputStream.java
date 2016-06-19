/* 
 * Reference Huffman coding
 * Copyright (c) Project Nayuki
 * 
 * https://www.nayuki.io/page/reference-huffman-coding
 * https://github.com/nayuki/Reference-Huffman-coding
 */

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;


/**
 * A stream of bits that can be read. Because they come from an underlying byte stream, the total number of bits is always a multiple of 8. The bits are read in big endian.
 * @see BitOutputStream
 */
public final class BitInputStream {
	
	// Underlying byte stream to read from.
	private InputStream input;
	
	// Either in the range 0x00 to 0xFF if bits are available, or is -1 if the end of stream is reached.
	private int currentByte;
	
	// Always between 0 and 7, inclusive.
	private int numBitsRemaining;
	
	
	
	
	// Creates a bit input stream based on the given byte input stream.
	public BitInputStream(InputStream in) {
		if (in == null)
			throw new NullPointerException();
		input = in;
		currentByte = 0;
		numBitsRemaining = 0;
	}
	
	
	
	// Reads a bit from the stream. Returns 0 or 1 if a bit is available, or -1 if the end of stream is reached. The end of stream always occurs on a byte boundary.
	public int read() throws IOException {
		if (currentByte == -1)
			return -1;
		if (numBitsRemaining == 0) {
			currentByte = input.read();
			if (currentByte == -1)
				return -1;
			numBitsRemaining = 8;
		}
		if (numBitsRemaining <= 0)
			throw new AssertionError();
		numBitsRemaining--;
		return (currentByte >>> numBitsRemaining) & 1;
	}
	
	
	// Reads a bit from the stream. Returns 0 or 1 if a bit is available, or throws an EOFException if the end of stream is reached.
	public int readNoEof() throws IOException {
		int result = read();
		if (result != -1)
			return result;
		else
			throw new EOFException();
	}
	
	
	// Closes this stream and the underlying InputStream.
	public void close() throws IOException {
		input.close();
		currentByte = -1;
		numBitsRemaining = 0;
	}
	
}
