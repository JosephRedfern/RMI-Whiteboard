import java.awt.*;

/**
 * Created by joe on 24/03/15.
 */
public class Triangle extends Polygon{

    public Triangle(int x, int y, int width, int height){
        this.addPoint(x + width/2, y);
        this.addPoint(x, y+height);
        this.addPoint(x+width, y+height);
    }
}
