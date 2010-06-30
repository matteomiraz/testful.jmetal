package jmetal.base;

/**
 * Class for an evaluation-based termination criterion
 * @author matteo
 */
public class EvaluationTerminationCriterion implements TerminationCriterion {

	/** Stop when this amount of evaluation has been reached */
	private final long maxEvaluation;

	/** Current number of evaluations */
	private long currEvaluation;

	public EvaluationTerminationCriterion(long maxEvaluation) {
		this.maxEvaluation = maxEvaluation;
		this.currEvaluation = 0;
	}

	public synchronized void addEvaluations(long num) {
		this.currEvaluation += num;
	}

	@Override
	public float getProgressPercent() {
		return (100.0f * currEvaluation) / maxEvaluation;
	}

	@Override
	public long getProgress() {
		return currEvaluation;
	}

	@Override
	public String getRemaining() {
		return Long.toString(maxEvaluation - currEvaluation) +  " evaluations";
	}

	@Override
	public long getTarget() {
		return maxEvaluation;
	}

	@Override
	public boolean isTerminated() {
		return currEvaluation >= maxEvaluation;
	}

	@Override
	public EvaluationTerminationCriterion clone() {
		return new EvaluationTerminationCriterion(maxEvaluation);
	}

	@Override
	public String toString() {
		return Long.toString(currEvaluation) + " evaluations";
	}
}
