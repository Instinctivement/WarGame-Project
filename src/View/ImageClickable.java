package View;

import javax.swing.*;
import java.awt.*;

public class ImageClickable extends JLabel {
    private static ImageClickable selectedImage = null;
    private static final Color SELECTED_BORDER_COLOR = Color.RED;
    private static final Color UNSELECTED_BORDER_COLOR = Color.BLACK;

    public ImageClickable(Icon icon) {
        super(icon);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setBorder(BorderFactory.createLineBorder(UNSELECTED_BORDER_COLOR));
    }

    public static ImageClickable getSelectedImage() {
        return selectedImage;
    }

    public static void clearSelectedImage() {
        if (selectedImage != null) {
            selectedImage.setBorder(BorderFactory.createLineBorder(UNSELECTED_BORDER_COLOR));
            selectedImage = null;
        }
    }
}
