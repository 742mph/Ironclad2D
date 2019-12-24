package org.cell2d.celick.opengl;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import org.cell2d.celick.util.Log;

/**
 * A composite data source that checks multiple loaders in order of
 * preference
 * 
 * @author kevin
 */
public class CompositeImageData implements LoadableImageData  {
	/** The list of images sources in order of preference to try loading the data with */
	private ArrayList sources = new ArrayList();
	/** The data source that worked and was used - or null if no luck */
	private LoadableImageData picked;
	
	/**
	 * Add a potentional source of image data
	 * 
	 * @param data The data source to try
	 */
	public void add(LoadableImageData data) {
		sources.add(data);
	}
	
	@Override
	public ByteBuffer loadImage(InputStream fis) throws IOException {
		return loadImage(fis, false, null);
	}

	@Override
	public ByteBuffer loadImage(InputStream fis, boolean flipped, int[] transparent) throws IOException {
		return loadImage(fis, flipped, false, transparent);
	}

	@Override
	public ByteBuffer loadImage(InputStream is, boolean flipped, boolean forceAlpha, int[] transparent) throws IOException {
		CompositeIOException exception = new CompositeIOException();
		ByteBuffer buffer = null;
		
		BufferedInputStream in = new BufferedInputStream(is, is.available());
		in.mark(is.available());
		
		// cycle through our source until one of them works
		for (int i=0;i<sources.size();i++) {
			in.reset();
			try {
				LoadableImageData data = (LoadableImageData) sources.get(i);
				
				buffer = data.loadImage(in, flipped, forceAlpha, transparent);
				picked = data;
				break;
			} catch (Exception e) {
				exception.addException(e);
			}
		}
		
		if (picked == null) {
			throw exception;
		}
		
		return buffer;
	}

	/**
	 * Check the state of the image data and throw a
	 * runtime exception if theres a problem
	 */
	private void checkPicked() {
		if (picked == null) {
			throw new RuntimeException("Attempt to make use of uninitialised or invalid composite image data");
		}
	}
	
	@Override
	public int getDepth() {
		checkPicked();
		
		return picked.getDepth();
	}

	@Override
	public int getHeight() {
		checkPicked();
		
		return picked.getHeight();
	}

	@Override
	public ByteBuffer getImageBufferData() {
		checkPicked();
		
		return picked.getImageBufferData();
	}

	@Override
	public int getTexHeight() {
		checkPicked();
		
		return picked.getTexHeight();
	}

	@Override
	public int getTexWidth() {
		checkPicked();
		
		return picked.getTexWidth();
	}

	@Override
	public int getWidth() {
		checkPicked();
		
		return picked.getWidth();
	}

	@Override
	public void configureEdging(boolean edging) {
		for (int i=0;i<sources.size();i++) {
			((LoadableImageData) sources.get(i)).configureEdging(edging);
		}
	}

}
