using System.Management;
using SystemManager.Model;

namespace SystemManager.Core
{
    public class ProcessWatcher
    {
        private ManagementEventWatcher? startWatcher;
        private ManagementEventWatcher? stopWatcher;

        public void Watch(
            List<Exe> exeList,
            Action onStart,
            Action onFinish)
        {
            startWatcher = CreateEventWatcher("Win32_ProcessStartTrace", exeList);
            startWatcher.EventArrived += (s, e) => onStart();
            startWatcher.Start();

            stopWatcher = CreateEventWatcher("Win32_ProcessStopTrace", exeList);
            stopWatcher.EventArrived += (s, e) => onFinish();
            stopWatcher.Start();
        }

        private static ManagementEventWatcher CreateEventWatcher(
            string eventClass, List<Exe> exeList)
        {
            var whereClause = string.Join(
                " OR ",
                exeList.Select(exe => $"ProcessName = '{exe.Name}'")
            );

            return new ManagementEventWatcher(
                new WqlEventQuery($"SELECT * FROM {eventClass} WHERE {whereClause}")
            );
        }

        public void Dispose()
        {
            startWatcher?.Stop();
            startWatcher?.Dispose();
            startWatcher = null;

            stopWatcher?.Stop();
            stopWatcher?.Dispose();
            stopWatcher = null;
        }
    }
}