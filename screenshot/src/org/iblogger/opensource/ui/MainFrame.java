package org.iblogger.opensource.ui;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.filechooser.FileSystemView;

import org.iblogger.opensource.core.ScreenShotCore;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.TimeoutException;

/**
 * @title
 * @author LiYa
 * @verson 1.0 Feb 6, 2012 9:15:30 AM
 */
public class MainFrame extends JFrame implements ActionListener {

	/**
	 */
	private static final long serialVersionUID = 1L;

	static final int WINDOW_WIDTH = 300;
	static final int WINDOW_HEIGHT = 300;

	private int width, height;

	private ScreenShotCore coreScreen = null;

	public MainFrame() {
		setLocationRelativeTo(null);
		width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		int centerWidth = width / 3;
		int centerHeight = height - 120;
		// setResizable(false);
		coreScreen = new ScreenShotCore();
		setLocation(WINDOW_WIDTH, WINDOW_HEIGHT);
		setSize(centerWidth, centerHeight);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				super.windowClosed(e);
				coreScreen.close();
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public void startFrame() {
		JSplitPane split = initContent();
		add(split);
		setVisible(true);
	}

	private JPanel bottomPanel = null;

	private Image image = null;

	/**
	 * @return
	 */
	@SuppressWarnings("serial")
	public JSplitPane initContent() {
		try {
			image = coreScreen.screenShot();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (AdbCommandRejectedException e) {
			e.printStackTrace();
		}
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		JPanel topPanel = new JPanel();
		topPanel.setLayout(null);
		topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		topPanel.add(createButton("Refresh"));
		topPanel.add(createButton("Save"));
		splitPane.setTopComponent(topPanel);
		bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)) {
			@Override
			public void paint(Graphics g) {
				g.drawImage(image, 30, 6, this);
			}
		};
		splitPane.setBottomComponent(bottomPanel);
		return (splitPane);
	}

	/**
	 * @param name
	 * @return
	 */
	public JButton createButton(String name) {
		JButton btn = new JButton(name);
		btn.setText(name);
		btn.setSize(100, 40);
		btn.setActionCommand(name);
		btn.addActionListener(this);
		return (btn);
	}

	public static void main(String[] args) {
		new MainFrame().startFrame();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String comand = event.getActionCommand();
		if ("Refresh".equals(comand)) {
			try {
				image = coreScreen.screenShot();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				e.printStackTrace();
			} catch (AdbCommandRejectedException e) {
				e.printStackTrace();
			}
			bottomPanel.repaint();
		} else if ("Save".equals(comand)) {
			if (image != null) {
				JFileChooser fileChooser = new JFileChooser(
						new File(".").getParent(),
						FileSystemView.getFileSystemView());
				int returnCode = fileChooser.showSaveDialog(MainFrame.this);
				if (returnCode == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					// String fileName = file.getName();
					String path = file.getPath();
					System.out.println(path);
					try {
						ImageIO.write((RenderedImage) image, "PNG", new File(
								path));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
