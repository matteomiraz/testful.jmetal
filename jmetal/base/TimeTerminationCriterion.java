package jmetal.base;

import java.lang.management.ManagementFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * Class for a generic time-based termination criterion
 * @author matteo
 */
public abstract class TimeTerminationCriterion implements TerminationCriterion {

	/**
	 * Get the current millisecond since the creation of this object.
	 * @return the current millisecond elapsed since creation of this object.
	 */
	public abstract long getProgress();

	private final long target;

	public TimeTerminationCriterion(long duration) {
		this.target = duration;
	}

	@Override
	public float getProgressPercent() {
		return (100.0f * getProgress()) / target;
	}

	@Override
	public String getRemaining() {
		final long remaining = (target - getProgress()) / 1000;
		return String.format("%d:%02d",  remaining / 60, remaining % 60);
	}

	@Override
	public long getTarget() {
		return target;
	}

	@Override
	public boolean isTerminated() {
		return getProgress() >= target;
	}

	@Override
	public TimeTerminationCriterion clone() {
		return this;
	}

	/**
	 * This class measure the used CPU time and use it as termination criterion
	 * @author matteo
	 */
	public static class TimeCPU extends TimeTerminationCriterion {
		private static Logger logger = Logger.getLogger("testful.utils");

		private static final MBeanServer mbs;
		private static final ObjectName opSystem;

		static {
			mbs = ManagementFactory.getPlatformMBeanServer();

			ObjectName tmp = null;
			try {
				tmp = new ObjectName("java.lang:type=OperatingSystem");
			} catch (Exception e) {
				logger.log(Level.WARNING, "Cannot access to the operating system JMX: " + e.getMessage(), e);
			}
			opSystem = tmp;
		}

		private final long start;

		public TimeCPU(long duration) throws Exception {
			super(duration);
			start = getProcessCpuTime();
			if(opSystem == null || start < 0)
				throw new Exception("Cannot measure the CPU time!");
		}

		@Override
		public long getProgress() {
			return getProcessCpuTime() - start;
		}

		public static long getProcessCpuTime() {
			if(opSystem != null) {
				try {
					Long time = (Long) mbs.getAttribute(opSystem, "ProcessCpuTime");
					if(time != null) return time / 1000000;
				} catch (Exception e) {
					logger.log(Level.WARNING, "Cannot read the used process CPU time: " + e.getMessage(), e);
				}
			}
			return -1;
		}

	}

	/**
	 * This class measure the elapsed wall-clock time and use it as termination criterion.
	 * @author matteo
	 */
	public static class TimeWall extends TimeTerminationCriterion {

		private long start;

		public TimeWall(long duration) {
			super(duration);
			start = System.currentTimeMillis();
		}

		@Override
		public long getProgress() {
			return System.currentTimeMillis() - start;
		}

	}
}
