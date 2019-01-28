class Bezier {
    static evaluate(t, ...points) {
        if(Array.isArray(points[0]))
            points = points[0]
        if(points.length == 2) {
            return Vector2.add(points[0], Vector2.scale(Vector2.sub(points[1], points[0]), t))
        } else {
            let p0 = Bezier.evaluate(t, points.slice(0, -1))
            let p1 = Bezier.evaluate(t, points.slice(1, points.length))

            return Bezier.evaluate(t, p0, p1)
        }
    }

    static tangent(t, ...points) {
        if(Array.isArray(points[0]))
            points = points[0]
        if(points.length == 2) {
            return Vector2.sub(points[1], points[0]).normalized
        } else {
            let p0 = Bezier.evaluate(t, points.slice(0, -1))
            let p1 = Bezier.evaluate(t, points.slice(1, points.length))

            return Vector2.sub(p1, p0).normalized
        }
    }

    static normal(t, ...points) {
        if(Array.isArray(points[0]))
            points = points[0]
        return new Vector2(Bezier.tangent(t, points).y, -Bezier.tangent(t, points).x)
    }

    static offset(t, l=1, ...points) {
        if(Array.isArray(points[0]))
            points = points[0]
        return Vector2.add(Bezier.evaluate(t, points), Vector2.scale(Bezier.normal(t, points), l))
    }
}

module.exports = Bezier;
