package org.carlspring.strongbox.cron.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

/**
 * @author Kate Novik.
 */
@Component
public class JobManagerImpl
        implements JobManager
{

    private final Map<String, JobExecutionListener> listenerRegistry;

    private final Map<String, Boolean> executedJobs;

    public JobManagerImpl()
    {
        listenerRegistry = new HashMap<>();
        executedJobs = new HashMap<>();
    }

    public synchronized void addExecutedJob(String jobName,
                                            Boolean statusExecuted)
    {
        executedJobs.put(jobName, statusExecuted);
        getJobExecutionListener(jobName).ifPresent(listener -> listener.onJobExecution(jobName, statusExecuted));
    }

    public Map<String, Boolean> getExecutedJobs()
    {
        return executedJobs;
    }

    @Override
    public void registerExecutionListener(String jobName,
                                          JobExecutionListener executionListener)
    {
        if (jobName == null || executionListener == null)
        {
            throw new IllegalArgumentException("Unable to use null jobName or executionListener");
        }

        listenerRegistry.put(jobName, executionListener);
    }

    @Override
    public Optional<JobExecutionListener> getJobExecutionListener(String jobName)
    {
        return Optional.ofNullable(listenerRegistry.get(jobName));
    }

}
