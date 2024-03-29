package geometries;

import java.util.List;

import primitives.*;

import static primitives.Util.*;

/**
 * Polygon class represents two-dimensional polygon in 3D Cartesian coordinate
 * system
 *
 * @author Dan && Royi Alishayev && Idan Darmoni
 */
public class Polygon extends Geometry {
    /**
     * List of polygon's vertices
     */
    protected List<Point3D> _vertices;
    /**
     * Associated plane in which the polygon lays
     */
    protected Plane _plane;


    /**
     * Polygon with {@link Color}, and {@link Material} constructor based on vertices list. The list must be ordered by edge
     * path. The polygon must be convex.
     *
     * @param color    <b> the {@link Color} of the Polygon </b>
     * @param material <b> the {@link Material} of the {@link Polygon} </b>
     * @param vertices list of vertices according to their order by edge path
     * @throws IllegalArgumentException in any case of illegal combination of
     *                                  vertices:
     *                                  <ul>
     *                                  <li>Less than 3 vertices</li>
     *                                  <li>Consequent vertices are in the same
     *                                  point
     *                                  <li>The vertices are not in the same
     *                                  plane</li>
     *                                  <li>The order of vertices is not according
     *                                  to edge path</li>
     *                                  <li>Three consequent vertices lay in the
     *                                  same line (180&#176; angle between two
     *                                  consequent edges)
     *                                  <li>The polygon is concave (not convex></li>
     *                                  </ul>
     */
    public Polygon(Color color, Material material, Point3D... vertices) {
        super(color, material);
        double x1 = vertices[0].getX(), x2 = vertices[0].getX();
        double y1 = vertices[0].getY(), y2 = vertices[0].getY();
        double z1 = vertices[0].getZ(), z2 = vertices[0].getZ();
        for (int i = 0; i < vertices.length; ++i) {
            double x = vertices[i].getX(), y = vertices[i].getY(), z = vertices[i].getZ();
            if (x < x1) {x1 = x;}
            if (x > x2) {x2 = x;}
            if (y < y1) {y1 = y;}
            if (y > y2) {y2 = y;}
            if (z < z1) {z1 = z;}
            if (z > z2) {z2 = z;}
        }
        box = new BoundingBox(x1, x2, y1, y2, z1, z2);
        if (vertices.length < 3)
            throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
        _vertices = List.of(vertices);
        // Generate the plane according to the first three vertices and associate the
        // polygon with this plane.
        // The plane holds the invariant normal (orthogonal unit) vector to the polygon
        _plane = new Plane(vertices[0], vertices[1], vertices[2]);
        if (vertices.length == 3) return; // no need for more tests for a Triangle

        Vector n = _plane.getNormal();

        // Subtracting any subsequent points will throw an IllegalArgumentException
        // because of Zero Vector if they are in the same point
        Vector edge1 = vertices[vertices.length - 1].subtract(vertices[vertices.length - 2]);
        Vector edge2 = vertices[0].subtract(vertices[vertices.length - 1]);

        // Cross Product of any subsequent edges will throw an IllegalArgumentException
        // because of Zero Vector if they connect three vertices that lay in the same
        // line.
        // Generate the direction of the polygon according to the angle between last and
        // first edge being less than 180 deg. It is hold by the sign of its dot product
        // with
        // the normal. If all the rest consequent edges will generate the same sign -
        // the
        // polygon is convex ("kamur" in Hebrew).
        boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
        for (int i = 1; i < vertices.length; ++i) {
            // Test that the point is in the same plane as calculated originally
            if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
                throw new IllegalArgumentException("All vertices of a polygon must lay in the same plane");
            // Test the consequent edges have
            edge1 = edge2;
            edge2 = vertices[i].subtract(vertices[i - 1]);
            if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0)) {
                throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
            }
        }
    }

    /**
     * Polygon with {@link Color} constructor based on vertices list. The list must be ordered by edge
     * path. The polygon must be convex.
     *
     * @param color    <b> the {@link Color} of the Polygon </b>
     * @param vertices list of vertices according to their order by edge path
     * @throws IllegalArgumentException in any case of illegal combination of
     *                                  vertices:
     *                                  <ul>
     *                                  <li>Less than 3 vertices</li>
     *                                  <li>Consequent vertices are in the same
     *                                  point
     *                                  <li>The vertices are not in the same
     *                                  plane</li>
     *                                  <li>The order of vertices is not according
     *                                  to edge path</li>
     *                                  <li>Three consequent vertices lay in the
     *                                  same line (180&#176; angle between two
     *                                  consequent edges)
     *                                  <li>The polygon is concave (not convex></li>
     *                                  </ul>
     */
    public Polygon(Color color, Point3D... vertices) {
        this(color, new Material(0, 0, 0), vertices);
    }

    /**
     * Polygon constructor based on vertices list. The list must be ordered by edge
     * path. The polygon must be convex.
     *
     * @param vertices list of vertices according to their order by edge path
     * @throws IllegalArgumentException in any case of illegal combination of
     *                                  vertices:
     *                                  <ul>
     *                                  <li>Less than 3 vertices</li>
     *                                  <li>Consequent vertices are in the same
     *                                  point
     *                                  <li>The vertices are not in the same
     *                                  plane</li>
     *                                  <li>The order of vertices is not according
     *                                  to edge path</li>
     *                                  <li>Three consequent vertices lay in the
     *                                  same line (180&#176; angle between two
     *                                  consequent edges)
     *                                  <li>The polygon is concave (not convex></li>
     *                                  </ul>
     */
    public Polygon(Point3D... vertices) { this(Color.BLACK, vertices); }

    /**
     * @param point <b> the {@link Point3D} in the Polygon </b>
     * @return {@link Vector} <b> normal </b>
     */
    @Override
    public Vector getNormal(Point3D point) { return _plane.getNormal(); }

    /**
     * checking the Points which intersections with the object, and with the given {@link Ray}, and return a List of those points.
     * <p>
     * in range of the given distance.
     * <p>
     * if there is no intersections, will return null.
     *
     * @param ray         <b> the {@link Ray} we will find his intersections </b>
     * @param maxDistance <b> the range of the distance checking of the {@link Ray} </b>
     * @return List<GeoPoint> <b> the intersections points </b>
     */
    @Override
    public List<GeoPoint> findIntersections(Ray ray, double maxDistance) {
        int size = this._vertices.size();
        Vector V = ray.getDirection();
        List<GeoPoint> pointList = this._plane.findIntersections(ray, maxDistance);
        if (pointList == null) { return null; }

        Point3D p0 = ray.getHead();
        Vector v[] = new Vector[size];
        int i;
        try {
            for (i = 0; i < size; i++) { v[i] = this._vertices.get(i).subtract(p0); }
        } catch (IllegalArgumentException e) { // if throw zero vector exception, so the point intersection is on the vertex
            return null;
        }
        Vector n[] = new Vector[size];
        try {
            for (i = 0; i < size - 1; i++) { n[i] = v[i].crossProduct(v[i + 1]); }
            n[i] = v[i].crossProduct(v[0]); // the last normal
        } catch (IllegalArgumentException e) { // check if the intersect is Outside against vertex
            return null;
        }
        try {
            double temp = V.dotProduct(n[0]);
            // check if all v*n[i] is the same sign (+/-)
            for (i = 0; i < size; i++) {
                if (temp * V.dotProduct(n[i]) < 0 || isZero(alignZero(temp * V.dotProduct(n[i])))) { return null; }
            }
        } catch (IllegalArgumentException e) { // if throw zero vector exception, so the point intersection is on the edge
            return null;
        }
        pointList.get(0).geometry = this;
        return pointList;
    }
}