package org.iblogger.opensource.core;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.RawImage;
import com.android.ddmlib.TimeoutException;

/**
 * @title
 * @author LiYa
 * @verson 1.0 Feb 3, 2012 4:00:23 PM
 */
public class ScreenShotCore {

	boolean landscape = false;

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

	public void test() throws IOException, TimeoutException,
			AdbCommandRejectedException {

		IDevice device;
		AndroidDebugBridge.init(false);
		AndroidDebugBridge bridge = AndroidDebugBridge.createBridge();
		waitDeviceList(bridge);

		IDevice devices[] = bridge.getDevices();

		device = devices[0];
		RawImage rawScreen = device.getScreenshot();
		if (rawScreen != null) {

			BufferedImage image = null;

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

			ImageIO.write((RenderedImage) image, "PNG", new File(
					"/Users/liyajie/Desktop/test.jpg"));
		}
	}

	public static void main(String[] args) {
		try {
			new ScreenShotCore().test();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AdbCommandRejectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
