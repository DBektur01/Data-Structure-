package sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Main extends Application {

    static int score = 0;
    static int foodColor = 0;
    static int width = 35;
    static int height = 25;
    static int foodX = 0;
    static int foodY = 0;
    static int cornerSize = 25;
    static List<Corner> snake = new ArrayList<>();
    static Direction direction = Direction.LEFT;
    static boolean gameOver = false;
    static Random rand = new Random();


    public void start(Stage primaryStage) {
        try {
            newFood();

            VBox root = new VBox();
            Canvas c = new Canvas(width * cornerSize, height * cornerSize);
            GraphicsContext gc = c.getGraphicsContext2D();
            root.getChildren().add(c);

            new AnimationTimer() {
                long lastTick = 0;
                public void handle(long now) {
                    if (lastTick == 0) {
                        lastTick = now;
                        tick(gc);
                        return;
                    }

                    if (now - lastTick > 100000000) {
                        lastTick = now;
                        tick(gc);
                    }
                }

            }.start();

            Scene scene = new Scene(root, width * cornerSize, height * cornerSize);

             scene.setOnKeyPressed(e -> {

                switch (e.getCode()) {
                    case W:
                    case UP:
                        if(direction != Direction.DOWN)
                            direction = Direction.UP;
                        break;
                    case S:
                    case DOWN:
                        if(direction != Direction.UP)
                            direction = Direction.DOWN;
                        break;
                    case A:
                    case LEFT:
                        if(direction != Direction.RIGHT)
                            direction = Direction.LEFT;
                        break;
                    case D:
                    case RIGHT:
                        if(direction != Direction.LEFT)
                            direction = Direction.RIGHT;
                        break;
                }
            });


            snake.add(new Corner(width / 2, height / 2));
            primaryStage.setScene(scene);
            primaryStage.setTitle("SNAKE GAME");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void tick(GraphicsContext gc) {
        if (gameOver) {
            gc.setFill(Color.RED);
            gc.setFont(new Font("", 50));
            gc.fillText("GAME OVER", 100, 250);
            return;
        }

        for (int i = snake.size() - 1; i >= 1; i--) {
            snake.get(i).x = snake.get(i - 1).x;
            snake.get(i).y = snake.get(i - 1).y;
        }

        switch (direction) {
            case UP:
                snake.get(0).y--;
                if (snake.get(0).y < 0) {
                    gameOver = true;
                }
                break;
            case DOWN:
                snake.get(0).y++;
                if (snake.get(0).y > height) {
                    gameOver = true;
                }
                break;
            case LEFT:
                snake.get(0).x--;
                if (snake.get(0).x < 0) {
                    gameOver = true;
                }
                break;
            case RIGHT:
                snake.get(0).x++;
                if (snake.get(0).x > width) {
                    gameOver = true;
                }
                break;
        }

        if (foodX == snake.get(0).x && foodY == snake.get(0).y) {
            snake.add(new Corner(-1, -1));
            score++;
            newFood();
        }

        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y) {
                gameOver = true;
                break;
            }
        }

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, width * cornerSize, height * cornerSize);

        gc.setFill(Color.BLACK);
        gc.setFont(new Font("", 30));
        gc.fillText("Score: " + score, 10, 30);

        Color cc = Color.WHITE;

        switch (foodColor) {

            case 0:
                cc = Color.PURPLE;
                break;
            case 1:
                cc = Color.LIGHTBLUE;
                break;
            case 2:
                cc = Color.YELLOW;
                break;
            case 3:
                cc = Color.PINK;
                break;
            case 4:
                cc = Color.ORANGE;
                break;
        }

        gc.setFill(cc);
        gc.fillRect(foodX * cornerSize, foodY * cornerSize, cornerSize, cornerSize);

        for (Corner c : snake) {
            gc.setFill(Color.BLACK);
            gc.fillRect(c.x * cornerSize, c.y * cornerSize, cornerSize, cornerSize);
        }
    }

    public static void newFood() {
        start: while (true) {
            foodX = rand.nextInt(width);
            foodY = rand.nextInt(height);

            for (Corner c : snake) {
                if (c.x == foodX && c.y == foodY) {
                    continue start;
                }
            }
            foodColor = rand.nextInt(5);
            break;

        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}




