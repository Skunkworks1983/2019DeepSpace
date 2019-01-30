class Vector2 {
    constructor(x, y) {
        this.x = x
        this.y = y
    }

    get magnitude() {
        return (this.x ** 2 + this.y ** 2) ** (1/2)
    }

    get negative() {
        return new Vector2(-this.x, -this.y)
    }

    get normalized() {
        return Vector2.scale(this, 1 / this.magnitude)
    }

    static add(v1, v2) {
        return new Vector2(v1.x + v2.x, v1.y + v2.y)
    }

    static sub(v1, v2) {
        return new Vector2(v1.x - v2.x, v1.y - v2.y)
    }

    static dot(v1, v2) {
        return v1.x * v2.x + v1.y * v2.y
    }

    static scale(v1, s) {
        return new Vector2(v1.x * s, v1.y * s)
    }

    static distance(v1, v2) {
        return Math.sqrt((v1.x - v2.x) ** 2 + (v1.y - v2.y) ** 2)
    }
}

module.exports = Vector2;
