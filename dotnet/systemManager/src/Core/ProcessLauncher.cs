using System.Diagnostics;

namespace SystemManager.Core
{
    public static class Launcher
    {
        private const int START_DELAY = 300;

        public static void Launch(string exePath)
        {
            using var process = new Process();
            process.StartInfo.FileName = exePath;

            if (HdrManager.IsEnabled())
            {
                process.Start();
                return;
            }
            else
            {
                HdrManager.Toggle();
                Thread.Sleep(START_DELAY);

                process.Start();
                process.WaitForExit();

                HdrManager.Toggle();
            }
        }
    }
}