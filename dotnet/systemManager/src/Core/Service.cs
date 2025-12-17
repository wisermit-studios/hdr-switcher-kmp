using SystemManager.Util;
using SystemManager.Model;

namespace SystemManager.Core;

public class Service
{
    private readonly ProcessWatcher _processWatcher = new();

    public Service(string dataPath)
    {
        Log.I($"Starting service. Data={dataPath}.");
        // TODO: Load json.
        List<Application> applications = [];
        if (applications.Count == 0)
        {
            Log.I($"No applications to watch.");
        }
        else
        {
            Log.I($"Watching ({applications.Count}) applications.");
            WatchApplications(applications);
        }
    }

    public void WatchApplications(List<Application> applications)
    {
        _processWatcher?.Watch(
            applications,
            onStart: () =>
            {
                Log.I($"Process started.");
                HdrManager.SetHdrEnabled(true);
            },
            onFinish: () =>
            {
                Log.I($"Process finished.");
                HdrManager.SetHdrEnabled(false);
            }
        );
    }

    public void Stop()
    {
        Log.I($"Service stopped.");
        _processWatcher.Stop();
    }
}