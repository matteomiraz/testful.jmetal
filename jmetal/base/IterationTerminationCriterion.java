package jmetal.base;

/**
 * Class for an iteration-based termination criterion
 * @author matteo
 */
public class IterationTerminationCriterion implements TerminationCriterion {

	/** Stop when this amount of iterations has been reached */
	private final long maxIterations;

	/** Current number of iterations */
	private long currIterations;

	public IterationTerminationCriterion(long maxIterations) {
		this.maxIterations = maxIterations;
		this.currIterations = 0;
	}

	public synchronized void addIteration(long num) {
		this.currIterations += num;
	}

	@Override
	public float getProgressPercent() {
		return (100.0f * currIterations) / maxIterations;
	}

	@Override
	public long getProgress() {
		return currIterations;
	}

	@Override
	public String getRemaining() {
		return Long.toString(maxIterations - currIterations) +  " iterations";
	}

	@Override
	public long getTarget() {
		return maxIterations;
	}

	@Override
	public boolean isTerminated() {
		return currIterations >= maxIterations;
	}

	@Override
	public IterationTerminationCriterion clone() {
		return new IterationTerminationCriterion(maxIterations);
	}

	@Override
	public String toString() {
		return Long.toString(currIterations) + " iterations";
	}
}
