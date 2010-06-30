/*
 * TestFul - http://code.google.com/p/testful/
 * Copyright (C) 2010  Matteo Miraz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package jmetal.base;

/**
 * A generic interface for the termination criterion
 * @author matteo
 */
public interface TerminationCriterion extends Cloneable {
	/**
	 * Returns the progress, as a number between 0 and 100 (%)
	 * @return the progress, as a number between 0 and 100 (%)
	 */
	public float getProgressPercent();

	/**
	 * Returns the progress, using a termination-criterion-specific progressive format (i.e., milliseconds, # of evaluations, ...)
	 * @return the progress, using a termination-criterion-specific progressive format
	 */
	public long getProgress();

	/**
	 * Returns the target to reach.
	 * @return the target to reach.
	 */
	public long getTarget();

	/**
	 * Returns the string indicating the remaining simulation time (or whatever the subclass is measuring)
	 * @return the string indicating the remaining simulation time (or whatever the subclass is measuring)
	 */
	public String getRemaining();

	/**
	 * Determines whether the termination criteria is reached (and the search must be stopped)
	 * @return true if the termination criteria is reached (and the search must be stopped)
	 */
	public boolean isTerminated();

	public TerminationCriterion clone();
}
