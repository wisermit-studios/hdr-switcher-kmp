// TODO: Review names and logs.
using SystemManager.Core;
using SystemManager.Framework;
using SystemManager.Model;

namespace SystemManager
{
    public static class Program
    {
        [STAThread]
        static void Main(string[] args)
        {
            var logLevel = LogLevel.Debug;
            Log.Level = logLevel;

            ConsoleHandler.ReadArgs(args);

            if (args.Length == 1)
            {
                Log.D("Launching process.");
                LaunchExe(args[0]);
            }
            else
            {
                Log.D("Starting service.");
                StartService(args);
            }
        }

        private static void LaunchExe(string exePath)
        {
            if (File.Exists(exePath))
            {
                Task.Run(() =>
                    {
                        Launcher.Launch(exePath);
                    }
                );
                Environment.Exit(0);
            }
            else
            {
                Environment.Exit(ErrorCode.ERROR_FILE_NOT_FOUND);
            }
        }

        private static void StartService(string[] args)
        {
            var exeList = Exe.ListFromArgs(args);
            var manager = new Service();
            manager.Watch(exeList);
        }
    }
}
