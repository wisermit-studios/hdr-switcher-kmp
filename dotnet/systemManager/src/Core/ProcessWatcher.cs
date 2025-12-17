using System.Management;
using SystemManager.Model;

namespace SystemManager.Core;

public class ProcessWatcher
{
    private ManagementEventWatcher? _startWatcher;
    private ManagementEventWatcher? _stopWatcher;

    public void Watch(
        List<Application> executables,
        Action onStart,
        Action onFinish)
    {
        _startWatcher = CreateEventWatcher("Win32_ProcessStartTrace", executables);
        _startWatcher.EventArrived += (s, e) => onStart();
        _startWatcher.Start();

        _stopWatcher = CreateEventWatcher("Win32_ProcessStopTrace", executables);
        _stopWatcher.EventArrived += (s, e) => onFinish();
        _stopWatcher.Start();
    }

    private static ManagementEventWatcher CreateEventWatcher(
        string eventClass, List<Application> executables)
    {
        var whereClause = string.Join(
            " OR ",
            executables.Select(exe => $"ProcessName = '{exe.File.Name}'")
        );

        return new ManagementEventWatcher(
            new WqlEventQuery($"SELECT * FROM {eventClass} WHERE {whereClause}")
        );
    }

    public void Dispose()
    {
        _startWatcher?.Stop();
        _startWatcher?.Dispose();
        _startWatcher = null;

        _stopWatcher?.Stop();
        _stopWatcher?.Dispose();
        _stopWatcher = null;
    }
}