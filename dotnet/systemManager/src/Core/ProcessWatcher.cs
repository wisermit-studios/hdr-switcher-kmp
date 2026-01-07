using SystemManager.Model;
using Microsoft.Diagnostics.Tracing.Parsers;
using Microsoft.Diagnostics.Tracing.Session;
using SystemManager.Util;

namespace SystemManager.Core;

public class ProcessWatcher
{
    readonly TraceEventSession _session = new("wisermit.systemmanager:ProcessWatcherSession");

    public void Watch(
            List<Application> applications,
            Action onStart,
            Action onFinish)
    {
        HashSet<string> processes = [.. applications.Select(e => e.File.Name.ToLowerInvariant())];

        _session.EnableKernelProvider(KernelTraceEventParser.Keywords.Process);

        _session.Source.Kernel.ProcessStart += e =>
        {
            if (processes.Contains(e.ImageFileName.ToLowerInvariant())) onStart();
        };

        _session.Source.Kernel.ProcessStop += e =>
        {
            if (processes.Contains(e.ImageFileName.ToLowerInvariant())) onFinish();
        };


        Task.Run(() => _session.Source.Process());
        Log.I($"ProcessWatcher started.");
    }

    public void Stop()
    {
        _session.Source.StopProcessing();
        _session.Dispose();
        Log.I($"ProcessWatcher stopped.");
    }
}