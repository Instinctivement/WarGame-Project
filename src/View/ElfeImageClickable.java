package View;

import Model.Elfe;
import javax.swing.Icon;

public class ElfeImageClickable extends ImageClickable {
    private Elfe elfe;

    public ElfeImageClickable(Elfe elfe, Icon icon) {
        super(icon);
        this.elfe = elfe;
    }

    public Elfe getElfe() {
        return elfe;
    }
}