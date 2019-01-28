class Robot {
  static show(x, y, heading) {
    rectMode(CENTER);

    push();

    // Draw rect
    translate(x * PIXELS_PER_FOOT, CANVAS_HEIGHT - (y * PIXELS_PER_FOOT));
    rotate(-heading - 90);
    rect(0, 0, ROBOT_WIDTH, ROBOT_HEIGHT);

    // Draw heading
    fill(0, 0, 0);
    triangle(0, ROBOT_HEIGHT / 2, -3, ROBOT_HEIGHT / 2 - 3, 3, ROBOT_HEIGHT / 2 - 3);

    pop();
  }
}
