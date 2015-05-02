package de.hetzge.sgame.common;

import javolution.util.FastTable;

public class PathfinderThread extends Thread {

	public abstract class PathfinderWorker {
		private Path path;
		private Boolean pathFound;


		public PathfinderWorker() {
			PathfinderThread.this.addWorker(this);
		}

		public abstract Path findPath();

		public void execute() {
			Path path = this.findPath();
			this.path = path;
			this.pathFound = path != null;
		}

		public boolean done() {
			return this.pathFound != null;
		}

		public boolean success() {
			return this.pathFound != null && this.pathFound;
		}

		public boolean failure() {
			return this.pathFound != null && !this.pathFound;
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
