package de.hetzge.sgame.common;

import javolution.util.FastTable;

public class PathfinderThread extends Thread {

	public abstract class PathfinderWorker {
		private Path path;

		public PathfinderWorker() {
			PathfinderThread.this.addWorker(this);
		}

		public abstract Path findPath();

		public void execute() {
			Path path = this.findPath();
			this.path = path;
		}

		public boolean done() {
			return this.path != null;
		}

		public Path get() {
			return this.path;
		}
	}

	private final FastTable<PathfinderWorker> workers = new FastTable<PathfinderWorker>();

	@Override
	public void run() {
		while (true) {
			PathfinderWorker poll;
			while ((poll = this.workers.poll()) != null) {
				poll.execute();
			}
			Util.sleep(100);
		}
	}

	public void addWorker(PathfinderWorker pathfinderWorker) {
		this.workers.add(pathfinderWorker);
	}

}
