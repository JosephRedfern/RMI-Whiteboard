import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Created by joe on 24/03/15.
 */
public class ShapeFactory{
    public enum Shape{
        Rectangle,
        Triangle,
        Circle,
        Ellipse
    }

    public static java.awt.Shape getShape(ShapeFactory.Shape shape, int x, int y, int width, int height){
        switch(shape){
            case Triangle:
                return new Triangle(x, y, width, height);
            case Circle:
                return new Ellipse2D.Double(x, y, width, width);
            case Ellipse:
                return new Ellipse2D.Double(x, y, width, height);
            case Rectangle:
                return new Rectangle(x, y, width, height);
        }

        return null;
    }
}
