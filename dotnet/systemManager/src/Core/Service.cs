using SystemManager.Util;
using SystemManager.Model;

namespace SystemManager.Core;

public class Service
{
    private readonly ProcessWatcher _processWatcher;

    public Service()
    {
        _processWatcher = new ProcessWatcher();
    }

    public void SetExecutables(List<Application> executables)
    {
        _processWatcher.Watch(
            executables,
            onStart: () =>
            {
                Log.I($"Process started.");
                HdrManager.SetHdrEnabled(true);
            },
            onFinish: () =>
            {
                Log.I($"Process ended.");
                HdrManager.SetHdrEnabled(false);
            }
        );
    }

    public void Stop()
    {
        Log.I($"Stopping service...");

        HdrManager.SetHdrEnabled(false);
        _processWatcher.Dispose();

        Log.I($"Service stopped.");
    }
}