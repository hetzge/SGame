package de.hetzge.sgame.network;

import java.io.InputStream;
import java.io.OutputStream;

import org.nustaq.serialization.FSTObjectOutput;

public class InOutStreams {

	public final InputStream inputStream;
	public final OutputStream outputStream;
	public final FSTObjectOutput fstObjectOutput;

	public InOutStreams(InputStream inputStream, OutputStream outputStream, FSTObjectOutput fstObjectOutput) {
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		this.fstObjectOutput = fstObjectOutput;
	}

}
