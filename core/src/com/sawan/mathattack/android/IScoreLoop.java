/**
 * File name:	IScoreLoop.java
 * Version:		1.0
 * Date:		@date 13:07:14
 * Author:		Sawan J. Kapai Harpalani
 * Copyright:	Copyright 200X Sawan J. Kapai Harpalani
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
package com.sawan.mathattack.android;

// TODO: Auto-generated Javadoc
/**
 * The Interface IScoreLoop.
 */
public interface IScoreLoop {
	
	/**
	 * Bootstrap.
	 */
	public void bootstrap();

	/**
	 * Show scoreloop.
	 */
	public void showScoreloop();

	/**
	 * Submit score.
	 *
	 * @param mode the mode
	 * @param score the score
	 */
	public void submitScore(int mode, int score);

	/**
	 * Refresh scores.
	 */
	public void refreshScores();
}
