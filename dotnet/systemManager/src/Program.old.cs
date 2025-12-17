using SystemManager.Core;
using SystemManager.Model;
using SystemManager.Util;

namespace SystemManager
{
    public static class ProgramOld
    {
        [STAThread]
        static async Task Main2(string[] args)
        {
            var logLevel = LogLevel.Debug;
            Log.Level = logLevel;

            ConsoleManager.ReadArgs(args);

            if (args.Length == 1)
            {
                Log.D("Launching process.");
                LaunchExecutable(args[0]);
            }
            else
            {
                Log.D("Starting service.");
                await StartService(args);
            }
        }

        private static void LaunchExecutable(string executablePath)
        {
            if (File.Exists(executablePath))
            {
                Launcher.Launch(new(new(executablePath)));
                Environment.Exit(0);
            }
            else
            {
                Environment.Exit(ErrorCode.ERROR_FILE_NOT_FOUND);
            }
        }

        private static async Task StartService(string[] args)
        {
            // var executables = App.ListFromArgs(args);
            // var service = new Service();
            // service.SetExecutables(executables);

            // await ConsoleManager.ListenInput();
        }
    }
}
