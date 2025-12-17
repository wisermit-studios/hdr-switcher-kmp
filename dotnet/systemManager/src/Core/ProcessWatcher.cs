using System.Management;
using SystemManager.Model;

namespace SystemManager.Core;

public class ProcessWatcher
{
    private ManagementEventWatcher? _startWatcher;
    private ManagementEventWatcher? _stopWatcher;

    public void Watch(
        List<Application> applications,
        Action onStart,
        Action onFinish)
    {
        _startWatcher = CreateEventWatcher("Win32_ProcessStartTrace", applications);
        _startWatcher.EventArrived += (s, e) => onStart();
        _startWatcher.Start();

        _stopWatcher = CreateEventWatcher("Win32_ProcessStopTrace", applications);
        _stopWatcher.EventArrived += (s, e) => onFinish();
        _stopWatcher.Start();
    }

    private static ManagementEventWatcher CreateEventWatcher(
        string eventClass, List<Application> executables)
    {
        var whereClause = string.Join(
            " OR ",
            executables.Select(app => $"ProcessName = '{app.File.Name}'")
        );

        return new ManagementEventWatcher(
            new WqlEventQuery($"SELECT * FROM {eventClass} WHERE {whereClause}")
        );
    }

    public void Stop()
    {
        _startWatcher?.Stop();
        _startWatcher?.Dispose();

        _stopWatcher?.Stop();
        _stopWatcher?.Dispose();
    }
}