package gui.designs;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
public abstract class Window extends JFrame {
	public static final Dimension SCREEN_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
	public Window(Rectangle xywh, int close_operation, String title, Image icon) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		super(title);
		setBounds(xywh);
		setDefaultCloseOperation(close_operation);
		setIconImage(icon);
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		init_gui();
	}
	public void set_fullscreen(){
		setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	public void set_customsize(){
		set_customsize(getBounds());
	}
	public void set_customsize(Rectangle xywh){
		setExtendedState(JFrame.NORMAL);
		setBounds(xywh);
	}
	public void set_centered() {
		setBounds(new Rectangle(
			(SCREEN_DIMENSION.width - getWidth()) / 2,
			(SCREEN_DIMENSION.height - getHeight()) / 2,
			getWidth(),
			getHeight()
		));
	}
	protected abstract void init_gui();
}
