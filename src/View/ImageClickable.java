package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ImageClickable extends JLabel {
    private static ImageClickable selectedImage = null;
    private static final Color SELECTED_BORDER_COLOR = Color.RED;
    private static final Color UNSELECTED_BORDER_COLOR = Color.BLACK;

    public ImageClickable(Icon icon) {
        super(icon);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setBorder(BorderFactory.createLineBorder(UNSELECTED_BORDER_COLOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedImage != null && selectedImage != ImageClickable.this) {
                    selectedImage.setBorder(BorderFactory.createLineBorder(UNSELECTED_BORDER_COLOR));
                    selectedImage = null;
                }

                if (selectedImage == ImageClickable.this) {
                    setBorder(BorderFactory.createLineBorder(UNSELECTED_BORDER_COLOR));
                    selectedImage = null;
                } else {
                    setBorder(BorderFactory.createLineBorder(SELECTED_BORDER_COLOR));
                    selectedImage = ImageClickable.this;
                }
            }
        });
    }
}
