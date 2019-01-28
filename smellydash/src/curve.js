class Bezier {
    constructor(...points) {
        this.points = points
    }

    evaluate(t) {
        if(this.points.length == 2) {
            return Vector2.add(this.points[0], Vector2.scale(Vector2.sub(this.points[1], this.points[0]), t))
        } else {
            let p0 = new Bezier(this.points.slice(0, -1)).evaluate(t)
            let p1 = new Bezier(this.points.slice(1, this.points.length)).evaluate(t)

            return new Bezier(p0, p1).evaluate(t)
        }
    }

    tangent(t) {
        if(this.points.length == 2) {
            return Vector2.sub(this.points[1], this.points[0]).normalized
        } else {
            let p0 = new Bezier(this.points.slice(0, -1)).evaluate(t)
            let p1 = new Bezier(this.points.slice(1, this.points.length)).evaluate(t)

            return Vector2.sub(p1, p0).normalized
        }
    }

    normal(t) {
        return new Vector2(this.tangent(t).y, -this.tangent(t).x)
    }

    offset(t, l=1) {
        return Vector2.add(this.evaluate(t), Vector2.scale(this.normal(t), l))
    }
}

module.exports = Bezier;
