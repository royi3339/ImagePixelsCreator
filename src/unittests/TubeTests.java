package unittests;

import geometries.Tube;
import org.junit.Test;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import static org.junit.Assert.*;

/**
 * implements the TubeTests class.
 *
 * @author Royi Alishayev idan darmoni
 */
public class TubeTests {
    /**
     * test method for
     * * {@link Tube#Tube(double, Ray)}
     * <p>
     * test method for
     * * {@link Tube#Tube(double, Point3D, Vector)}
     */
    @Test
    public void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: negative radius constructor#1
        try {
            new Tube(-5, new Point3D(0, 0, 0), new Vector(0, 0, 13));
            fail("TC01: Constructed a Sphere with a not positive radius");
        } catch (IllegalArgumentException e) {}

        // TC02: negative radius constructor#1
        try {
            new Tube(-5, new Ray(new Point3D(0, 0, 0), new Vector(0, 0, 13)));
            fail("TC02: Constructed a Sphere with a not positive radius");
        } catch (IllegalArgumentException e) { }

        // TC03: correct Tube
        try {
            new Tube(7, new Point3D(0, 0, 0), new Vector(0, 0, 13));
        } catch (IllegalArgumentException e) {
            fail("TC03: Failed constructing a correct Sphere");
        }

        // TC04: correct Tube
        try {
            new Tube(7, new Ray(new Point3D(0, 0, 0), new Vector(0, 0, 13)));
        } catch (IllegalArgumentException e) {
            fail("TC04: Failed constructing a correct Sphere");
        }
    }

    /**
     * test method for
     * {@link Tube#getNormal(Point3D)}
     */
    @Test
    public void testGetNormal() {
        Tube t = new Tube(2, new Point3D(0, 0, 0), new Vector(0, 0, 1));
        // ============ Equivalence Partitions Tests ==============
        //TC01: the point is on the casing
        assertEquals("TC01: wrong normal detected", new Vector(0, 1, 0), t.getNormal(new Point3D(0, 2, 3339)));

        //TC02: the point is on the casing, and the Ray head start point
        assertEquals("TC02: wrong normal detected", new Vector(0, 1, 0), t.getNormal(new Point3D(0, 2, 0)));
    }
}