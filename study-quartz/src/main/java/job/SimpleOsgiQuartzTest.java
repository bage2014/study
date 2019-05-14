package job;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/*
 * All content copyright Terracotta, Inc., unless otherwise indicated. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author hhuynh
 * 
 */
public class SimpleOsgiQuartzTest implements JobListener {
	public static final CyclicBarrier barrier = new CyclicBarrier(2);


	// note this part of code run in osgi container
//	@Test
	public void testQuartz() throws Exception {
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		sched.getListenerManager().addJobListener(this);
		JobDetail job = JobBuilder.newJob(HelloJob.class)
				.withIdentity("job1", "group1").build();
		org.quartz.Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity("trigger1", "group1").startNow().build();
		sched.scheduleJob(job, trigger);
		sched.start();
		await();
		sched.shutdown(true);
	}

	private static void await() {
		try {
			barrier.await(10, TimeUnit.SECONDS);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getName() {
		return "SimpleOsgiQuartzTest";
	}

	public void jobToBeExecuted(JobExecutionContext context) {
		//
	}

	public void jobExecutionVetoed(JobExecutionContext context) {
		//
	}

	public void jobWasExecuted(JobExecutionContext context,
			JobExecutionException jobException) {
		if (context.getJobInstance() instanceof HelloJob) {
			HelloJob job = (HelloJob) context.getJobInstance();
			System.out.println("XXX: count = " + job.getCount());
			await();
		}
	}
}

