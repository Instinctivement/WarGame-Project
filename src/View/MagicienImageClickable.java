package View;

import Model.Magicien;
import javax.swing.Icon;

public class MagicienImageClickable extends ImageClickable {
    private Magicien magicien;

    public MagicienImageClickable(Magicien magicien, Icon icon) {
        super(icon);
        this.magicien = magicien;
    }

    public Magicien getMagicien() {
        return magicien;
    }
}