package org.iblogger.opensource.core;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.RawImage;
import com.android.ddmlib.TimeoutException;

/**
 * @title
 * @author LiYa
 * @verson 1.0 Feb 3, 2012 4:00:23 PM
 */
public class ScreenShotCore implements IDeviceChangeListener {

	boolean landscape = false;
	
	public ScreenShotCore() {
		getDevices();
	}

	private static void waitDeviceList(AndroidDebugBridge bridge) {
		int count = 0;
		while (bridge.hasInitialDeviceList() == false) {
			try {
				Thread.sleep(100); // 如果没有获得设备列表，则等待
				count++;
			} catch (InterruptedException e) {
			}
			if (count > 300) {
				// 设定时间超过300×100 ms的时候为连接超时
				System.err.print("Time out");
				break;
			}
		}
	}
	
	private IDevice[] devices = null;
	
	public void getDevices() {
		AndroidDebugBridge.init(false);
		AndroidDebugBridge bridge = AndroidDebugBridge.createBridge();
		bridge.addDeviceChangeListener(this);
		waitDeviceList(bridge);
		devices = bridge.getDevices();
	}

	/**
	 * @return
	 * @throws IOException
	 * @throws TimeoutException
	 * @throws AdbCommandRejectedException
	 */
	public Image screenShot() throws IOException, TimeoutException,
			AdbCommandRejectedException {
		IDevice device = devices[0];
		RawImage rawScreen = device.getScreenshot();
		BufferedImage image = null;
		if (rawScreen != null) {
			System.out.println(rawScreen.width + " " + rawScreen.height);
			int width2 = landscape ? rawScreen.height : rawScreen.width;
			int height2 = landscape ? rawScreen.width : rawScreen.height;
			if (image == null) {

				image = new BufferedImage(width2, height2,

				BufferedImage.TYPE_INT_RGB);

			} else {
				if (image.getHeight() != height2 || image.getWidth() != width2) {

					image = new BufferedImage(width2, height2,

					BufferedImage.TYPE_INT_RGB);

				}

			}
			int index = 0;
			int indexInc = rawScreen.bpp >> 3;
			for (int y = 0; y < rawScreen.height; y++) {
				for (int x = 0; x < rawScreen.width; x++, index += indexInc) {
					int value = rawScreen.getARGB(index);
					if (landscape)
						image.setRGB(y, rawScreen.width - x - 1, value);
					else
						image.setRGB(x, y, value);
				}
			}
//			ImageIO.write((RenderedImage) image, "PNG", new File(
//					"/Users/liyajie/Desktop/test.jpg"));
		}
		return (image);
	}
	
	public void close() {
		AndroidDebugBridge.terminate();
	}

	@Override
	public void deviceChanged(IDevice arg0, int arg1) {
		
	}

	@Override
	public void deviceConnected(IDevice arg0) {
		
	}

	@Override
	public void deviceDisconnected(IDevice arg0) {
		
	}
}
