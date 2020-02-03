package frc.robot.util;

public class Vector2d {
    double x, y;
    public Vector2d() {
        this.x = 0;
        this.y = 0;
    }
    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void scale(double k) {
        this.x *= k;
        this.y *= k;
    }

    public void iadd(Vector2d other) {
        this.x += other.x;
        this.y += other.y;
    }
    public Vector2d add(Vector2d other) {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    public void isub(Vector2d other) {
        this.x -= other.x;
        this.y -= other.y;
    }
    public Vector2d sub(Vector2d other) {
        return new Vector2d(this.x - other.x, this.y - other.y);
    }

    public double dot(Vector2d other) {
        return this.x * other.x + this.y * other.y;
    }
    public double dot(double x, double y) {
        return this.x * x + this.y * y;
    }

    public void rotate(double angle) {
        this.x = this.dot(Math.cos(angle), -Math.sin(angle));
        this.y = this.dot(Math.sin(angle), Math.cos(angle));
    }

    public double getMagnitude() {
        return Math.sqrt(x*x + y*y);
    }

    public double getAngle() {
        return Math.atan2(x, y);
    }
}

