package com.pacman.pacmangame.gameActors;

import com.pacman.pacmangame.gameService.PacmanActor;
import com.pacman.pacmangame.gameService.PacmanGame;

import java.awt.*;

public class Point extends PacmanActor {

    private Pacman pacman;

    public Point(PacmanGame game, Pacman pacman) {
        super(game);
        this.pacman = pacman;
    }

    @Override
    public void init() {
        loadFrames("/images/point_0.png", "/images/point_1.png"
            , "/images/point_2.png", "/images/point_3.png");

        collider = new Rectangle(0, 0, 4, 4);
    }

    private void updatePosition(int col, int row) {
        x = col * 8 - 4 - 32;
        y = (row + 3) * 8 + 1;
    }

    @Override
    public void updateGhostCatched() {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    updatePosition(game.catchedGhost.col, game.catchedGhost.row);
                    pacman.visible = false;
                    game.catchedGhost.visible = false;
                    int frameIndex = game.currentCatchedGhostScoreTableIndex;
                    frame = frames[frameIndex];
                    game.addScore(game.catchedGhostScoreTable[frameIndex]);
                    game.currentCatchedGhostScoreTableIndex++;
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 1;
                case 1:
                    while (System.currentTimeMillis() - waitTime < 500) {
                        break yield;
                    }
                    pacman.visible = true;
                    pacman.updatePosition();
                    game.catchedGhost.visible = true;
                    game.catchedGhost.updatePosition();
                    game.catchedGhost.died();
                    game.setState(PacmanGame.State.PLAYING);
                    break yield;
            }
        }
    }

    // broadcast messages

    @Override
    public void stateChanged() {
        visible = false;
        if (game.getState() == PacmanGame.State.GHOST_CATCHED) {
            visible = true;
            instructionPointer = 0;
        }
    }

    public void hideAll() {
        visible = false;
    }

}
