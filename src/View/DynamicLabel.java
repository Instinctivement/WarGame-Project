package View;

import java.io.Serializable;
import javax.swing.JLabel;

public class DynamicLabel extends JLabel implements Serializable {
    public DynamicLabel() {
        super("Infomations");
    }

    public void setText(String text) {
        super.setText(text);
    }
}
