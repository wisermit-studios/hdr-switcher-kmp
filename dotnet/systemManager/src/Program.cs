// TODO: Review names and logs.
using System.Threading.Tasks;
using SystemManager.Core;
using SystemManager.Model;
using SystemManager.Util;

namespace SystemManager
{
    public static class Program
    {
        [STAThread]
        static async Task Main(string[] args)
        {
            var logLevel = LogLevel.Debug;
            Log.Level = logLevel;

            ConsoleManager.ReadArgs(args);

            if (args.Length == 1)
            {
                Log.D("Launching process.");
                LaunchExe(args[0]);
            }
            else
            {
                Log.D("Starting service.");
                await StartService(args);
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

        private static async Task StartService(string[] args)
        {
            var exeList = Exe.ListFromArgs(args);
            var manager = new Service();
            manager.Watch(exeList);

            await ConsoleManager.ListenInput();
        }
    }
}
