package View;

import Model.Archer;
import javax.swing.Icon;

public class ArcherImageClickable extends ImageClickable {
    private Archer archer;

    public ArcherImageClickable(Archer archer, Icon icon) {
        super(icon);
        this.archer = archer;
    }

    public Archer getArcher() {
        return archer;
    }
}