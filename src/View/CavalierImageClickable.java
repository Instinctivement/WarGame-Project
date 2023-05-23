package View;

import Model.Cavalier;
import javax.swing.Icon;

public class CavalierImageClickable extends ImageClickable {
    private Cavalier cavalier;

    public CavalierImageClickable(Cavalier cavalier, Icon icon) {
        super(icon);
        this.cavalier = cavalier;
    }

    public Cavalier getCavalier() {
        return cavalier;
    }
}
