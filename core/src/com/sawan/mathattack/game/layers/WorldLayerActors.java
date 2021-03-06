/**
 * File name:	WorldLayerActors.java
 * Version:		1.0
 * Date:		20/3/2015 10:05:12
 * Author:		Itop1
 * Copyright:	Copyright 200X Itop1
 *
 *				This file is part of Math Attack.
 *
 *				Math Attack is free software: you can redistribute it 
 *				and/or modify it under the terms of the GNU General
 *				Public License as published by the Free Software 
 *				Foundation, either version 3 of the License, 
 *				or (at your option) any later version.
 *
 *				Math Attack is distributed in the hope that it will 
 *				be useful, but WITHOUT ANY WARRANTY; without even 
 *				the implied warranty of MERCHANTABILITY or FITNESS 
 *				FOR A PARTICULAR PURPOSE. See the GNU General Public
 *			    License for more details.
 *
 *				You should have received a copy of the GNU General 
 *				Public License along with Math Attack. If not, see 
 *				http://www.gnu.org/licenses/.
 */
package com.sawan.mathattack.game.layers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.sawan.mathattack.asset.MonsterAssets;
import com.sawan.mathattack.asset.GameAssets;
import com.sawan.mathattack.asset.HeroAssests;
import com.sawan.mathattack.collision.CollisionDetector;
import com.sawan.mathattack.game.GameState;
import com.sawan.mathattack.game.managers.MAGameManager;
import com.sawan.mathattack.models.ammunition.Bullet;
import com.sawan.mathattack.models.characters.Hero;
import com.sawan.mathattack.models.characters.enemies.MAMonster;
import com.sawan.mathattack.scene2d.AbstractWorldScene2d;
import com.sawan.mathattack.settings.AppSettings;

// TODO: Auto-generated Javadoc
/**
 * The Class WorldLayerActors.
 *
 * @author Sawan
 */
public class WorldLayerActors extends AbstractWorldScene2d {
	
	/** The game manager. */
	private MAGameManager gameManager;
	
	/** The hero. */
	public Hero hero;
	
	/** The enemies. */
	public ArrayList<MAMonster> enemies;
	
	/** The bullets. */
	public ArrayList<Bullet> bullets;
	
	/** The Constant NUM_ENEMIES. */
	protected final static int NUM_ENEMIES = 15;
	
	/** The Constant SPEED. */
	protected final static int SPEED = 80;
	
	/** The Constant BULLET_SIZE. */
	protected final static float BULLET_SIZE = 60f;
	
	/** The level. */
	public int level;
	
	/**
	 * Instantiates a new world layer actors.
	 *
	 * @param gameManager the game manager
	 * @param posX the pos x
	 * @param posY the pos y
	 * @param worldWidth the world width
	 * @param worldHeight the world height
	 * @param level the level
	 */
	public WorldLayerActors(MAGameManager gameManager, float posX, float posY, float worldWidth, float worldHeight, int level) {
		super(posX, posY, worldWidth, worldHeight);
		this.gameManager =  gameManager;
		this.level = level;
		setUpHero();
		setUpEnemies();
		
	}
	
	/**
	 * Sets the up hero.
	 */
	public void setUpHero() {
		hero = new Hero(gameManager.worldLayer_background.SOIL_WIDHT, gameManager.worldLayer_background.SOIL_HEIGHT, true);
		bullets = new ArrayList<Bullet>();
		hero.setAlive(true);
		
		hero.setY(gameManager.worldLayer_background.SOIL_HEIGHT * AppSettings.getWorldSizeRatio());
		hero.setX(0f * AppSettings.getWorldPositionXRatio());
		hero.setAnimation(HeroAssests.hero_standing, true, true);
		addActor(hero);
	}
	
	/**
	 * Sets the up enemies.
	 */
	public void setUpEnemies() {
		enemies = new ArrayList<MAMonster>();
		Random rnd = new Random();
		
		MonsterAssets.setFILE_IMAGE_ATLAS(level);
		MonsterAssets.loadAll();
		
		for (int i = 0; i < NUM_ENEMIES; i++) {
			MAMonster current_monster = new MAMonster(gameManager.worldLayer_background.SOIL_WIDHT, gameManager.worldLayer_background.SOIL_HEIGHT, true);
			
			float posY = gameManager.worldLayer_background.SOIL_HEIGHT * AppSettings.getWorldSizeRatio();
			current_monster.setY(posY);
			current_monster.setX(gameManager.getStage().getWidth() + (i * (current_monster.getWidth() + 100)));
			
			current_monster.setAnimation(MonsterAssets.monster_walking, true, true);
			
			float rndSpeed = rnd.nextInt(SPEED) + 20;
			current_monster.startMoving(gameManager.getStage().getWidth(), rndSpeed * AppSettings.getWorldPositionXRatio(), true, false);
			
			enemies.add(current_monster);
			addActor(current_monster);
			
			
		}
	}
	
	/**
	 * Kill hero.
	 */
	public void killHero() {
		
			hero.setAnimationMomentary(HeroAssests.hero_faint, true, null, true, true);
			Timer.schedule(new Task() {
				@Override
				public void run() {
					gameManager.setGameState(GameState.GAME_OVER);
				}
			}, 0.9f);
	}

	
	/**
	 * Checks if is hero alive.
	 *
	 * @return true, if is hero alive
	 */
	public boolean isHeroAlive() {
		 if (hero.isAlive()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Game win.
	 */
	public void gameWin() {
		if (enemies.isEmpty() && hero.getLifes() > 0) {
			gameManager.setGameState(GameState.GAME_LEVELWIN);
		}
	}
	
	/**
	 * Check collision.
	 *
	 * @param hero the hero
	 * @param enemies the enemies
	 */
	public void checkCollision(Hero hero, ArrayList<MAMonster> enemies) {
		for (Iterator<MAMonster> iterator = enemies.iterator(); iterator.hasNext();) {
			MAMonster enemy = (MAMonster) iterator.next();
			if (CollisionDetector.isActorsCollide(hero, enemy) && enemy.isAlive() && hero.getLifes() > 0) {
			//	System.out.println(enemies.indexOf(enemy));
				enemy.setAlive(false);
				iterator.remove();
				enemies.remove(enemy);
				removeActor(enemy);
				
				hero.setLifes(hero.getLifes() - 1);
				if (hero.getLifes() <= 0) {
					hero.setAlive(false);
				}
				
				hero.setAnimationMomentary(HeroAssests.hero_dizzy, true, HeroAssests.hero_standing, true, false);
				
				hero.setLost_life(true);				
			}
		}
	}

	/**
	 * Hit enemy.
	 *
	 * @param bullets the bullets
	 * @param enemies the enemies
	 */
	public void hitEnemy(ArrayList<Bullet> bullets, ArrayList<MAMonster> enemies) {
		for (Iterator<Bullet> iterator_bullets = bullets.iterator(); iterator_bullets.hasNext();) {
			Bullet bullet = (Bullet) iterator_bullets.next();
			
			for (Iterator<MAMonster> iterator_enemies = enemies.iterator(); iterator_enemies.hasNext();) {
				MAMonster blueMonster = (MAMonster) iterator_enemies.next();
				
				if (CollisionDetector.isActorsCollide(bullet, blueMonster)) {
					iterator_bullets.remove();
					iterator_enemies.remove();
					bullets.remove(bullet);
					enemies.remove(blueMonster);
					removeActor(bullet);
					removeActor(blueMonster);
					break;
				}
			}
			
		}
	}
	
	/**
	 * Adds the bullet.
	 */
	public void addBullet() {
		final Bullet bullet = new Bullet(BULLET_SIZE, BULLET_SIZE, true);
		bullet.setX(hero.getX() + hero.getWidth());
		bullet.setY((hero.getY()) + (bullet.getHeight() / 2));
		bullet.setTextureRegion(GameAssets.loadRandomProjectile(), true);
		
		bullets.add(bullet);
		
		bullet.startMoving(gameManager.getStage().getWidth(), 150f * AppSettings.getWorldPositionXRatio(), true);
		
		addActor(bullet);
	}
	
}
