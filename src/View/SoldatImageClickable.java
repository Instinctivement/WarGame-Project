package View;

import Model.Soldat;
import javax.swing.Icon;

public class SoldatImageClickable extends ImageClickable {
    private Soldat soldat;

    public SoldatImageClickable(Soldat soldat, Icon icon) {
        super(icon);
        this.soldat = soldat;
    }

    public Soldat getSoldat() {
        return soldat;
    }
}
